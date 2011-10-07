package gov.nara.nwts.ftapp.filter;

/**
 * Default filter which accepts all files
 * @author TBrady
 *
 */
public class DefaultFileTestFilter implements FileTestFilter {

	public String getName() {
		return "All Files";
	}
	public String getContains() {
		return "";
	}
	public String getPrefix() {
		return "";
	}

	public String getExclusion() {
		return "";
	}

	public String getSuffix() {
		return "";
	}
	public boolean isRePrefix() {
		return false;
	}
	public boolean isReContains() {
		return false;
	}
	public boolean isReSuffix() {
		return false; 
	}
	public boolean isReExclusion() {
		return false; 
	}

	public String getShortName() {
		return getName();
	}
	
	public String getShortNameNormalized() {
		return getShortName().replaceAll("[\\s&]","");
	}
	public String getShortNameFormatted() {
		StringBuffer buf = new StringBuffer();
		buf.append(getShortNameNormalized());
		buf.append("                     ");
		return buf.substring(0,20);
	}
}
