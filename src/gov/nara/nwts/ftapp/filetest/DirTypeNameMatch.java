package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DirTypeStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Create FileAnalyzer statistics by directory and by FileType.
 * This routine is useful for understanding the contents of a large volume of material.
 * This routine was built as a sample rule for NWME.
 * @author TBrady
 *
 */
class DirTypeNameMatch extends DirMatch {

	public DirTypeNameMatch(FTDriver dt) {
		super(dt);
	}
	public String toString() {
		return "Count By Type and Dir";
	}
	public String getKey(File f) {
		return getKey(f, f.getParentFile());
	}
	
	public String getKey(File f, Object parentdir) {
		String key = getExt(f);
		if (parentdir instanceof File) {
			key = getExt(f)+": " + ((File)parentdir).getAbsolutePath().substring(getRoot().getAbsolutePath().length());
		}
		return key;		
	}
	
    public String getShortName(){return "Type&Dir";}
    public Stats createStats(String key){ 
    	return new DirTypeStats(key);
    }
    public Object[][] getStatsDetails() {
    	return DirTypeStats.details;
    }
	public String getDescription() {
		return "This test counts the number of occurrences of a specific filetype within a directory.  \nA cumulative total is counted for each parent directory.";
	}

}
