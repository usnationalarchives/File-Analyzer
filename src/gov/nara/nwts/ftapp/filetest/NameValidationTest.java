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
