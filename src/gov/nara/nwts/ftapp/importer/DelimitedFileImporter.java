package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.ActionResult;
import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.Timer;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Abstract class handling the import of a character-delimited text file allowing for individual values to be wrapped by quotation marks.
 * @author TBrady
 *
 */
public abstract class DelimitedFileImporter extends DefaultImporter {
	public DelimitedFileImporter(FTDriver dt) {
		super(dt);
	}
	boolean forceKey;
	int rowKey = 1000000;
	
	public abstract String getSeparator();
	protected static String getNextString(String in, String sep) {
		return getNextString(in,sep,0);
	}
	protected static String getNextString(String in, String sep, int start) {
		int pos = -1;
		if (in.startsWith("\"")) {
			int qpos = in.indexOf("\"", (start==0) ? 1 : start);
			int qqpos = in.indexOf("\"\"", (start==0) ? 1 : start);
			if ((qpos==qqpos)&&(qqpos >= 0)) {
				return getNextString(in,sep,qqpos+2);
			}
			if (qpos == in.length()) {
				return in;
			}
			if (qpos == -1) {
				qpos = 0;
			}
			pos = in.indexOf(sep,qpos+1);
		} else {
			pos = in.indexOf(sep, 0);
		}
		if (pos == -1) return in;
		return in.substring(0,pos);
	}
	
	public static Vector<String> parseLine(String line, String sep){
		String pline = line;
		Vector<String> cols = new Vector<String>();
		while(pline!=null){
			pline = pline.trim();
			String tpline = getNextString(pline,sep);
			cols.add(normalize(tpline));
			if (pline.length() == tpline.length()) break;
			pline = pline.substring(tpline.length()+1);
		}
		return cols;
	}
	
	public static Vector<Vector<String>> parseFile(File f, String sep) throws IOException{
		return parseFile(f,sep,false);
	}
	public static Vector<Vector<String>> parseFile(File f, String sep,boolean skipFirstLine) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		if (skipFirstLine) br.readLine();
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		for(String line=br.readLine(); line!=null; line=br.readLine()){
			rows.add(parseLine(line, sep));
		}
		br.close();
		return rows;
	}

	public ActionResult importFile(File selectedFile) throws IOException {
		Timer timer = new Timer();
		forceKey = dt.getImporterForceKey();
		BufferedReader br = new BufferedReader(new FileReader(selectedFile));
		int colcount = 0;
		TreeMap<String,Stats> types = new TreeMap<String,Stats>();
		for(String line=br.readLine(); line!=null; line=br.readLine()){
			Vector<String> cols = parseLine(line, getSeparator());
			colcount = Math.max(colcount,cols.size());
			String key = cols.get(0);
			if (forceKey) {
				key = "" + (rowKey++);
			} 
			Stats stats = new Stats(key);
			if (forceKey) {
				stats.vals.add(cols.get(0));
			}
			for(int i=1; i<cols.size(); i++){
				stats.vals.add(cols.get(i));
			}
			types.put(key, stats);
		}
		Object[][] details = (forceKey) ?  new Object[colcount+1][5] : new Object[colcount][5];
		if (forceKey) {
			details[0][0] = String.class;
			details[0][1] = "Col0";
			details[0][2] = 100;
			details[0][4] = false;
			for(int i=0; i<colcount; i++) {
				details[i+1][0] = String.class;
				details[i+1][1] = "Col" + (i+1);
				details[i+1][2] = 100;
				details[i+1][4] = true;
			}

		} else {
			for(int i=0; i<colcount; i++) {
				details[i][0] = String.class;
				details[i][1] = "Col" + i;
				details[i][2] = 100;
				details[i][4] = true;
			}			
		}
		return new ActionResult(selectedFile, selectedFile.getName(), this.toString(), details, types, true, timer.getDuration());
	}
	
	protected static String normalize(String val) {
		val = val.trim();
		if (val.startsWith("\"")) {
			val = val.substring(1);
		}
		if (val.endsWith("\"")) {
			val = val.substring(0,val.length()-1);
		}
		if (val.endsWith("'")) {
			val = val.substring(0,val.length()-1);
		}
		return val;
	}
	public boolean allowForceKey() {
		return true;
	}
}
