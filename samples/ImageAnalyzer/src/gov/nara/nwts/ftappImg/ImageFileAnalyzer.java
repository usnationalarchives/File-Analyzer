package gov.nara.nwts.ftappImg;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.ActionRegistry;
import gov.nara.nwts.ftapp.gui.DirectoryTable;
import gov.nara.nwts.ftapp.importer.ImporterRegistry;
import gov.nara.nwts.ftappImg.filetest.ImageActionRegistry;
/**
 * Driver for the File Analyzer GUI loading image-specific rules but not NARA specific rules.
 * @author TBrady
 *
 */
public class ImageFileAnalyzer extends DirectoryTable {

	public ImageFileAnalyzer(File f, boolean modifyAllowed) {
		super(f, modifyAllowed);
	}
	
	protected ActionRegistry getActionRegistry() {
		return new ImageActionRegistry(this, modifyAllowed);
	}

	protected ImporterRegistry getImporterRegistry() {
		return new ImporterRegistry(this);
	}
	public static void main(String[] args) {
		if (args.length > 0)
			new ImageFileAnalyzer(new File(args[0]), false);		
		else
			new ImageFileAnalyzer(null, false);		
	}

}
