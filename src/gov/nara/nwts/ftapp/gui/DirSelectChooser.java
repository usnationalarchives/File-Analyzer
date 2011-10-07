package gov.nara.nwts.ftapp.gui;

import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

/**
 * Widget that will display a chosen directory and will open a separate window allowing a directory to be selected.
 * @author TBrady
 *
 */
class DirSelectChooser extends FileSelectChooser {
	private static final long serialVersionUID = 1L;
	
	DirSelectChooser(JFrame parent, String title, String def) {
		this(parent, title, null, null, def);
	}
	DirSelectChooser(JFrame parent, String title, Preferences p, String key, String def) {
		super(parent, title, p, key, def);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		new DirSelect(parent, tf, title);
	}


}
