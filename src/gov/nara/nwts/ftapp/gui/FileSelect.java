package gov.nara.nwts.ftapp.gui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Displays a File selection dialog; results will be saved to a specified text field.
 * @author TBrady
 *
 */
class FileSelect extends JDialog {
	private static final long serialVersionUID = 1L;
	JTextField result;
	JFileChooser jfc;
	

	public void configureChooser() {
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);		
	}
	
	FileSelect(JFrame parent, JTextField result, String title) {
		super(parent, title);
		this.setModal(true);
		this.result = result;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel p = new JPanel(new BorderLayout());
		add(p);
		jfc = new JFileChooser() {
			private static final long serialVersionUID = 1L;

			public void cancelSelection() {
				FileSelect.this.dispose();
			}

			public void approveSelection() {
				FileSelect.this.setVisible(false);
				FileSelect.this.result.setText(this.getSelectedFile()
						.getAbsolutePath());
				FileSelect.this.dispose();
			}
		};
		String root = FileSelect.this.result.getText();
		if (root != null) {
			jfc.setCurrentDirectory(new File(root));
		}
		p.add(jfc, BorderLayout.CENTER);
		p.add(new JLabel(title), BorderLayout.NORTH);
		configureChooser();
		pack();
		setVisible(true);
	}
}
