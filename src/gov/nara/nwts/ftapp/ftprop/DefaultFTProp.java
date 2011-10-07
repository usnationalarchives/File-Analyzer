package gov.nara.nwts.ftapp.ftprop;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Abstract base class for File Test Properties
 * @author TBrady
 *
 */

public abstract class DefaultFTProp implements FTProp {
	String name;
	String shortname;
	String description;
	Object def;
	FileTest ft;
	
	public enum RUNMODE {
		TEST,
		PROD;
	}

	public DefaultFTProp(FileTest ft, String name, String shortname, String description, Object def) {
		this.name = name;
		this.shortname = shortname;
		this.description = description;
		this.ft = ft;
		this.def =  def;
	}
	
	public void init() {
		if (ft.getFTDriver().hasPreferences()) {
			def = ft.getFTDriver().getPreferences().get(getPrefString(), def.toString());
		}
	}
	public void init(Object[] vals) {
		if (ft.getFTDriver().hasPreferences()) {
			String s = ft.getFTDriver().getPreferences().get(getPrefString(), def.toString());
			if (s == null) return;
			for(Object obj: vals) {
				if (s.equals(obj.toString())) {
					def = obj;
					return;
				}
			}
		}
	}
	
	public String getPrefString() {
		return ft.toString()+"--"+name;
	}

	public String describe() {
		return description;
	}
	public String describeFormatted() {
		return "\t\t\t"+description;
	}

	public Object getDefault() {
		return def;
	}

	public String getName() {
		return name;
	}
	public String getShortName() {
		return shortname;
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
