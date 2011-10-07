package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class handling an individual name validation check that requires custom coding beyond a regular expression match.
 * @author TBrady
 *
 */
public abstract class CustomPattern extends NameValidationPattern {
	
    public CustomPattern(String pattern, boolean checkPath){
    	this(pattern, checkPath, 0);
    }
    public CustomPattern(String pattern, boolean checkPath, int pattFlags){
    	super(Pattern.compile(pattern, pattFlags), checkPath, RenameStatus.NEXT);
    }
    public CustomPattern(Pattern pattern, boolean checkPath){
    	super(pattern, checkPath, RenameStatus.NEXT);
    }
    
    public File getNewFile(File f, Matcher m) {
    	return null;
    }
    public String getMessage(File f, Matcher m) {
    	return "";
    }
    public abstract RenameDetails report(File f, Matcher m);
}
