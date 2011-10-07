package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Status objects that counts items by key.
 * @author TBrady
 *
 */
public class CountStats extends Stats {
	public static Object[][] details = {
			{String.class,"Type",100},
			{Long.class,"Count",100},
		};

	
	public CountStats(String key) {
		super(key);
		vals.add(new Long(0));
	}
	
	public Object compute(File f, FileTest fileTest) {
		Long count = (Long)vals.get(0);
		vals.set(0, count.longValue()+1);
		return fileTest.fileTest(f);
	}
}
