package gov.nara.nwts.ftapp.filter;

/**
 * Contract that a File Test Filter must support
 * @author TBrady
 *
 */
public interface FileTestFilter {
	public String getName();

    public String getPrefix();
    public String getContains();
    public String getSuffix();
    public String getExclusion();

    public boolean isRePrefix();
    public boolean isReContains();
    public boolean isReSuffix();
    public boolean isReExclusion();

    public String getShortName();
    public String getShortNameFormatted();
    public String getShortNameNormalized();

}
