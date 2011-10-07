package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A name validation pattern which will return {@link RenameStatus#VALID} if true.
 * @author TBrady
 *
 */
public class ValidPattern extends NameValidationPattern {
	
    public ValidPattern(String pattern, boolean checkPath){
    	this(pattern, checkPath, 0);
    }
    public ValidPattern(String pattern, boolean checkPath, int pattFlags){
    	super(Pattern.compile(pattern, pattFlags), checkPath, RenameStatus.VALID);
    }
    
    public File getNewFile(File f, Matcher m) {
    	return null;
    }
	public String getMessage(File f, Matcher m) {
		return "";
	}
}
