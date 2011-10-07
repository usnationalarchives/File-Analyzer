package gov.nara.nwts.ftapp.gui;

import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * UI component allowing the selction of the root directory for a File Test.
 * Note: this pre-dates the creation of the {@link FileSelectChooser} and {@link DirSelectChooser} classes but accomplishes a similar function
 * @author TBrady
 *
 */
class FileCatalog extends JFrame {
	private static final long serialVersionUID = 1L;
	
	DirectoryTable dt;
	FileCatalog(DirectoryTable dt) {
		super("File Analyzer: Set Input Directory");
		this.dt = dt;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel p = new JPanel(new BorderLayout());
		add(p);
		JFileChooser jfc = new JFileChooser() {
			private static final long serialVersionUID = 1L;
			public void cancelSelection() {
				FileCatalog.this.dispose();
			}
			public void approveSelection() {
				FileCatalog.this.setVisible(false);
				FileCatalog.this.dt.criteriaPanel.rootLabel.setText(getSelectedFile().getAbsolutePath());
				FileCatalog.this.dt.setSelectedFile();
				FileCatalog.this.dispose();
			}
		};
		if (dt.root != null){
			jfc.setCurrentDirectory(dt.root);
		}
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		p.add(jfc, BorderLayout.CENTER);
		p.add(new JLabel("Please select the directory that you wish to catalog"),BorderLayout.NORTH);
		pack();
		setVisible(true);
	}
	
}
