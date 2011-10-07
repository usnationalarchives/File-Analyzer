package gov.nara.nwts.ftapp.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;

import java.io.File;

/**
 * Stats object showing file name and file size.
 * @author TBrady
 *
 */
public class NameStats extends Stats {
	public static Object[][] details = {
			{String.class,"Name",250},
			{Long.class,"Size",100},
		};

	
	public NameStats(String key) {
		super(key);
		vals.add(new Long(0));
	}
	
	public Object compute(File f, FileTest fileTest) {
		Long size = f.length();
		vals.set(0, size.longValue()+1);
		return fileTest.fileTest(f);
	}
}
