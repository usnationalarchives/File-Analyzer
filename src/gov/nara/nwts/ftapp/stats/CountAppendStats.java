package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Stats class that accumulates counts as well as accumulates notes into a text buffer as processing continues.
 * When using a checksum algorithm to find duplicate values, this routine will provide details about instances in which more than one file share the same checksum.
 * @author TBrady
 *
 */
public class CountAppendStats extends CountStats {
	public static Object[][] details = {
			{String.class,"Type",100},
			{Long.class,"Count",100},
			{String.class,"Details",2000},
		};

	
	public CountAppendStats(String key) {
		super(key);
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		vals.set(1, vals.get(1) + f.getAbsolutePath()+"; ");
		return super.compute(f, fileTest);
	}
}
