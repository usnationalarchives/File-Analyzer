package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DirStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Create FileAnalyzer statistics by directory.
 * @author TBrady
 *
 */
class DirMatch extends DefaultFileTest {

	public DirMatch(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Match By Path";
	}
	public String getKey(File f) {
		return getKey(f, f.getParentFile());
	}
	
	public String getKey(File f, Object parentdir) {
		String key = "";
		if (parentdir instanceof File) {
			key = ((File)parentdir).getAbsolutePath().substring(getRoot().getAbsolutePath().length());
		}
		return key;		
	}
	
    public String getShortName(){return "Path";}

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new DirStats(key);
    }
    public Object[][] getStatsDetails() {
    	return DirStats.details;
    }
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "This test counts the number of items found in a specific directory.  This test will also compute cumulative totals found for each directory that is scanned.";
	}

}
