package gov.nara.nwts.ftapp;

import gov.nara.nwts.ftapp.filetest.ActionRegistry;
import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.filter.FileTestFilter;
import gov.nara.nwts.ftapp.ftprop.FTProp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Driver for the command line version of the File Analyzer (for performing File Tests)
 * This application was originally created by Terry Brady in NARA's Digitization Services Branch.
 * @author TBrady
 *
 */
public class BatchAnalyzer {
	FTDriver dt;
	File root;
	File batch;
	File outdir;
	String outfile;
	HashMap<String,String>params;
	
	FileTest ft;
	FileTestFilter ftf;
	String filtername;
	int max = 500000;
	ActionRegistry ar;
	boolean overwrite = true;
	boolean listfilters = false;
	boolean listparams = false;
	
	public ActionRegistry getActionRegistry(FTDriver dt) {
		return new ActionRegistry(dt, false);
	}
	public BatchAnalyzer() {
		root = new File(System.getProperty("user.dir"));
		dt = new FTDriver(root);
		ar = getActionRegistry(dt);
		outdir = new File(System.getProperty("user.dir"));
		outfile = "";
		params = new HashMap<String,String>();
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
		root = null;
		for(int i=0; i<args.length; i++) {
			String s = args[i];
			if (s.equalsIgnoreCase("-help")) {
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-listparams")) {
				listparams = true;
			} else if (s.equalsIgnoreCase("-listfilters")) {
				listfilters = true;
			} else if (s.equalsIgnoreCase("-listtests")) {
				listTests();
				System.exit(0);

			} else if (s.equalsIgnoreCase("-overwrite")) {
				overwrite = !getArg(args,++i).equals("false");

			} else if (s.equalsIgnoreCase("-root")) {
				root = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-filter")) {
				filtername = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-batchfile")) {
				batch = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-outdir")) {
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-outfile")) {
				outfile = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-prefix")) {
				dt.prefix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-suffix")) {
				dt.suffix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-contains")) {
				dt.contains = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-excludes")) {
				dt.excludes = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-rf")) {
				String colstr = getArg(args,++i);
				String val = getArg(args,++i);
				try {
					int col = Integer.parseInt(colstr);
					if (col < 2) {
						reportError("Column number must be greater than 1");
					}
					dt.myresfilter.add(col-2, val);
				} catch (NumberFormatException e) {
					reportError("Column must be a number -rf <col> <val>");
				}

			} else if (s.equalsIgnoreCase("-param")) {
				String pname = getArg(args,++i);
				String pval = getArg(args,++i);
				params.put(pname, pval);

			} else if (s.equalsIgnoreCase("-max")) {
				try {
					max = Integer.parseInt(getArg(args,++i));
				} catch (NumberFormatException e) {
					reportError("Max value must be a number");
				}
			} else if (s.startsWith("-")) {
				reportError("Invalid Option: "+s);
			} else if (ftname == null){
				ftname = s;
				for(FileTest ftest: ar) {
					if (ftname.equalsIgnoreCase(ftest.getShortNameNormalized())){
						ft = ftest;
					}
				}
				if (ft == null) {
					reportError("Invalid File Test name: "+s);
				}
			} else {
				reportError("Unexpected argument: "+s);
			}
		}
		
		if ((batch != null) && ((root !=null) || !outfile.equals(""))){
			reportError("When specifying -batchfile, neither a root dir nor an output file should be specified.");			
		}
		
		if (root == null) {
			root = new File(System.getProperty("user.dir"));
		}
		
		if (ft == null) {
			reportError("No File Test Specified");
		}
		
		if (listfilters) {
			reportFilters(ft);
			System.exit(0);
		}
		
		if (listparams) {
			reportParams(ft);
			System.exit(0);
		}
		if (filtername !=null) {
			for(FileTestFilter fil: ft.getFilters()){
				if (fil.getShortNameNormalized().equalsIgnoreCase(filtername)) {
					ftf = fil;
				}
			}
			if (ftf == null) {
				reportFilters(ft);
				reportError("Invalid Filter Name for File Test: "+ft);
			} else {
				dt.myfilter = ftf;
			}
		}
		for(String param: params.keySet()) {
			ft.setProperty(param, params.get(param));
		}
	}
	
	public void reportFilters(FileTest ft) {
		System.out.println("Filters for File Test: "+ft.toString());
		for(FileTestFilter fil: ft.getFilters()){
			System.out.println("\t"+fil.getShortNameFormatted()+"\t"+fil.getName());
		}
		System.out.flush();		
	}
	
	public void reportParams(FileTest ft) {
		System.out.println("Parameters for File Test: "+ft.toString());
		for(FTProp ftprop: ft.getPropertyList()){
			System.out.println("\t"+ftprop.getShortNameFormatted()+"\t"+ftprop.getName());
			System.out.println(ftprop.describeFormatted());
		}
		System.out.flush();		
	}
	
	public void reportError(String s) {
		System.err.println("*ERROR:"+s);
		System.err.println("Pass -help for command line usage");
		System.exit(10);		
	}
	
	public boolean run() {
		dt.overwrite = overwrite;
		dt.root = root;
		dt.saveDir = outdir;
		dt.saveFile = outfile;
		dt.fileTraversal.setTraversal(ft, max);		
		if (batch!=null){
			try {
				dt.loadBatch(batch);
				dt.batchStart();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return dt.fileTraversal.traverseFile();					
		}
	}

	public void reportUsage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("\tBatchAnalyzer [-options] [-root rootdir] [-outfile outfile] filetest");
		System.out.println("\t\tto process a single directory, writing results to one file");
		System.out.println("\t\tIf absent, rootdir will default to current directory.\n\t\tIf absent, outfile will be generated");
		System.out.println("or");
		System.out.println("\tBatchAnalyer [-options] -batchfile batch filetest");
		System.out.println("\t\tto process a collection of directories ");
		System.out.println("\t\tThe batch file is a tab separated file containing:");
		System.out.println("\t\t\trootdir --> result filenames");
		System.out.println("");
		System.out.println("where options include");
		System.out.println("\t-outdir <dir>  \tDirectory to which output files will be written.\n\t\t\t\tDefaults to working directory");
		System.out.println("\t-max <num>     \tdefaults to 500000");
		System.out.println("\t-overwrite     \tdefaults to true");
		System.out.println("\t-listparams    \tlists the parameters associated with a file test");
		System.out.println("\t-listfilters   \tlists the filters associated with a file test");
		System.out.println("\t-filter <name> \tname of the filter to use.\n\t\t\t\tdefaults to the first filter");
		System.out.println("");
		System.out.println("filetest parameters");
		System.out.println("\t-param <name> <val>\tPass filetest specific parameters");
		System.out.println("\t               \tMultiple param vals may be provided");
		System.out.println("");
		System.out.println("the following options override filter defaults");
		System.out.println("\t-prefix <val>  \tFilenames must start with this value");
		System.out.println("\t-suffix <val>  \tFilenames must end with this value");
		System.out.println("\t-contains <val>\tFilenames must contain this value");
		System.out.println("\t-excludes <val>\tFilenames may not contain this value");
		System.out.println("");
		System.out.println("result filtering options");
		System.out.println("\t-rf <col> <val>\tOnly output results where column <col> matches <val>");
		System.out.println("\t               \tMultiple rf vals may be provided");
		listTests();
	}
	
	public void listTests() {
		System.out.println("filetest");
		for(FileTest ftest: ar) {
			System.out.println("\t"+ftest.getShortNameFormatted()+"\t"+ftest.toString());
		}
		System.out.flush();
	}
	
	public void report() {
		System.out.println("Root Directory:  \t" +root.getAbsolutePath());
		System.out.println("Output Directory:\t" +outdir.getAbsolutePath());
		System.out.println("Output File:     \t" +outfile);
		System.out.println("FileTest:        \t" + ft.getShortNameNormalized()+": "+ft.toString());
		System.out.println("Max Files:       \t" + max);
		System.out.println("Overwrite:       \t" + overwrite);
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchAnalyzer ba = new BatchAnalyzer();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("Completion:      \t" + b);
		System.out.flush();
	}

}
