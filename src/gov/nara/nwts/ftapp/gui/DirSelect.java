package gov.nara.nwts.ftapp.gui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Displays a directory selection dialog; results will be saved to a specified text field.
 * @author TBrady
 *
 */
class DirSelect extends FileSelect {
	private static final long serialVersionUID = 1L;

	public void configureChooser() {
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);		
	}
	DirSelect(JFrame parent, JTextField result, String title) {
		super(parent, result, title);
	}
}
