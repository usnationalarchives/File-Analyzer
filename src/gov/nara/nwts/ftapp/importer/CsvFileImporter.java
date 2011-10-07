package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for comma delimited files
 * @author TBrady
 *
 */
public class CsvFileImporter extends DelimitedFileImporter {

	public CsvFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return ",";
	}

	public String toString() {
		return "Import Comma-Separated File";
	}
	public String getDescription() {
		return "This rule will import a comma separated file and use the first column as a unique key";
	}
	public String getShortName() {
		return "CSV";
	}
}
