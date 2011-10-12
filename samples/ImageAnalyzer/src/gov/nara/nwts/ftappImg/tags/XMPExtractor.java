package gov.nara.nwts.ftappImg.tags;

import java.util.ArrayList;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.properties.XMPProperty;
import com.adobe.xmp.properties.XMPPropertyInfo;

/**
 * Extractor for accessing data from XMP within a TIF or a JPG
 * @author TBrady
 *
 */
public class XMPExtractor {
	XMPMeta xmp;
	public static final String[] XMP_DESC = {"http://purl.org/dc/elements/1.1/","dc:description[1]"};
	public static final String[] XMP_INSTR = {"http://ns.adobe.com/photoshop/1.0/","photoshop:Instructions"};
	public static final String[] XMP_KEY = {"http://purl.org/dc/elements/1.1/","dc:subject[1]"};
	public static final String[] XMP_ICC = {"http://ns.adobe.com/photoshop/1.0/","photoshop:ICCProfile"};
	
	public XMPExtractor(XMPMeta xmp) {
		this.xmp = xmp;
	}
	public XMPExtractor() {
	}

	public String getXMP(String[] xmppath) {
		try {
			if (xmp==null) return "";
			//System.err.println(xmp.dumpObject());
			XMPProperty xp = xmp.getProperty(xmppath[0], xmppath[1]);
			if (xp==null) return "";
			return xp.toString();
		} catch (XMPException e) {
			e.printStackTrace();
			return "";
		}
	}
	public ArrayList<Object> getTags() {
		ArrayList<Object> list = new ArrayList<Object>();
		if (xmp != null){
			try {
				for(XMPIterator xi = xmp.iterator();xi.hasNext();) {
					XMPPropertyInfo o = (XMPPropertyInfo)xi.next();
					list.add(o);
				}
			} catch (XMPException e) {
				e.printStackTrace();
			}
			
		}
		return list;
	}

}
