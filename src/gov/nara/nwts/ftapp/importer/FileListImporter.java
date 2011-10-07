package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.ActionResult;
import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import gov.nara.nwts.ftapp.Timer;
import java.util.TreeMap;
/**
 * This routine will import a list of paths from a file making the filename of each into a key.
 * On the 1940 Census project, several QC applications were written in which file paths were written to a text file in order to be imported into a thumbnail viewer.  
 * This routine allowed those files to be used as input for other comparison processes.  
 * This rule may or may not be useful for the future.
 * @author TBrady
 *
 */
public class FileListImporter extends DefaultImporter {
	public static Object[][] details = {
		{String.class,"Filename Import",200},
	};

	public FileListImporter(FTDriver dt) {
		super(dt);
	}

	public ActionResult importFile(File selectedFile) throws IOException {
		Timer timer = new Timer();
		TreeMap<String,Stats> types = new TreeMap<String,Stats>();
		FileReader fr = new FileReader(selectedFile);
		BufferedReader br = new BufferedReader(fr);
		for(String line=br.readLine(); line!=null; line=br.readLine()){
			File f=new File(line);
			String s = f.getName();
			Stats stats = new Stats(s);
			types.put(s,stats);
		}			
		return new ActionResult(selectedFile, selectedFile.getName(), this.toString(), details, types, true, timer.getDuration());
	}

	
	public String toString() {
		return "File List Importer"; 
	}
	public String getDescription() {
		return "This rule is used to import a list of file paths from a file.  The file name will be made the key for each record.";
	}
	public String getShortName() {
		return "LIST";
	}

}
