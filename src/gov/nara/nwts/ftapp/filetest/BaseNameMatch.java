package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.NameStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
/**
 * Match files based on the base file name (without a file extension).
 * @author TBrady
 *
 */
class BaseNameMatch extends DefaultFileTest {

	public BaseNameMatch(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Match By Base Name";
	}
	public String getKey(File f) {
		String s = f.getName();
		String[] sa = s.split("\\.");
		if (sa.length > 1) s = s.substring(0,s.length()-sa[sa.length-1].length()-1);
		return s; 
	}
	
    public String getShortName(){return "Base Name";}

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
		return "This test reports on file size by base name (no extension) regardless of the directory in a file name is found.";
	}

}
