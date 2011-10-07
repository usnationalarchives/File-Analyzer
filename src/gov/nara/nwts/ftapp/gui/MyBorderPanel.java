package gov.nara.nwts.ftapp.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * GUI helper class to add a panel with a border layout 
 * @author TBrady
 *
 */
class MyBorderPanel extends MyPanel {
	private static final long serialVersionUID = 1L;
	MyBorderPanel() {
		super(new BorderLayout());			
	}
	JPanel addPanel(JPanel p, String loc) {
		add(p, (loc != null) ? loc : BorderLayout.CENTER);
		return p;
	}
	
}
