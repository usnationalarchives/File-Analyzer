package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.FileCountStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
/**
 * Count items by file extension; this is the most basic and easy to understand File Analyzer rule.
 * @author TBrady
 *
 */
class CountByType extends DefaultFileTest {

	public CountByType(FTDriver dt) {
		super(dt);
	}

	public Object fileTest(File f) {
		return null;
	}

	public String toString() {
		return "Count Files By Type";
	}

    public Stats createStats(String key){ 
    	return new FileCountStats(key);
    }
    public Object[][] getStatsDetails() {
    	return FileCountStats.details;
    }
    public String getShortName(){return "By Type";}
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "This test counts the number of files found by file extension.";
	}
}
