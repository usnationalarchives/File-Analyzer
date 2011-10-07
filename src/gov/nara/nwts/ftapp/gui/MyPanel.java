package gov.nara.nwts.ftapp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * GUI helper class allowing new components to be quickly added to the File Analyzer in a consistent fashion
 * @author TBrady
 *
 */
class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Box main;
	MyPanel(LayoutManager layout) {
		super(layout);
		main = new Box(BoxLayout.Y_AXIS);
		add(main);
	}

	MyPanel() {
		this(new FlowLayout());
	}

	
	JPanel addPanel() {
		return addPanel((String)null);
	}
	
	JPanel addPanel(String title) {
		return addPanel(title, null);
	}
	JPanel addPanel(String title, String loc) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if (title != null) {
			p.setBorder(BorderFactory.createTitledBorder(title));
		}
		return addPanel(p, loc);
	}
	
	JPanel addBorderPanel(String title) {
		JPanel p = new JPanel(new BorderLayout());
		if (title != null) {
			p.setBorder(BorderFactory.createTitledBorder(title));
		}
		return addPanel(p, (String)null);
	}
	
	JPanel addPanel(JPanel p, String loc) {
		if (loc == null) {
			main.add(p);
		} else {
			main.add(p, loc);
		}
		return p;
	}
}
