package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate MD5 checksums for a set of files.
 * @author TBrady
 *
 */
class NameMD5Checksum extends NameChecksum {

	public NameMD5Checksum(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Get MD5 Checksum By Name";
	}
    public String getShortName(){return "MD5 ";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("MD5");
    }

	public String getDescription() {
		return "This test reports the MD5 checksum for a given filename.\nNote, the checksum will be overwritten if the file is found more than once.";
	}
}
