package gov.nara.nwts.ftapp.gui;

/**
 * Helper class that reports on File Analyzer progress (on the Progress Tab)
 * @author TBrady
 *
 */
class MyProgress {
	int dircount;
	int lastdircount;
	int rptgap = 1;
	int rptgapmult = 5;
	int dirfound = 0;
	boolean processing;
	GuiFileTraversal gft;
	DirectoryTable dt;

	public MyProgress(GuiFileTraversal gft) {
		this.gft = gft;
		dt = gft.gftSW.dt;
	}
	
	public void resetDirCount() {
		dircount = 0;
		lastdircount = 0;
		rptgap = 1;
	}
	
	public void increment() {
		dircount++;
		testDirCount(false,processing ? " directories processed" : " directories found");
	}

	public void testDirCount(boolean b, String note) {
		if (processing) {
			gft.gftSW.publish("");
		} else if (b || (dircount >= lastdircount + rptgap)) {
			gft.gftSW.publish(dircount + " dirs "+note);
			lastdircount = dircount;
			if (dircount > rptgap*rptgapmult*2) {
				rptgap *= rptgapmult;
				gft.gftSW.publish("... Reporting every "+ rptgap);
			}
		}		
	}
	
	public void complete(boolean processed) {
		testDirCount(true, processed ? " total directories processed" : " total directories found");
		if (!processed){
		dt.progressPanel.progress.setMaximum(dircount);
		dirfound = dircount;
		resetDirCount();
		processing = !processed;
		}
	}
}