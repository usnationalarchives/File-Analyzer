package gov.nara.nwts.ftapp.filetest;

import java.io.File;
import java.util.ArrayList;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filetest.DefaultFileTest;
import gov.nara.nwts.ftapp.nameValidation.CustomPattern;
import gov.nara.nwts.ftapp.nameValidation.NameValidationPattern;
import gov.nara.nwts.ftapp.nameValidation.RenameDetails;
import gov.nara.nwts.ftapp.nameValidation.RenameStatus;
import gov.nara.nwts.ftapp.nameValidation.ValidPattern;
import gov.nara.nwts.ftapp.stats.NameValidationStats;
import gov.nara.nwts.ftapp.stats.Stats;
/**
 * Abstract class defining the core functionality of a filename validation rule.
 * 
 * <h3>Sample Usage applying a complex set of patterns:</h3>
 * <pre>
 * class CustomFilenameTest extends NameValidationTest {
 * public static String fold = "(\\d\\d\\d\\d[A-Z]?)";
 * public static String fold2 = "\\\\" + fold + "\\\\";
 * public static String box = "\\d\\d\\d\\d?[A-Z]?";
 * public static String box2 = "^.*\\\\Box(" + box + ")";
 * public static String box3 = "^(.*)\\\\Box(\\d\\d\\d\\d?[A-Za-z]?)";
 * public static String suff = "(001|002)_(MA|AC).tif$";
 * public static String seqsuff = "(\\d\\d\\d)_" + suff;
 * 
 * Pattern dirPatternFolder;
 * Pattern dirPatternImage;
 * Pattern dirPatternIgnore;
 * Pattern dirPatternIgnoreR;
 * 
 * public CustomFilenameTest(FTDriver dt, FileTest next) {
 * 		super(dt, new ValidPattern(box2 + fold2 + "Box\\1_" + seqsuff, true),
 * 			next, "Forest Service Filename", "RG95 ");
 * 		seqs = new TreeMap<String, Integer>();
 * 		dirseqs = new TreeMap<File, Integer>();
 * 
 * 		dirPatternFolder = Pattern.compile("^(\\d\\d\\d\\d)[A-Z]?$");
 * 		dirPatternImage = Pattern.compile("^Box"+box+"_" + seqsuff);
 * 		dirPatternIgnore = Pattern.compile("^(\\..*|Thumbs.db)$");
 * 		dirPatternIgnoreR = Pattern
 * 				.compile("^(\\..*|Thumbs.db|\\d\\d\\d\\d[A-Z]?)$");
 * 
 * 		testPatterns.add(new InvalidManualPattern(box2 + fold2
 * 				+ "Box(\\d\\d\\d[A-Z]?)_" + seqsuff, true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "Box number mismatch in filename: [" + m.group(1) + "/"
 * 						+ m.group(3) + "]";
 * 			}
 * 		});
 * 		testPatterns.add(new InvalidManualPattern("^.*\\\\(Box" + box
 * 				+ "[^\\\\]+)" + fold2 + "Box(" + box + ")" + seqsuff, true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "Box folder name is invalid: [" + m.group(1) + "]";
 * 			}
 * 		});
 * 		testPatterns.add(new InvalidManualPattern("(" + box3 + "\\\\"
 * 				+ "\\d{6,6}_.*_(\\d\\d\\d))_(MA|AC).tif$", true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "No FOLDER folder present";
 * 			}
 * 		});
 * 		testPatterns.add(new RenameablePattern("^.*\\\\\\s*(Box" + box
 * 				+ ")[\\-_](\\d\\d\\d)[\\-_](001|002)[\\-_]*(MA|AC)\\s*.tif$",
 * 				true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "Separators replaced";
 * 			} 
 * 
 * 			public File getNewFile(File f, Matcher m) {
 * 				String newname = m.group(1) + "_" + m.group(2) + "_"
 * 						+ m.group(3) + "_" + m.group(4) + ".tif";
 * 				return new File(f.getParentFile(), newname);
 * 			}
 * 		});
 * 		testPatterns.add(new RenameablePattern(box2 + fold2
 * 				+ "(Box\\1)_\\d{5,5}_" + seqsuff, true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "Subject code removed";
 * 			} 
 * 
 * 			public File getNewFile(File f, Matcher m) {
 * 				String newname = m.group(3) + "_" + m.group(4) + "_"
 * 						+ m.group(5) + "_" + m.group(6) + ".tif";
 * 				return new File(f.getParentFile(), newname);
 * 			}
 * 		});
 * 		testPatterns.add(new RenameablePattern(box3 + fold2 + "Box\\d{1,3}_"
 * 				+ seqsuff, true) {
 * 			public String getMessage(File f, Matcher m) {
 * 				return "Extraneous identifiers removed";
 * 			}
 * 
 * 			public File getNewFile(File f, Matcher m) {
 * 				String mbox = "Box" + m.group(2).toUpperCase();
 * 				String newname = m.group(1) + "\\" + mbox + "\\" + m.group(3)
 * 						+ "\\" + mbox + "_" + m.group(4) + "_" + m.group(5)
 * 						+ "_" + m.group(6) + ".tif";
 * 				return new File(CustomFilenameTest.this.dt.root, newname);
 * 			}
 * 		});
 * 		testPatterns.add(new RenameablePattern("("+box3 + fold2 + "\\d{6,6}[a-zA-Z]?_.*_)(\\d\\d\\d)_(MA|AC).tif$", true) {
 * 			String message = "Sequence number generated";
 * 			public String getMessage(File f, Matcher m) {
 * 				return message;
 * 			}
 * 
 * 			
 * 			public File getNewFile(File f, Matcher m) {
 * 			   ... custom logic to introspect the file system and determine the next sequence number to assign.
 * 			}
 * 		});
 * 
 * 
 * 		dirTestPatterns.add(new CustomPattern("^Box\\d\\d\\d[A-Z]?$", false) {
 * 			public RenameDetails report(File f, Matcher m) {
 * 				RenameDetails det = DirAnalysis.analyze(f, dirPatternFolder, 1,
 * 						dirPatternIgnore, false);
 * 				if (det.status == RenameStatus.DIRECTORY_VALID) {
 * 					det = DirAnalysis.recursiveAnalyze(f, dirPatternImage, 1,
 * 							dirPatternIgnoreR, false);
 * 				}
 * 				return det;
 * 			}
 * 		});
 * 		dirTestPatterns.add(new CustomPattern(dirPatternFolder, false) {
 * 			public RenameDetails report(File f, Matcher m) {
 * 				return DirAnalysis.analyze(f, dirPatternImage, 1,
 * 						dirPatternIgnore, false);
 * 			}
 * 		});
 * 	}
 * </pre>
 * @author TBrady
 *
 */
abstract public class NameValidationTest extends DefaultFileTest {

	int numRename = 0;
	String name;
	String shortname;
	protected ArrayList<NameValidationPattern> testPatterns;
	protected ArrayList<CustomPattern> dirTestPatterns;
	boolean renameParent = false;
	ValidPattern valPatt;
	FileTest nextTest;
	protected boolean allowRename = false;

	public NameValidationTest(FTDriver dt, ValidPattern valPatt, FileTest nextTest, String name, String shortname) {
		super(dt);
		this.nextTest = nextTest;
		this.allowRename = (nextTest!=null);
		this.name = name;
		this.shortname = shortname;
		this.valPatt = valPatt;
		testPatterns = new ArrayList<NameValidationPattern>();
		testPatterns.add(valPatt);
		dirTestPatterns = new ArrayList<CustomPattern>();
	}

	public FileTest resetOption() {
		return nextTest;
	}
    
	public String toString() {
		return name +  (allowRename ? " Rename" : " Test");
	}

	public Object dirTest(File f) {
		return new RenameDetails(RenameStatus.DIRECTORY, null, "");
	}
	
	public Object fileTest(File f) {
		if (f.isDirectory()) {
			for(NameValidationPattern nvp: dirTestPatterns) {
				RenameDetails det = nvp.checkFile(f);
				if (det.status == RenameStatus.NEXT) continue;
				return det;
			}
			return new RenameDetails(RenameStatus.DIRECTORY, null, null);
		}
		for(NameValidationPattern nvp: testPatterns) {
			RenameDetails det = nvp.checkFile(f);
			if (det.status == RenameStatus.NEXT) continue;
			File newFile = det.getFile();
			if (newFile != null) {
				if (valPatt.checkFile(newFile).status != RenameStatus.VALID) {
					det.status = RenameStatus.NEW_NAME_INVALID;
					return det;
				}
				if (f.equals(newFile)) {
				} else if (newFile.exists()) {
					det.status = RenameStatus.RENAME_FILE_EXISTS;
					return det;
				}
				if (allowRename) {
					return renameFile(f, det);
				}
			}
			return det;
		}
		return new RenameDetails(RenameStatus.PARSE_ERR, null, "");
	}

	public String getKey(File f) {
		String path = f.getAbsolutePath();
		return path;
	}

	public Stats createStats(String key) {
		return new NameValidationStats(key);
	}

	public Object[][] getStatsDetails() {
		return NameValidationStats.details;
	}

	public String getShortName() {
		return shortname + (allowRename ? " Rename" : "");
	}

	public String getDescription() {
		return "This test will check filename case."
				+ ((allowRename) ? "\nFILES WILL BE RENAMED IF POSSIBLE." : "");
	}

    public boolean isTestFiles() {
    	return true;
    }
    public boolean isTestDirectory() {
    	return true;
    }

	public RenameDetails renameFile(File f, RenameDetails det) {
		File newFile = det.getFile();
		if (!newFile.getParentFile().exists()) {
			if (!newFile.getParentFile().mkdirs()) {
				det.status = RenameStatus.RENAME_FAILURE;
				return det;
			}			
		}
		File pf = f.getParentFile();
		if (!f.renameTo(newFile)) {
			det.status = RenameStatus.RENAME_FAILURE;	
			return det;
		}
		if (renameParent) {
			if (pf.list().length == 0) {
				File gpf = pf.getParentFile();
				File newpf = new File(gpf, "__" + pf.getName());
				if (!pf.renameTo(newpf)){
					System.err.println("Could not rename" + pf.getAbsolutePath());
				}
			}
		}
		det.status = RenameStatus.RENAMED;
		return det;
		
	}
}
