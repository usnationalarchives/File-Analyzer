package gov.nara.nwts.ftapp;

import gov.nara.nwts.ftapp.importer.Importer;
import gov.nara.nwts.ftapp.importer.ImporterRegistry;

import java.io.File;
import java.io.IOException;

/**
 * Driver for the command line version of the File Analyzer (for performing file imports)
 * This application was originally created by Terry Brady in NARA's Digitization Services Branch.
 * @author TBrady
 *
 */
public class BatchImporter {
	FTDriver dt;
	File infile;
	File outdir;
	String outfile;
	Importer impsel;
	ImporterRegistry ar;
	boolean overwrite = true;
	
	public ImporterRegistry getImporterRegistry(FTDriver dt) {
		return new ImporterRegistry(dt);
	}
	public BatchImporter() {
		dt = new FTDriver(infile);
		ar = getImporterRegistry(dt);
		outdir = new File(System.getProperty("user.dir"));
		outfile = "";
	}
	
	public String getArg(String[] args, int i) {
		if (i >= args.length) reportError("Missing argument for "+args[args.length-1]);
		String s = args[i];
		if ((s.length() > 2) && (s.startsWith("\"")) && (s.endsWith("\""))) {
			return s.substring(1,s.length()-2);
		}
		return s;
	}
	
	public void parse(String[] args) {
		String ftname = null;
		for(int i=0; i<args.length; i++) {
			String s = args[i];
			if (s.equalsIgnoreCase("-help")) {
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-overwrite")) {
				overwrite = !getArg(args,++i).equals("false");
			} else if (s.equalsIgnoreCase("-infile")) {
				infile = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-outdir")) {
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-outfile")) {
				outfile = getArg(args,++i);
			} else if (s.startsWith("-")) {
				reportError("Invalid Option: "+s);
			} else if (ftname == null){
				ftname = s;
				for(Importer imp: ar) {
					if (ftname.equalsIgnoreCase(imp.getShortNameNormalized())){
						impsel = imp;
					}
				}
				if (impsel == null) {
					reportError("Invalid File Test name: "+s);
				}
			} else {
				reportError("Unexpected argument: "+s);
			}
		}
		
		if (infile == null) {
			reportError("No Input File Specified");
		}
		if (impsel == null) {
			reportError("No File Test Specified");
		}
		
	}
	
	public void reportError(String s) {
		System.err.println("*ERROR:"+s);
		System.err.println("Pass -help for command line usage");
		System.exit(10);		
	}
	
	public boolean run() {
		dt.overwrite = overwrite;
		dt.saveDir = outdir;
		dt.saveFile = outfile;
		try {
			dt.importFile(impsel, infile);
		} catch (IOException e) {
			reportError(e.getMessage());
			return false;
		}
		return true;
	}

	public void reportUsage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("\tBatchImporter [-options] -infile inputfile importer");
		System.out.println("");
		System.out.println("where options include");
		System.out.println("\t-outdir   \tDirectory to which output files will be written.\n\t\tDefaults to working directory");
		System.out.println("\t-outfile  \tOutput file name, defaults to a system generated name");
		System.out.println("\t-max      \tdefaults to 500000");
		System.out.println("\t-overwrite\tdefaults to true");
		System.out.println("");
		System.out.println("importer");
		for(Importer imp: ar) {
			System.out.println("\t\t"+imp.getShortNameFormatted()+"\t"+imp.toString());
		}
	}
	public void report() {
		System.out.println("Input File:      \t" +infile.getAbsolutePath());
		System.out.println("Output Directory:\t" +outdir.getAbsolutePath());
		System.out.println("Output File:     \t" +outfile);
		System.out.println("Importer:        \t" + impsel.getShortNameNormalized()+": "+impsel.toString());
		System.out.println("Overwrite:       \t" + overwrite);
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchImporter ba = new BatchImporter();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("Completion:      \t" + b);
		System.out.flush();
	}

}
