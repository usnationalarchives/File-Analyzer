package gov.nara.nwts.ftapp.filter;

/**
 * Filter for text files
 * @author TBrady
 *
 */
public class TxtFilter extends DefaultFileTestFilter {
	public String getSuffix() { 
		return ".txt";
	}
    public String getName(){return "TXT";}

}
