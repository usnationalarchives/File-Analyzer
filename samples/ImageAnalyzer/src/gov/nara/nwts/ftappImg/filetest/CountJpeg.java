package gov.nara.nwts.ftappImg.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filetest.DefaultFileTest;
import gov.nara.nwts.ftapp.filter.JpegFileTestFilter;
import gov.nara.nwts.ftappImg.stats.JpegStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Extract key metadata from a JPG file; this implementation has been superceeded by better implementations
 * @author TBrady
 *
 */
class CountJpeg extends DefaultFileTest { 

	public CountJpeg(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Jpeg Properties";
	}
	public String getKey(File f) {
		return f.getName();
	}
	
    public String getShortName(){return "Jpeg";}

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new JpegStats(key);
    }
    public Object[][] getStatsDetails() {
    	return JpegStats.details;
    }

	public void initFilters() {
		filters.add(new JpegFileTestFilter());
	}

	public String getDescription() {
		return "This test will extract key metadata items from each Jpeg file that is found";
	}

}
