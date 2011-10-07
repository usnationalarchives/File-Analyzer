package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.ActionResult;

import java.io.File;
import java.io.IOException;

/**
 * Contract that Importers will fullfill.
 * @author TBrady
 *
 */
public interface Importer {
	public ActionResult importFile(File selectedFile) throws IOException;
	public String getDescription();
	public boolean allowForceKey();
    public String getShortName();
    public String getShortNameFormatted();
    public String getShortNameNormalized();
}
