package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Statistics showing accumulated file counts within a directory structure.
 * @author TBrady
 *
 */
public class DirStats extends Stats {
	public static Object[][] details = {
			{String.class,"Key",300},
			{Long.class,"Count",100},
			{Long.class,"Cumulative Count",100},
		};

	
	public DirStats(String key) {
		super(key);
		vals.add(new Long(0));
		vals.add(new Long(0));
	}
	
	public Object compute(File f, FileTest fileTest) {
		File root = fileTest.getRoot();
		for(File ftest = f.getParentFile(); ftest!=null; ftest = ftest.getParentFile()){
			DirStats stats = (DirStats)fileTest.getStats(fileTest.getKey(f,ftest));
			stats.accumulate(f, fileTest, ftest);
			if (ftest.equals(root)){
				break;
			}
		}
		return fileTest.fileTest(f);
	}
	
	public void accumulate(File f, FileTest fileTest, File parentdir) {
		Long count = (Long)vals.get(0);
		if (f.getParentFile().equals(parentdir)){
			vals.set(0, count.longValue()+1);
		}
		count = (Long)vals.get(1);
		vals.set(1, count.longValue()+1);
	}
}
