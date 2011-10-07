package gov.nara.nwts.ftapp.filetest;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.Stats;
import gov.nara.nwts.ftapp.filter.FileTestFilter;
import gov.nara.nwts.ftapp.ftprop.FTProp;

/**
 * Contract defining the behavior of a File Analyzer custom rule.
 * @author TBrady
 *
 */
public interface FileTest {
	public String toString();
	public String getDescription();
	public String getExt(File f);
	public String getKey(File f);
	public String getKey(File f, Object o);
	public boolean isTestable(File f);
    public Object fileTest(File f);
    public Stats getStats(File f);
    public Stats getStats(String key);

    public List<FileTestFilter> getFilters();
    
    public Stats createStats(String key);
    public Object[][] getStatsDetails();
    public String getShortName();
    public String getShortNameFormatted();
    public String getShortNameNormalized();
    
    public FileTestFilter getDefaultFilter();
    
    public File getRoot();
    
    void initFilters();
    
    public boolean isTestFiles();
    public boolean isTestDirectory();
    public boolean processRoot();
    public Pattern getDirectoryPattern();
    
    public void refineResults();
    public void init();

    public void progress(int count);
	public FileTest resetOption();
	public List<FTProp> getPropertyList();
	public FTDriver getFTDriver();
	public Object getProperty(String name);
	public void setProperty(String name, String str);
}
