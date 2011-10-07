package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.importer.Importer;
import gov.nara.nwts.ftapp.stats.Stats;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * User interface component presenting file import options and auto-sequencing options.
 * @author TBrady
 *
 */
class ImportPanel extends MyPanel {
	private static final long serialVersionUID = 1L;
	JTextField prefix;
	JTextField suffix;
	JFormattedTextField start;
	JFormattedTextField end;
	JComboBox pad;
	JComboBox importers;
	JTextArea importerDesc;
	JCheckBox forceKey;
	DirectoryTable parent;
	FileSelectChooser fsc;
	
	void updateDesc() {
		Importer i = (Importer)importers.getSelectedItem();
		importerDesc.setText(i.getDescription());
		forceKey.setEnabled(i.allowForceKey());
	}
	
	ImportPanel(DirectoryTable dt) {
		parent = dt;
		JPanel p = addBorderPanel("Contents to Import for Analysis");
		JPanel tp = new JPanel(new BorderLayout());
		p.add(tp, BorderLayout.NORTH);
		fsc = new FileSelectChooser(parent.frame, "Select file to Import", parent.preferences, "IMPORT", "");
		fsc.setBorder(BorderFactory.createTitledBorder("File to Import"));
		tp.add(fsc, BorderLayout.NORTH);
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(p1, BorderLayout.SOUTH);
		forceKey = new JCheckBox("Force Unique Keys");
		p1.add(forceKey);
		JButton jb = new JButton("Import File");
		jb.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					File f = new File(fsc.tf.getText());
					if (f.exists()) {
						Importer imp = (Importer)parent.importPanel.importers.getSelectedItem();
						try {
							parent.importFile(imp, f);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(parent.frame, e.getMessage()+": "+f.getAbsolutePath());
						}
					}
				}
			}
		);
		p1.add(jb);
		p1 = new JPanel();
		p1.add(new JLabel("File Import Action"));
		importers = new JComboBox(parent.importerRegistry);
		importers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				updateDesc();
			}
		});
		p1.add(importers);
		tp.add(p1, BorderLayout.SOUTH);
		importerDesc = new JTextArea(5,60);
		importerDesc.setLineWrap(true);
		importerDesc.setWrapStyleWord(true);
		importerDesc.setBackground(this.getBackground());
		importerDesc.setEditable(false);
		p.add(importerDesc);
		updateDesc();
		
		p = addPanel("Auto-Number");
		p.setLayout(new GridLayout(0,2));
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
		p.add(new JLabel("Prefix"));
		prefix = new JTextField(25);
		p.add(prefix);
		p.add(new JLabel("Start Number"));
		start = new JFormattedTextField(nf);
		start.setColumns(8);
		p.add(start);
		p.add(new JLabel("End Number"));
		end = new JFormattedTextField(nf);
		end.setColumns(8);
		p.add(end);
		p.add(new JLabel());
		Object[] objs = {"No Padding",2,3,4,5,6,7,8};
		pad = new JComboBox(objs);
		p.add(pad);
		p.add(new JLabel("Suffix"));
		suffix = new JTextField(25);
		p.add(suffix);
		p.add(new JLabel(""));
		jb = new JButton("Auto-Generate Keys for Analysis");
		p.add(jb);
		jb.addActionListener(new ActionListener(){
			Object[][] details = {
				{String.class,"Key",100},
				{Integer.class,"Count",10}
			};
			public void actionPerformed(ActionEvent arg0) {
				TreeMap<String,Stats> types = new TreeMap<String,Stats>();
				Long startval = (Long)start.getValue();
				Long endval = (Long)end.getValue();
				
				if ((startval == null)||(endval==null)) return;
				
				for(long i=startval.intValue(); i<=endval.intValue(); i++){
					NumberFormat nf = NumberFormat.getIntegerInstance();
					nf.setGroupingUsed(false);
					if (pad.getSelectedItem() instanceof Integer) {
					  nf.setMinimumIntegerDigits((Integer)pad.getSelectedItem());
					}
					String key = prefix.getText() + nf.format(i) + suffix.getText();
					Stats stats = new Stats(key);
					stats.vals.add(1);
					types.put(key, stats);
				}
				parent.showSummary("Generated "+(++parent.summaryCount), details, types, true);
			}
		});
	}
}
