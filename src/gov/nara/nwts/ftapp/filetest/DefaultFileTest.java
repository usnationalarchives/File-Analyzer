package gov.nara.nwts.ftapp.filetest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filter.AVFileTestFilter;
import gov.nara.nwts.ftapp.filter.DefaultFileTestFilter;
import gov.nara.nwts.ftapp.filter.FileTestFilter;
import gov.nara.nwts.ftapp.filter.ImageFileTestFilter;
import gov.nara.nwts.ftapp.filter.JpegFileTestFilter;
import gov.nara.nwts.ftapp.filter.TiffFileTestFilter;
import gov.nara.nwts.ftapp.ftprop.FTProp;
import gov.nara.nwts.ftapp.stats.Stats;
import gov.nara.nwts.ftapp.stats.CountStats;

/**
 * Abstract implementation of the FileTest interface; FileTest objects will derive these behaviors unless explicitly overridden.
 * @author TBrady
 *
 */
public abstract class DefaultFileTest implements FileTest {
	protected FTDriver dt;
	protected ArrayList<FileTestFilter>filters;
	protected ArrayList<FTProp>ftprops;
	
	public File getRoot() {
		return dt.getRoot();
	}
	
	public DefaultFileTest(FTDriver dt) {
		this.dt = dt;
		filters = new ArrayList<FileTestFilter>();
		ftprops = new ArrayList<FTProp>();
		initFilters();
	}

	public void initFilters() {
		filters.add(new DefaultFileTestFilter());
	}
	
	public FileTest resetOption() {
		return null;
	}
	public void initAllFilters() {
		filters.add(new DefaultFileTestFilter());
		filters.add(new AVFileTestFilter());
		filters.add(new ImageFileTestFilter());
		filters.add(new TiffFileTestFilter());
		filters.add(new JpegFileTestFilter());
	}

    public FileTestFilter getDefaultFilter() {
    	if (filters.size() > 0)
    		return filters.get(0);
    	return null;
    }

	public List<FileTestFilter> getFilters() {
		return filters;
	}
	
	public String getExt(File f) {
		String ext = "";
		StringTokenizer st = new StringTokenizer(f.getName(), ".");
		while(st.hasMoreElements()) {
			ext = st.nextElement().toString().toUpperCase();
		}	
		return ext;
	}
	
	public String getKey(File f) {
		return getExt(f);
	}
	public String getKey(File f, Object o) {
		return getKey(f);
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
	public Stats getStats(File f) {
		return getStats(getKey(f));
	}

	public Stats getStats(String key) {
		Stats mystats = dt.types.get(key);
		if (mystats == null) {
			mystats = createStats(key);
		}
		dt.types.put(key, mystats);
		
		return mystats;
	}

	public boolean isTestable(File f) {
		return true;
	}

    public Stats createStats(String key){
    	return new CountStats(key);
    }
    public Object[][] getStatsDetails() {
    	return CountStats.details;
    }

    public boolean isTestDirectory() {
    	return false;
    }
    public boolean processRoot() {
    	return false;
    }
    public boolean isTestFiles() {
    	return true;
    }
    public Pattern getDirectoryPattern() {
    	return null;
    }
    public void init() {
    }
    public void refineResults() {
    }

    public void progress(int count) {
    }

    public void showCount(int count) {
   		if (count>0) System.out.println("Progress Count: "+count+" files processed.");
    }

    /* Some tasks such as checksum validation had errors due to too many open file handles although the files were out of scope.  
     * Explicitly calling GC to force cleanup.*/
    public void cleanup(int count) {
    	if (count>0) System.out.println("Progress Count: "+count+" files processed. Start Cleanup.");
		System.gc();
		System.runFinalization();
		if (count>0) System.out.println("Cleanup complete.");
		System.out.flush();
    }
	public List<FTProp> getPropertyList() {
		return ftprops;
	}
	
	public FTDriver getFTDriver() {
		return dt;
	}
	
	public Object getProperty(String name) {
		return getProperty(name, null);
	}
	public Object getProperty(String name, Object def) {
		for(FTProp ftprop: ftprops) {
			if (ftprop.getName().equals(name)) {
				return ftprop.getValue();
			}
		}
		return def;
	}
	public void setProperty(String name, String s) {
		for(FTProp ftprop: ftprops) {
			if (ftprop.getName().equals(name)) {
				ftprop.setValue(ftprop.validate(s));
				return;
			}
		}
	}
	
}
