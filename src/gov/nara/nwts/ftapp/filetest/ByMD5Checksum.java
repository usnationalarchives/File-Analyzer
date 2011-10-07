package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.CountAppendStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * List files by MD5 checksum; this can be used to identify potentially duplicate digital objects.
 * @author TBrady
 *
 */
class ByMD5Checksum extends NameChecksum {

	public ByMD5Checksum(FTDriver dt) {
		super(dt);
	}
	public String getKey(File f) {
		return getChecksum(f);
	}
	public Object fileTest(File f) {
		return f.getAbsolutePath();
	}
    public Stats createStats(String key){ 
    	return new CountAppendStats(key);
    }
    public Object[][] getStatsDetails() {
    	return CountAppendStats.details;
    }

	public String toString() {
		return "By MD5 Checksum";
	}
    public String getShortName(){return "By MD5";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("MD5");
    }

	public String getDescription() {
		return "Lists files by MD5 checksum.\nNote, the checksum will be overwritten if the file is found more than once.";
	}
}
