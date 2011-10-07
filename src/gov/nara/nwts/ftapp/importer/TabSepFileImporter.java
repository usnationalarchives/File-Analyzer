package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for tab delimited files
 * @author TBrady
 *
 */
public class TabSepFileImporter extends DelimitedFileImporter {

	public TabSepFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return "\t";
	}
	public String toString() {
		return "Import Tab-Separated File";
	}
	public String getDescription() {
		return "This rule will import a tab separated file and use the first column as a unique key";
	}
	public String getShortName() {
		return "TAB";
	}

}
