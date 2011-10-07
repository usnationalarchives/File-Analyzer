package gov.nara.nwts.ftapp.filter;

/**
 * Filter for TIF or JPG files
 * @author TBrady
 *
 */
public class TiffJpegFileTestFilter extends DefaultFileTestFilter {

	public String getSuffix() {
		return ".*\\.(tif|jpg)$";
	}
	public boolean isReSuffix() {
		return true;
	}
    public String getName(){return "Tif Jpeg";}

}
