package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special file name validation logic that applies to directories.  
 * When naming conventions exist for files, there is usually a different rule governing the containing directories.
 * This class also has a facility to ensure unique and sequential sequencing of individual file names within a directory
 * @author TBrady
 *
 */
public class DirAnalysis{

	public static RenameDetails analyze(File dir, Pattern p, int group, Pattern pIgnore){
		return analyze(dir,p,group,pIgnore,true);
	}
	public static RenameDetails analyze(File dir, Pattern p, int group, Pattern pIgnore, boolean rptIgnore){
		ArrayList<String> names = new ArrayList<String>();
		for(String s: dir.list()){
			names.add(s);
		}
		return analyze(names, dir,p,group,pIgnore,rptIgnore);
	}
	public static RenameDetails recursiveAnalyze(File dir, Pattern p, int group, Pattern pIgnore, boolean rptIgnore){
		ArrayList<String> names = new ArrayList<String>();
		gatherFileNames(names, dir);
		return analyze(names, dir,p,group,pIgnore,rptIgnore);
	}
	
	public static void gatherFileNames(List<String>names, File f) {
		if (f.isDirectory()) {
			for(File cf: f.listFiles()){
				gatherFileNames(names, cf);
			}
		} else {
			names.add(f.getName());
		}
	}
	
	public static RenameDetails analyze(List<String> names, File dir, Pattern p, int group, Pattern pIgnore, boolean rptIgnore){
		TreeMap<Integer,Integer> seq = new TreeMap<Integer,Integer>();
		StringBuffer buf = new StringBuffer();
		buf.append(names.size());
		buf.append(" Files: ");

		boolean invalid = false;

		if (names.size() == 0) {
			return new RenameDetails(RenameStatus.DIRECTORY_EMPTY, null, null);
		}

		if (p == null) {
			return new RenameDetails(RenameStatus.DIRECTORY, null, buf.toString());
		}
		
		for(String name: names){
			Matcher m = p.matcher(name);
			if (m.matches()) {
				if (group < 0) continue;
				if (group > m.groupCount()) continue;
				String s = m.group(group);
				try {
					int cur = Integer.parseInt(s);
					Integer x = seq.get(cur);
					if (x == null) x = 0;
					//handle dup?
					seq.put(cur,x++);
				} catch(NumberFormatException e) {
					invalid=true;
					buf.append(" ["+name+"]");
				}
			} else if (pIgnore.matcher(name).matches()){
				if (rptIgnore) buf.append(" {Skip: "+name+"}");
			} else {
				buf.append(" ["+name+"]");
				invalid=true;
			}
		}
		
		if (invalid) {
			return new RenameDetails(RenameStatus.DIRECTORY_CHILD_INVALID, null, buf.toString());
		}
		
		if (seq.size() == 0) {
			return new RenameDetails(RenameStatus.DIRECTORY_INCOMPLETE, null, buf.toString());
		}
		boolean sequenceValid = true;
		int last = -1;
		int last_rangestart = -1;
		for(int cur: seq.keySet()){ 
			if (last == -1) {
				buf.append(cur);
				last_rangestart = cur;
			}else if (cur != last+1) {
				if (last_rangestart != last) {
					buf.append(" - " + last);
				}
				buf.append(", " + cur);
				last_rangestart = cur;
				sequenceValid = false;
			}
			last = cur;
		}
		if (last != last_rangestart) {
			buf.append(" - " + last);
		}
		
		if (sequenceValid) {
			return new RenameDetails(RenameStatus.DIRECTORY_VALID, null, buf.toString());			
		}
		return new RenameDetails(RenameStatus.DIRECTORY_SEQUENCE_ERROR,	null, buf.toString());
	}
	
}

