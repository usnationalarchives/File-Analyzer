package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.NameStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
/**
 * Match files by file name (with file extension); this can be used to verify the completeness of a copy operation.
 * @author TBrady
 *
 */
class NameMatch extends DefaultFileTest {

	public NameMatch(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Match By Name";
	}
	public String getKey(File f) {
		return f.getName();
	}
	
    public String getShortName(){return "Name";}

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new NameStats(key);
    }
    public Object[][] getStatsDetails() {
    	return NameStats.details;
    }
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "This test reports on file size by name regardless of the directory in a file name is found.";
	}

}
