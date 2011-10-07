package gov.nara.nwts.ftapp.filter;

/**
 * Filter for comma-separated text files
 * @author TBrady
 *
 */
public class CSVFilter extends DefaultFileTestFilter {
	public String getSuffix() { 
		return ".csv";
	}
    public String getName(){return "CSV";}

}
