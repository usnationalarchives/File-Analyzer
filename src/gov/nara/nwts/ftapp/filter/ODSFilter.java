package gov.nara.nwts.ftapp.filter;

/**
 * Filter for Open Office Spreadsheet files
 * @author TBrady
 *
 */
public class ODSFilter extends DefaultFileTestFilter {
	public String getSuffix() { 
		return ".ods";
	}
    public String getName(){return "Open Office Spreadsheet";}

    public String getShortName(){return "ODS";}
}
