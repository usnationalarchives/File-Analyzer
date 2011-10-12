package gov.nara.nwts.ftappImg.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filetest.DefaultFileTest;
import gov.nara.nwts.ftapp.filter.TiffFileTestFilter;
import gov.nara.nwts.ftappImg.stats.ImageStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Extract key metadata from a TIF file; this implementation has been superceeded by better implementations
 * @author TBrady
 *
 */
class CountTiff extends DefaultFileTest { 

	public CountTiff(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Tif Properties";
	}
	public String getKey(File f) {
		return f.getName();
	}
	
    public String getShortName(){return "Tif";}

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new ImageStats(key);
    }
    public Object[][] getStatsDetails() {
    	return ImageStats.details;
    }

	public void initFilters() {
		filters.add(new TiffFileTestFilter());
	}

	public String getDescription() {
		return "This test will extract key metadata items from each TIF file that is found";
	}

}
