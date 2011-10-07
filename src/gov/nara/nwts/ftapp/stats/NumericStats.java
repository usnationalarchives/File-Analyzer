package gov.nara.nwts.ftapp.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;

import java.io.File;

/**
 * Stats object showing a key and a numeric value.
 * This was used in one of the Census rules, it may not have much additional use.
 * @author TBrady
 *
 */
public class NumericStats extends Stats {
	static Object[][] details = {
			{String.class,"Key",100},
			{Long.class,"Computed Count",100},
		};

	
	public NumericStats(String key) {
		super(key);
		vals.add(new Long(0));
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object count = fileTest.fileTest(f);
		vals.set(0, count);
		return count;
	}
}
