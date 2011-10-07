package gov.nara.nwts.ftapp.gui;

import java.util.Iterator;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * Worker thread that will perform an actual file test and send periodic status updates to the GUI thread.
 * Note: this class is the primary reason for running Java 1.6 or higher.  It handles GUI actions elegantly.
 * @author TBrady
 *
 */
class GuiFileTraversalSW extends SwingWorker<String,String>  {
	GuiFileTraversal traversal;
	DirectoryTable dt;
	
	public GuiFileTraversalSW(DirectoryTable dt, DefaultTableModel tm) {
		this.dt = dt;
		traversal = new GuiFileTraversal(this, tm);
	}
	
	public void publish(String s) {
		super.publish(s);
	}
	
	protected String doInBackground() throws Exception {
		try {
			publish("starting");
			traversal.traverseFile();
			publish("Complete");
			return "complete..";
		} catch (Throwable e) {
			e.printStackTrace();
			this.cancel(true);
			return "err...";
		}
	}
	
	protected void process(List<String> messages) {
		if (traversal.myprogress.processing) {
			dt.progressPanel.progress.setValue(traversal.myprogress.dircount+1);
		} 
		for(Iterator<String>i=messages.iterator(); i.hasNext(); ){
			String s = i.next();
			if (s.equals("")) continue;
			dt.report(s); 
		}
		
	}
	

	public void done() {
		if (isCancelled()) {
			publish("Processing cancelled");
		}
		dt.report((traversal.myprogress.dircount+1) + " of " + traversal.myprogress.dirfound + " processed");
	}


}
