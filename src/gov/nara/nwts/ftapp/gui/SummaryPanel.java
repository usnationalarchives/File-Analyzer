package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.Stats;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * User interface component that will be generated at the completion of each File Analyzer action
 * @author TBrady
 *
 */
class SummaryPanel extends MyBorderPanel {
	private static final long serialVersionUID = 1L;
	JPanel tp;
	JPanel filterPanel;
	StatsTable st;
	JTextField note;
	JTextField fnote;
	DirectoryTable parent;
	SummaryPanel(DirectoryTable dt) {
		parent = dt;
		tp = addBorderPanel("Summary Counts");
		JPanel p = addPanel("", BorderLayout.SOUTH);
		note = new JTextField();
		note.setEditable(false);
		note.setBorder(BorderFactory.createEmptyBorder());
		fnote = new JTextField(45);
		fnote.setEditable(false);
		fnote.setBorder(BorderFactory.createEmptyBorder());
		p.add(note);
		p.add(fnote);
		JButton save = new JButton("Export Table");
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new TableSaver(parent,st.tm,st.jt,"Stats",st.noExport);
				}
			}
		);
		p.add(save);
		JPanel np = addPanel("",BorderLayout.NORTH);
		np.setLayout(new BorderLayout());
		p = new JPanel();
		np.add(p, BorderLayout.NORTH);
		String note = parent.criteriaPanel.actions.getSelectedItem().toString() +": "+parent.criteriaPanel.rootLabel.getText();
		JTextField jtf = new JTextField(note,30);
		parent.detailsPanel.jtfRoot.setText(jtf.getText());
		jtf.setEditable(false);
		p.add(jtf);
		JButton b = new JButton("Remove Tab");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				parent.tabs.remove(SummaryPanel.this);
				parent.checkTabs();
			}});
		p.add(b);
		filterPanel = new JPanel();
		np.add(filterPanel, BorderLayout.SOUTH);
	}
	
	
	void showStats(Object[][] details,TreeMap<String,Stats> types) {
		st = new StatsTable(details,types, parent);
		tp.removeAll();
		tp.add(new JScrollPane(st.jt), BorderLayout.CENTER);
		filterPanel.removeAll();
		for(Iterator<JComboBox>i=st.filters.iterator();i.hasNext();) {
			JComboBox cb = i.next();
			if (cb!=null) filterPanel.add(cb);
		}
	}
	
	public void setFilterNote(int x, int y) {
		fnote.setText("["+FTDriver.nf.format(x)+ " of " + FTDriver.nf.format(y) + " showing]");
	}
}

