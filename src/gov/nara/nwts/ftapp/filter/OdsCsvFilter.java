package gov.nara.nwts.ftapp.filter;

/**
 * Filter for open office spreadsheet files OR Comma separated files
 * @author TBrady
 *
 */
public class OdsCsvFilter extends DefaultFileTestFilter {

	public String getSuffix() {
		return ".*\\.(ods|csv)$";
	}
	public boolean isReSuffix() {
		return true;
	}
    public String getName(){return "ODS/CSV";}

}
