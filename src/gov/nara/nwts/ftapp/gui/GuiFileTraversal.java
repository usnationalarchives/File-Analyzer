package gov.nara.nwts.ftapp.gui;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import gov.nara.nwts.ftapp.FileTraversal;
import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.stats.Stats;

/**
 * As files are processed within the FileAnalyzer, this class manages status updates.
 * This class is used by {@link GuiFileTraversalSW} to provide a responsive user experience. 
 * Since GuiFileTraversalSW must extend SwingWorker, this companion class was written so that it could override
 * the default behaviors in the {@link gov.nara.nwts.ftapp.FileTraversal} class.
 * 
 * 
 * @author TBrady
 *
 */
class GuiFileTraversal extends FileTraversal {
	DefaultTableModel tm;
	GuiFileTraversalSW gftSW;
	
	MyProgress myprogress;
	
	public void completeDirectoryScan() {	
		myprogress.complete(false);
	}
	public void completeFileScan() {		
		myprogress.complete(true);
	}
	public int getNumProcessed() {
		return tm.getRowCount();
	}

	public GuiFileTraversal(GuiFileTraversalSW gftsw, DefaultTableModel tm) {
		super(gftsw.dt);
		gftSW = gftsw;
		this.tm = tm;
		myprogress = new MyProgress(this);
	}
	
	public void increment() {
		myprogress.increment();
	}
	
	public void reportCancel() {
		gftSW.publish("Stopping: " +max + " items found.");
	}

	
	public void checkFile(File thefile, FileTest fileTest) {
		Vector<Object> v = new Vector<Object>();
		String s = thefile.getParent();
		int len = gftSW.dt.root.getPath().length();
		if (s.length() > len) s = s.substring(len);
		v.add(s);
		String name = thefile.getName();
		v.add(name);
		v.add(fileTest.getExt(thefile));
		if (fileTest.isTestable(thefile)){
			Stats mystats = fileTest.getStats(thefile);
			long size = thefile.length(); 
			v.add(new Long(size));
			v.add(new Date(thefile.lastModified()));
			Object o = null;
			if (mystats!=null){
				try {
					o = mystats.compute(thefile, fileTest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			v.add((o == null) ? "" : o);
			tm.addRow(v);			
		} 
	}
	public void checkDirFile(File thefile, FileTest fileTest) {
		Vector<Object> v = new Vector<Object>();
		if (thefile.equals(gftSW.dt.root)){
			v.add("root");
		} else {
			v.add(thefile.getParent().substring(gftSW.dt.root.getPath().length()));
		}
		String name = thefile.getName();
		v.add(name);
		v.add("");
		if (fileTest.isTestable(thefile)){
			Stats mystats = fileTest.getStats(thefile);
			v.add(new Long(0));
			v.add(new Date(thefile.lastModified()));
			Object o = mystats.compute(thefile, fileTest);
			v.add((o == null) ? "" : o);					
			tm.addRow(v);
		}
	}
	public void clear() {
		tm.setNumRows(0);		
		myprogress.resetDirCount();
	}
	
	public boolean traverseFile() {
		return traverseFile(fileTest, max);
	}
	public void reportDuration(double duration) {
	}

}
