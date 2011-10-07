package gov.nara.nwts.ftapp.filter;

/**
 * Filter for XML files
 * @author TBrady
 *
 */
public class XmlFilter extends DefaultFileTestFilter {
	public String getSuffix() { 
		return ".xml";
	}
    public String getName(){return "XML";}

}
