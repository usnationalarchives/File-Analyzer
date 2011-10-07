package gov.nara.nwts.ftapp.ftprop;

import javax.swing.JComponent;

/**
 * Contract that a File Test Property must satisfy in both the GUI and Command Line form of the File Analyzer
 * @author TBrady
 *
 */
public interface FTProp {
	public String getName();
	public String describe();
	public JComponent getEditor();
	public Object getDefault();
	public Object validate(String s);
	public Object getValue();
	public void setValue(Object obj);

    public String getShortName();
    public String getShortNameFormatted();
    public String getShortNameNormalized();
    public String describeFormatted();
}
