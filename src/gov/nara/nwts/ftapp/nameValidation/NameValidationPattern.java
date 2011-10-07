package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class containing a regular expression pattern that will be used to perform file name validation
 * @author TBrady
 *
 */
public abstract class NameValidationPattern {
	Pattern pattern;  
	RenameStatus status;
	boolean checkPath;
	
    public NameValidationPattern(Pattern pattern, boolean checkPath, RenameStatus status){
    	this.pattern = pattern;
    	this.status = status;
    	this.checkPath = checkPath;
    }
    
    public RenameDetails checkFile(File f) {
    	String s = checkPath ? f.getAbsolutePath() : f.getName();
    	Matcher m = pattern.matcher(s);    
    	if (m.matches()) {
    		return report(f, m);
    	}
    	return new RenameDetails(RenameStatus.NEXT, null, "");
    }
    
    public RenameDetails report(File f, Matcher m) {
		return new RenameDetails(status, getNewFile(f, m), getMessage(f, m));    	
    }
    
    public abstract File getNewFile(File f, Matcher m);
    
    public abstract String getMessage(File f, Matcher m);
    
}
