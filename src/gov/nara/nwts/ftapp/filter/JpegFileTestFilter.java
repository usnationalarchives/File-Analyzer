package gov.nara.nwts.ftapp.filter;

/**
 * Filter for JPG files
 * @author TBrady
 *
 */
public class JpegFileTestFilter extends DefaultFileTestFilter {

	public String getSuffix() {
		return ".jpg";
	}
	public String getPrefix() {
		return "^[^\\.].*";
	}
	public boolean isRePrefix() {
		return true;
	}
    public String getName(){return "Jpg";}

}
