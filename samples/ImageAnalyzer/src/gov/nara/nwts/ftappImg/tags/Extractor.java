package gov.nara.nwts.ftappImg.tags;

import gov.nara.nwts.ftappImg.tags.ImageTags.DUP;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;

/**
 * Contract for default behavior for an image metadata extractor
 * @author TBrady
 *
 */
public interface Extractor {
	public void close();
	public String getString(DUP tags);
	public String getString(DUP tags, boolean b);
	public String getString(TAGS tags);
}
