package gov.nara.nwts.ftapp.filter;

/**
 * Filter for AV files
 * @author TBrady
 *
 */
public class AVFileTestFilter extends DefaultFileTestFilter {

	public String getSuffix() {
		return ".*(\\.wav|\\.mov|\\.mp3|\\.avi|\\.dpx|\\.mxf)$";
	}
	public boolean isReSuffix() {
		return true;
	}
    public String getName(){return "AV Files";}

}
