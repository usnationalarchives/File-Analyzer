package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for semicolon delimited files
 * @author TBrady
 *
 */
public class SemicolonFileImporter extends DelimitedFileImporter {

	public SemicolonFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return ";";
	}

	public String toString() {
		return "Import Semicolon-Separated File";
	}
	public String getDescription() {
		return "This rule will import a semicolon separated file and use the first column as a unique key";
	}
	public String getShortName() {
		return "SEMI";
	}

}
