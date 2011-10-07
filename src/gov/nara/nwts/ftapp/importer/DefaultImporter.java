package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.ActionResult;
import gov.nara.nwts.ftapp.FTDriver;

import java.io.File;
import java.io.IOException;

/**
 * Abstract base class for importer behaviors.
 * @author TBrady
 *
 */
public abstract class DefaultImporter implements Importer {
	protected FTDriver dt;
	public DefaultImporter(FTDriver dt) {
		this.dt = dt;
	}
	
	public abstract String toString();
	public abstract ActionResult importFile(File selectedFile) throws IOException;
	public boolean allowForceKey() {
		return false;
	}
	public String getShortNameNormalized() {
		return getShortName().replaceAll("[\\s&]","");
	}
	public String getShortNameFormatted() {
		StringBuffer buf = new StringBuffer();
		buf.append(getShortNameNormalized());
		buf.append("                     ");
		return buf.substring(0,20);
	}
}
