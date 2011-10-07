package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A name validation pattern which will return {@link RenameStatus#RENAMABLE} if true indicating that the file could be automatically renamed.
 * @author TBrady
 *
 */
public abstract class RenameablePattern extends NameValidationPattern {
	
    public RenameablePattern(String pattern, boolean checkPath){
    	this(pattern, checkPath, 0);
    }
    public RenameablePattern(String pattern, boolean checkPath, int pattFlags){
    	super(Pattern.compile(pattern, pattFlags), checkPath, RenameStatus.RENAMABLE);
    }
    public RenameDetails report(File f, Matcher m) {
    	File nf = getNewFile(f, m);
    	if (nf == null) {
    		return new RenameDetails(RenameStatus.NEW_NAME_INVALID, nf, getMessage(f, m));
    	}
		return new RenameDetails(status, nf, getMessage(f, m));    	
    }
    
}
