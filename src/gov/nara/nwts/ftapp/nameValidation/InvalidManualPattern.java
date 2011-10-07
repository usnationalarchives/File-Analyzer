package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A name validation pattern which will return {@link RenameStatus#INVALID_MANUAL} if true indicating that a file cannot be programmatically renamed.
 * @author TBrady
 *
 */
public abstract class InvalidManualPattern extends NameValidationPattern {
	
    public InvalidManualPattern(String pattern, boolean checkPath){
    	this(pattern, checkPath, 0);
    }
    public InvalidManualPattern(String pattern, boolean checkPath, int pattFlags){
    	super(Pattern.compile(pattern, pattFlags), checkPath, RenameStatus.INVALID_MANUAL);
    }
    
    public File getNewFile(File f, Matcher m) {
    	return null;
    }
}
