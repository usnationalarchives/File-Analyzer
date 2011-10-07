package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.gui.MergePanel.FilterOptions.FilterOptionTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * User interface allowing the results of 2 or more FileAnalyzer actions (File Test or File Import) to be compared
 * The merge options that are presented are dynamically adjusted based on the number of {@link gov.nara.nwts.ftapp.gui.SummaryPanel Summary Result Panels} that are being displayed.  At least 2 summary panels must be present in order for the merge options to appear.
 * @author TBrady
 *
 */
class MergePanel extends MyBorderPanel {
	public static final String STR_IGNORE = "*Ignore*";
	public static final String STR_YES = "\"Yes\"";
	private static final long serialVersionUID = 1L;
	JPanel tp;
	DefaultTableModel tm;
	JTable jt;
	Vector<JComboBox> mergeOptions;
	JComboBox filterOptions;
	Box box;
	JButton b;
	TableRowSorter<TableModel> sorter;
	DirectoryTable parent;
	MergePanel(DirectoryTable dt) {
		parent = dt;
		mergeOptions = new Vector<JComboBox>();
		String[] cols = {"Key","Run 1", "Run 2"};
		tm = new DefaultTableModel(cols,5);
		jt = new MergeTable(tm);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
		jt.setPreferredScrollableViewportSize(new Dimension(600,200));
		sorter = new TableRowSorter<TableModel>(tm);
		jt.setRowSorter(sorter);
		filterOptions = new JComboBox();
		tp = addBorderPanel("Merged Counts");
		tp.add(new JScrollPane(jt), BorderLayout.CENTER);
		JPanel p = addPanel("", BorderLayout.SOUTH);
		JButton save = new JButton("Export Table");
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new TableSaver(parent,tm,jt,"Stats");
				}
			}
		);
		p.add(save);
		p = addPanel("Specify Merge Options", BorderLayout.NORTH);
		p.setLayout(new BorderLayout());
		JPanel pp = new JPanel(new BorderLayout());
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.add(pp, BorderLayout.SOUTH);
		p.setPreferredSize(new Dimension(600,250));
		pp.add(p1, BorderLayout.NORTH);
		pp.add(p2, BorderLayout.SOUTH);
		filterOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				filter();
			}
		});
		p2.add(filterOptions);
		b = new JButton("Merge Summaries");     
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				TreeMap<String,Object[]> merge = new TreeMap<String,Object[]>();
				int compcount = 1;
				int col = 0;
				for(int i=DirectoryTable.TAB_MERGE + 1; i < parent.tabs.getTabCount(); i++){
					JComboBox opt = MergePanel.this.mergeOptions.get(i-DirectoryTable.TAB_MERGE-1);						
					if (opt.getSelectedItem().equals(STR_IGNORE)) continue;
					compcount++;
				}
				String[] colnames = new String[compcount];
				colnames[0] = "Key";
				for(int i=DirectoryTable.TAB_MERGE + 1; i < parent.tabs.getTabCount(); i++){
					JComboBox opt = MergePanel.this.mergeOptions.get(i-DirectoryTable.TAB_MERGE-1);
					if (opt.getSelectedItem().equals(STR_IGNORE)) continue;
					col++;
					colnames[col] = parent.tabs.getTitleAt(i);
					DefaultTableModel tm = ((SummaryPanel)parent.tabs.getComponentAt(i)).st.tm;
					for(int r=0; r<tm.getRowCount(); r++){
						String key = (String)tm.getValueAt(r, 0);
						Object[] v = merge.get(key);
						if (v == null) {
							v = new Object[compcount];
							v[0] = key;
							for(int x=1;x<v.length; x++){
								v[x]="";
							}
							merge.put(key, v);
						}
						if (opt.getSelectedItem().equals(STR_YES)) {
							v[col] = "YES";
						} else {
							v[col] = tm.getValueAt(r, opt.getSelectedIndex()-1);
						}
					}
				}
				tm.setColumnIdentifiers(colnames);
				tm.setRowCount(0);
				filterOptions.removeAllItems();
				for(FilterOptions fo: FilterOptions.values()) {
					fo.addOptions(filterOptions, tm);
				}
				
				DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
				tcr.setBackground(Color.PINK);
				for(Iterator<String>i=merge.keySet().iterator(); i.hasNext();){
					String key = i.next();
					Object[] v = merge.get(key);
					tm.addRow(v);
				}
				jt.repaint();
			}});
		p1.add(b);
		parent.tabs.addContainerListener(new ContainerListener(){
			public void componentAdded(ContainerEvent arg0) {
				refreshOptions();
			}
			public void componentRemoved(ContainerEvent arg0) {
				refreshOptions();
			}});
		p1 = new JPanel();
		p.add(new JScrollPane(p1), BorderLayout.CENTER);
		box = new Box(BoxLayout.Y_AXIS);
		p1.add(box);
	}
	
	void filter() {
		RowFilter<TableModel,Integer> rf = new RowFilter<TableModel,Integer>(){
			public boolean include(Entry<? extends TableModel, ? extends Integer> row) {
				FilterOptionTest fo = (FilterOptionTest)filterOptions.getSelectedItem();
				if (fo == null) {
					return true;
				} else if (fo.eOpt == FilterOptions.All_Items) { 
	 				return true;
	 			} else if (fo.col == -1) {
	 				boolean matches = true;
	 				Object comp = row.getValue(1);
	 				for(int i=2; i<row.getValueCount(); i++){
	 					if (comp == null) {
	 						if (row.getValue(i) != null) {
	 							matches = false;
	 							break;
	 						}
	 					} else if (!comp.equals(row.getValue(i))) {
	 						matches = false;
	 						break;
	 					}
	 				}
	 			    if (fo.eOpt == FilterOptions.Matches) {
	 			    	return matches;
	 			    } else {
	 			    	return !matches;
	 			    }
	 			} else {
	 				Object comp = row.getValue(fo.col);
	 				if (comp==null) {
	 					return fo.eOpt == FilterOptions.ColEmpty;
	 				}
	 				if (comp.equals("")) {
	 					return fo.eOpt == FilterOptions.ColEmpty;		 					
	 				}
 					return fo.eOpt == FilterOptions.ColHasValue;
	 			}
			}
		};
		sorter.setRowFilter(rf);
		
	}
	
	void refreshOptions() {
		mergeOptions.clear();
		box.removeAll();
		for(int i=DirectoryTable.TAB_MERGE + 1; i < parent.tabs.getTabCount(); i++){
			JPanel p = new JPanel();
			box.add(p);
			p.add(new JLabel(parent.tabs.getTitleAt(i)));
			SummaryPanel sp = (SummaryPanel)parent.tabs.getComponentAt(i);
			Vector<String> v = new Vector<String>();
			v.add(STR_IGNORE);
			for(int c=0;c<sp.st.details.length; c++){
				v.add(sp.st.details[c][1].toString());
			}
			v.add(STR_YES);
			JComboBox cb = new JComboBox(v);
			cb.setSelectedIndex((v.size() > 2) ? 2 : (v.size() == 2) ? 1 : 0);
			mergeOptions.add(cb);
			p.add(cb);
			
			
			parent.frame.pack();
			parent.frame.repaint();
		}
	}
	class MergeTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableCellRenderer def;
		DefaultTableCellRenderer diff;
		MergeTable(DefaultTableModel tm) {
			super(tm);
			def = new DefaultTableCellRenderer();
			diff = new DefaultTableCellRenderer();
			diff.setBackground(Color.PINK);
		}

		public TableCellRenderer getCellRenderer(int row, int col){
			if (col <= 1) return def;
			Object o1 = this.getValueAt(row, col);
			Object o2 = this.getValueAt(row, col-1);
			if ((o1 == null) || (o2 == null)) return def;
			
			return (o1.equals(o2)) ? def : diff;
		}
		
	}
	/** 
	 * Options for filtering the results on the merge panel
	 * @author TBrady
	 *
	 */
	public enum FilterOptions {
		All_Items("Show All Items" , true),
		Matches("Show Matches", true),
		Mismatches("Show Mismatches",true),
		ColHasValue("Value Present",false),
		ColEmpty("Value Missing",false);
		
		boolean global;
		String name;
		FilterOptions(String name,boolean global) {
			this.name = name;
			this.global = global;
		}
		
		public void addOptions(JComboBox cb, TableModel tm) {
			if (global) {
				cb.addItem(new FilterOptionTest(this, -1, tm));
			}else {
				for(int i=1; i<tm.getColumnCount(); i++) {
					FilterOptionTest test = new FilterOptionTest(this, i, tm);
					cb.addItem(test);
				}
			}
		}
		
		public class FilterOptionTest {
			FilterOptions eOpt;
			int col;
			String colname;
			
			public FilterOptionTest(FilterOptions eOpt, int col, TableModel tm) {
				this.eOpt = eOpt;
				this.col = col;
				this.colname = tm.getColumnName(col);
			}
			
			public String toString() {
				if (eOpt.global) {
					return eOpt.name;
				} else {
					return eOpt.name +": " + colname;
				}
			}
		}
	};

}

