package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DataStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Abstract class for rules that report on checksum values; derived versions of this class will provide a specific hashing algorithm from the Java core library.
 * @author TBrady
 *
 */
abstract class NameChecksum extends DefaultFileTest {

	public NameChecksum(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Sort By Checksum";
	}
	public String getKey(File f) {
		return f.getName();
	}
	
    public String getShortName(){return "Checksum";}

    abstract public MessageDigest getMessageDigest() throws NoSuchAlgorithmException;
    
    public String getChecksum(File f) {
    	FileInputStream fis = null;
		try {
			MessageDigest md = getMessageDigest();
			fis = new FileInputStream(f);
			byte[] dataBytes = new byte[1204];
			int nread = 0;
			while((nread = fis.read(dataBytes)) != -1){
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<mdbytes.length; i++){
				sb.append(Integer.toString((mdbytes[i] & 0xFF) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;    	
    }
    
	public Object fileTest(File f) {
		return getChecksum(f);
	}
    public Stats createStats(String key){ 
    	return new DataStats(key);
    }
    public Object[][] getStatsDetails() {
    	return DataStats.details;
    }
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "This test reports the checksum for a given filename.\nNote, the checksum will be overwritten if the file is found more than once.";
	}
    public void progress(int count) {
    	if (count % 5000 == 0) {
    		cleanup(count);
    	} else if (count % 100 == 0){
    		showCount(count);
    	}
    }

}
