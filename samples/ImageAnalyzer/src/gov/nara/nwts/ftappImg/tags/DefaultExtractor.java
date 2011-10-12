package gov.nara.nwts.ftappImg.tags;

import java.util.HashMap;

import gov.nara.nwts.ftappImg.tags.ImageTags.DUP;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;

/** 
 * Abstract base class for image extractors
 * @author TBrady
 *
 */
public abstract class DefaultExtractor implements Extractor {

	public abstract void close();
	public String getString(DUP dup) {
		return getString(dup, false);
	}
	public String getString(DUP dup, boolean getFirst) {
		HashMap<TAGS,String> tagvals = new HashMap<TAGS,String>();
		HashMap<String,Integer> vals = new HashMap<String,Integer>();
		for(TAGS ctag: dup.duptags) {
			String s = getString(ctag);
			if (s == null) continue;
			s = s.trim();
			if (s.equals("")) continue;
			if (getFirst) return s;
			tagvals.put(ctag,s);
			Integer i = vals.get(s);
			if (i == null) {
				vals.put(s, 1);
			} else {
				vals.put(s, i+1);
			}
		}
		if (vals.size() == 0) return "";
		String first = vals.keySet().iterator().next(); 
		if (vals.size() == 1) {
			return first;
		}
		return "["+vals.size()+"] " + first; 
	}

}
