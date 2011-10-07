package gov.nara.nwts.ftapp.filetest;

import java.io.File;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.Stats;

/**
 * List the full path for a dirctory; this can be used as input for the FileAnalzyer batch capability.
 * @author TBrady
 *
 */
class ListDirectories extends DefaultFileTest {
	public static Object[][] details = {
		{String.class,"Folder",250},
		{String.class,"Name",100},
	};
	
	public ListDirectories(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "List Dir";
	}

	public Object fileTest(File f) {
		return f.getName();
	}

	public String getKey(File f) {
		String path = f.getAbsolutePath();
		return path;
	}

	public Stats createStats(String key) {
		Stats stats = new Stats(key) {
			public Object compute(File f, FileTest fileTest) {
				vals.set(0, f.getName());
				return f.getName();
			}
		};
		stats.vals.add("");
		return stats;
	}

	public Object[][] getStatsDetails() {
		return details;
	}

	public String getShortName() {
		return "DIR";
	}


	public String getDescription() {
		return "Generate a list of directories";
	}

    public boolean isTestDirectory() {
    	return true;
    }
    public boolean isTestFiles() {
    	return false;
    }
}
