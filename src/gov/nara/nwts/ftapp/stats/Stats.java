package gov.nara.nwts.ftapp.stats;

import java.io.File;
import java.util.Vector;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Base class for Stats objects containing a key and a variable lenght list of values.
 * @author TBrady
 *
 */
public class Stats {
	public static Object[][] details = {
		{String.class,"Type",100},
	};

	public Vector<Object> vals;
	public String key;
	
	 public Stats(String key) {
		this.key = key;
		vals = new Vector<Object>();
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object o = fileTest.fileTest(f);
		return o;
	}
}
