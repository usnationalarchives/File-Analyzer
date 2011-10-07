package gov.nara.nwts.ftapp.filter;

/**
 * Filter for TIF files
 * @author TBrady
 *
 */
public class TiffFileTestFilter extends DefaultFileTestFilter {

	public String getSuffix() {
		return ".tif";
	}
	public String getPrefix() {
		return "^[^\\.].*";
	}
	public boolean isRePrefix() {
		return true;
	}
    public String getName(){return "Tiffs";}

}
