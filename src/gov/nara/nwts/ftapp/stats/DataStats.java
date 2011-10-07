package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Stats object that generically displays a key and data value.  When using this Stats object, the assumption is that the FileTest will customize the column headers.
 * @author TBrady
 *
 */
public class DataStats extends Stats {
	public static Object[][] details = {
			{String.class,"Key",200},
			{Object.class,"Data",300},
		};

	
	public DataStats(String key) {
		super(key);
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object o = fileTest.fileTest(f);
		vals.set(0, o);
		return o;
	}
}
