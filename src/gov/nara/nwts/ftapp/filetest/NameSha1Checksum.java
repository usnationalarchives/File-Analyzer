package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate SHA1 checksums for a set of files.
 * @author TBrady
 *
 */
class NameSha1Checksum extends NameChecksum {

	public NameSha1Checksum(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Get SHA1 Checksum By Name";
	}
	
    public String getShortName(){return "SHA1";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("SHA1");
    }

	public String getDescription() {
		return "This test reports the SHA1 checksum for a given filename.\nNote, the checksum will be overwritten if the file is found more than once.";
	}

}
