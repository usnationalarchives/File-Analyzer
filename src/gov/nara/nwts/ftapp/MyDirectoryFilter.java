package gov.nara.nwts.ftapp;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter for identifying directories when traversing a directory tree
 * @author TBrady
 *
 */
public class MyDirectoryFilter implements FilenameFilter {
	boolean ignorePeriods;
    public MyDirectoryFilter(boolean ignorePeriods){
    	this.ignorePeriods = ignorePeriods;
    }
	public boolean accept(File dir, String filename) {

		if (ignorePeriods) {
			if (filename.contains(".")) return false;
		}
		return (new File(dir,filename)).isDirectory();
	}
	
}
