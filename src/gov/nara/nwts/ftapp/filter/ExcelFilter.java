package gov.nara.nwts.ftapp.filter;

/**
 * Filter for excel files
 * @author TBrady
 *
 */
public class ExcelFilter extends DefaultFileTestFilter {
	public String getSuffix() { 
		return ".xls";
	}
    public String getName(){return "Excel";}

}
