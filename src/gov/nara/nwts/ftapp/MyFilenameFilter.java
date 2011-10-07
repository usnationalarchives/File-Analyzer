package gov.nara.nwts.ftapp;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Filter used to identify files for analysis honoring the values supplied within the filter options
 * @author TBrady
 *
 */
public class MyFilenameFilter implements FilenameFilter {
	boolean ignorePeriods;
	String prefix;
	String contains;
	String suffix;
	String exclusion;
	
	boolean isRePrefix;
	boolean isReContains;
	boolean isReSuffix;
	boolean isReExclusion;
	
	boolean isTestFiles;
	
	Pattern rePrefix;
	Pattern reContains;
	Pattern reSuffix;
	Pattern reExclusion;
	
    public MyFilenameFilter(
    	boolean ignorePeriods, 
    	String prefix, 
    	boolean isRePrefix, 
    	String contains, 
    	boolean isReContains, 
    	String suffix,
    	boolean isReSuffix,
    	String exclusion,
    	boolean isReExclusion,
    	boolean isTestFiles
    ){
    	this.ignorePeriods = ignorePeriods;
    	this.prefix = prefix;
    	this.contains = contains;
    	this.suffix = suffix;
    	this.exclusion = exclusion;
    	
    	this.isRePrefix = isRePrefix;
    	this.isReContains = isReContains;
    	this.isReSuffix = isReSuffix;
    	this.isReExclusion = isReExclusion;
    	
    	this.isTestFiles = isTestFiles;
    	
    	if (isRePrefix)	
    		rePrefix = Pattern.compile(prefix, Pattern.CASE_INSENSITIVE);
    	if (isReContains)	
    		reContains = Pattern.compile(contains, Pattern.CASE_INSENSITIVE);
    	if (isReSuffix)	
    		reSuffix = Pattern.compile(suffix, Pattern.CASE_INSENSITIVE);
    	if (isReExclusion)	
    		reExclusion = Pattern.compile(exclusion, Pattern.CASE_INSENSITIVE);
    }
	public boolean accept(File dir, String filename) {

		if (ignorePeriods) {
			if (!filename.contains(".")){
				if ((new File(dir,filename)).isDirectory()){
					return true;
				}						
			}
		}else {
			if ((new File(dir,filename)).isDirectory()){
				return true;
			}									
		}
		
		if (!isTestFiles) return false;
		
		if (prefix.length() > 0){
			if (isRePrefix) {
				if (!rePrefix.matcher(filename).matches())
					return false;
			} else if (!filename.toLowerCase().startsWith(prefix.toLowerCase())){
				return false;
			}			
		}
		if (contains.length() > 0){
			if (isReContains) {
				if (!reContains.matcher(filename).matches())
					return false;
			} else if (!filename.toLowerCase().contains(contains.toLowerCase())){
				return false;
			}			
		}
		if (suffix.length() > 0){
			if (isReSuffix) {
				if (!reSuffix.matcher(filename).matches())
					return false;
			} else if (!filename.toLowerCase().endsWith(suffix.toLowerCase())){
				return false;
			}			
		}
		if (exclusion.length() > 0){
			if (isReExclusion) {
				if (reExclusion.matcher(filename).matches())
					return false;
			} else if (filename.toLowerCase().contains(exclusion.toLowerCase())){
				return false;
			}			
		}
		return true;
	}
	
}
