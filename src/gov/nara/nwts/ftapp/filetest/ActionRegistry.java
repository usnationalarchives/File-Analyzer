package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;

import java.util.Vector;
/**
 * Activates the FileTest objects that will be made available to a user.
 * In the event of a load failure of an individual class, the runtime loading of those classes can be disabled in this method.
 * @author TBrady
 *
 */
public class ActionRegistry extends Vector<FileTest> {
	
	private static final long serialVersionUID = 1L;
	boolean modifyAllowed = true;

	public ActionRegistry(FTDriver dt, boolean modifyAllowed) {
		this.modifyAllowed = modifyAllowed;
		add(new CountByType(dt));
		add(new ListDirectories(dt));
		add(new NameMatch(dt));
		add(new BaseNameMatch(dt));
		add(new NameMD5Checksum(dt));
		add(new NameSha1Checksum(dt));
		add(new NameSha256Checksum(dt));
		add(new ByMD5Checksum(dt));
		add(new DirMatch(dt));
		add(new DirTypeNameMatch(dt));
		add(new RandomFileTest(dt));
		FileTest next = new LowercaseTest(dt, null); 
		add(next);
		if (modifyAllowed()) {
			add(new LowercaseTest(dt, next));
		}
	}
	
	/**
	 * By design, File Analyzer actions should not be destructive.
	 * When implementing routines that will automatically rename items (vs validating file names), the registry can be configured to only load those actions for specific users.
	 * Also, the base File Analyzer module can be initiated with or without modification enabled.
	 * This check is only enforced if the FileTest honors this flag.
	 * 
	 */
	public boolean modifyAllowed() {
		return modifyAllowed;
	}
	
}
