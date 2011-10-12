package gov.nara.nwts.ftappImg.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filetest.ActionRegistry;

/** 
 * Initialize the File Analzyer with generic image processing rules (but not NARA specific business rules)
 * @author TBrady
 *
 */
public class ImageActionRegistry extends ActionRegistry {
	
	private static final long serialVersionUID = 1L;

	public ImageActionRegistry(FTDriver dt, boolean modifyAllowed) {
		super(dt, modifyAllowed);
		//add(new CountTiff(dt));
		//add(new CountJpeg(dt));
		add(new GenericImageProperties(dt));
	}
	
}
