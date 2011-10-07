package gov.nara.nwts.ftapp.nameValidation;

/**
 * Enumeration indicating all of the possible results of a file name/directory name validation test.
 * @author TBrady
 *
 */
public enum RenameStatus {
	NEXT("Test inconclusive", RenamePassFail.PASS),
	VALID("Original filename is valid, rename not required", RenamePassFail.PASS),
	INVALID_MANUAL("Original filename must be manually renamed",RenamePassFail.FAIL),
	PARSE_ERR("Original filename could not be parsed",RenamePassFail.FAIL),
	NEW_NAME_INVALID("File not renamed, new name is invalid",RenamePassFail.FAIL),
	RENAMABLE("New filename is valid (rename not performed)", RenamePassFail.PASS),
	RENAMED("Renamed",RenamePassFail.PASS),
	RENAME_FILE_EXISTS("Rename failed, file already exists", RenamePassFail.FAIL),
	RENAME_FAILURE("Rename failed", RenamePassFail.FAIL),
	DIRECTORY("Directory: Not Tested",RenamePassFail.PASS),
	DIRECTORY_VALID("Valid Directory",RenamePassFail.PASS),
	DIRECTORY_EMPTY("Empty Directory",RenamePassFail.FAIL),
	DIRECTORY_CHILD_INVALID("Directory contains Invalid File",RenamePassFail.FAIL),
	DIRECTORY_NAME_INVALID("Directory name is Invalid",RenamePassFail.FAIL),
	DIRECTORY_SEQUENCE_ERROR("Sequence Error in Directory",RenamePassFail.FAIL),
	DIRECTORY_INCOMPLETE("Incomplete Directory",RenamePassFail.FAIL);
	String message;
	RenamePassFail passfail;
	RenameStatus(String message, RenamePassFail passfail) {
		this.message = message;
		this.passfail = passfail;
	}
	public RenamePassFail getPassFail() {return passfail;}
	public String getMessage() {return message;}
};
