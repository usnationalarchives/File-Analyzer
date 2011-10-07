package gov.nara.nwts.ftapp.filetest;

import java.io.File;
import java.util.regex.Matcher;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.nameValidation.RenameablePattern;
import gov.nara.nwts.ftapp.nameValidation.ValidPattern;
/**
 * Filename validation rule to ensure that filenames are lowercase.
 * @author TBrady
 *
 */
class LowercaseTest extends NameValidationTest {

	public LowercaseTest(FTDriver dt, FileTest nextTest) {
		super(dt, new ValidPattern("^[^A-Z]*$", false),nextTest, "Lowercase","Lowercase");
		testPatterns.add(new RenameablePattern(".*", false){
			public String getMessage(File f, Matcher m) {
				return "";
			}

			public File getNewFile(File f, Matcher m) {
				return new File(f.getParentFile(), f.getName().toLowerCase());
			}
			
		});
	}


}
