package gov.nara.nwts.ftapp.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/** 
 * Helper class allowing the contents of a table to be saved to a tab-separated text file; the user will be allowed to choose the actual columns to be exported.
 * @author TBrady
 *
 */
class TableSaver extends JDialog {
	private static final long serialVersionUID = 1L;
	DefaultTableModel tm;
	JTable jt;
	JPanel colPanel;
	ArrayList<JCheckBox> checks;

	DirectoryTable parent;
    TableSaver(DirectoryTable parent, DefaultTableModel tm, JTable jt, String fname) {
    	this(parent, tm, jt, fname, new ArrayList<String>());
    }
    TableSaver(DirectoryTable parent, DefaultTableModel tm, JTable jt, String fname, List<String>noExport) {
    	super(parent.frame, "Export Table: "+fname);
    	this.tm = tm;
    	this.parent = parent;
    	this.jt = jt;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	setModal(true);
    	setLayout(new BorderLayout());
    	JFileChooser jfc = new JFileChooser(){
    		private static final long serialVersionUID = 1L;
    		public String getApproveButtonText() {
    			return "Export";
    		}
			public void cancelSelection() {
				TableSaver.this.dispose();
			}
			public void approveSelection() {
				File f = getSelectedFile();
				if (f.exists()) {
					if (JOptionPane.showConfirmDialog(this, "File Exists.  Do you wish to replace it?","File Exists", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
						return;
					}
				}
				try {
					save(getSelectedFile());
					TableSaver.this.parent.preferences.put("importexport",getSelectedFile().getParentFile().getAbsolutePath());
					//throw new IOException("testing...");
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				TableSaver.this.dispose();
			}    		
    	};
    	String defpath = parent.preferences.get("importexport", "");
    	jfc.setSelectedFile(new File(fname +".txt"));    		
    	if (!defpath.equals("")){
    		File p = new File(defpath);
    		if (p.exists()) {
    			jfc.setSelectedFile(new File(p,fname +".txt"));
    		}
    	}
    	add(jfc, BorderLayout.NORTH);
    	colPanel = new JPanel(new GridLayout(0,1));
    	add(new JScrollPane(colPanel), BorderLayout.CENTER);
    	checks = new ArrayList<JCheckBox>();
   		for(int c=0; c<tm.getColumnCount(); c++){
   			Object chead = jt.getColumnModel().getColumn(c).getHeaderValue();
   			String cname = (chead == null) ? "" : chead.toString();
   			JCheckBox cb = new JCheckBox(cname, !noExport.contains(cname));
   			colPanel.add(cb);
   			checks.add(cb);
   		}
    	pack();
    	setVisible(true);
    }
    
    public void save(File f) throws IOException {
    	BufferedWriter bw = new BufferedWriter(new FileWriter(f));
    	boolean first = true;
		for(int c=0; c<tm.getColumnCount(); c++){
			if (checks.get(c).isSelected()){
				if (first) {
					first = false;
				} else {
					bw.write("\t");				
				}
				bw.write('"');
				if (jt.getColumnModel().getColumn(c).getHeaderValue() != null){
	  			  bw.write(jt.getColumnModel().getColumn(c).getHeaderValue().toString());    				
				}
				bw.write('"');
			}
		}
		bw.write("\r\n");
		RowSorter<? extends TableModel> rs = jt.getRowSorter();
    	for(int r=0; r<tm.getRowCount(); r++) {
    		if (rs!=null) {
        		if (rs.convertRowIndexToView(r) == -1) 
        			continue;    			
    		}
    		first = true;
    		for(int c=0; c<tm.getColumnCount(); c++){
    			if (checks.get(c).isSelected()){
    				if (first) {
    					first = false;
    				} else {
    					bw.write("\t");				
    				}
    				bw.write('"');
    				if (tm.getValueAt(r, c) != null){
    					bw.write(tm.getValueAt(r, c).toString());    				
    				}
    				bw.write('"');
    			}
    		}
			bw.write("\r\n");
    	}
    	bw.flush();
    	bw.close();
    }
}
