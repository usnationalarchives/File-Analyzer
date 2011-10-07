package gov.nara.nwts.ftapp.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Widget that will display a chosen file and will open a separate window allowing a file to be selected.
 * @author TBrady
 *
 */
class FileSelectChooser extends JPanel implements ActionListener, DocumentListener {
	private static final long serialVersionUID = 1L;
	private Preferences p;
	protected JFrame parent; 
	public JTextField tf;
	private JButton button;
	protected String title;
	private String key;
	
	FileSelectChooser(JFrame parent, String title, String def) {
		this(parent, title, null, null, def);
	}
	FileSelectChooser(JFrame parent, String title, Preferences p, String key, String def) {
		this.p = p;
		this.parent = parent;
		this.title = title;
		this.key = key;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		String val = (p == null) ? def : p.get(key, def);
		tf = new JTextField(val, 40);
		tf.setEditable(false);
		add(tf);
		button = new JButton("...");
		add(button);
		button.addActionListener(this);
		tf.getDocument().addDocumentListener(this);
	}
	
	public void change() {
		if (p != null) {
			p.put(key, tf.getText());
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		new FileSelect(parent, tf, title);
	}
	public void changedUpdate(DocumentEvent arg0) {
		change();
	}
	public void insertUpdate(DocumentEvent arg0) {
		change();
	}
	public void removeUpdate(DocumentEvent arg0) {
		change();
	}


}
