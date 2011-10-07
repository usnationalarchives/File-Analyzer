package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.ActionResult;
import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.Timer;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class parser to ananlyze and ingest individual rows from a file using a regular expression pattern.
 * @author TBrady
 *
 */
public class Parser extends DefaultImporter {
	public enum status {PASS,FAIL}
	public static Object[][] details = {
		{String.class,"Row",60},
		{status.class,"Pass/Fail",100,status.values()},
		{String.class,"Data",300},
	};
	Pattern p;
	int cols;
	Object[][]mydetails;
	
	public Object[][] getDetails() {
		return details;
	}
	public Pattern getPattern() {
		return Pattern.compile("^(.*)$");
	}

	public Parser(FTDriver dt) {
		super(dt);
		mydetails = getDetails();
		cols = mydetails.length - 1;
		p = getPattern();
	}
	
	public Matcher test(String line) {
		return p.matcher(line);
	}

	public Object getVal(Matcher m, int i) {
		if (m.groupCount()>= i) {
			return m.group(i).trim();
		}
		return "";
	}

	public Object getDefVal(Matcher m, int i, String line) {
		if (m.groupCount()== i) {
			return line;
		}
		return "";
	}

	public void setVals(Matcher m, Stats stats, String line) {
		if (m.matches()) {
			stats.vals.add(status.PASS);
			for(int i=1;i<=cols;i++) {
				stats.vals.add(getVal(m,i));
			}
		} else {
			stats.vals.add(status.FAIL);
			for(int i=1;i<=cols;i++) {
				stats.vals.add(getDefVal(m,i,line));
			}
		}
		
	}

	public ActionResult importFile(File selectedFile) throws IOException {
		Timer timer = new Timer();
		TreeMap<String,Stats> types = new TreeMap<String,Stats>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
			int i=1000000;
			for(String line=br.readLine(); line!=null; line=br.readLine()){
				String key = ""+ (i++);
				Stats stats = new Stats(key);
				types.put(key, stats);
				Matcher m = test(line);
				setVals(m, stats, line);
			}
			br.close();
			return new ActionResult(selectedFile, selectedFile.getName(), this.toString(), mydetails, types, true, timer.getDuration());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ActionResult(selectedFile, selectedFile.getName(), this.toString(), mydetails, types, false, timer.getDuration());
	}

	public String toString() {
		return "Parser";
	}
	public String getDescription() {
		return "This rule will parse each line of a file and add it to the results table.";
	}
	public String getShortName() {
		return "Parse";
	}

}
