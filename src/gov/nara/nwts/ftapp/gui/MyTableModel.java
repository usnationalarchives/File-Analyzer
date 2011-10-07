package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.FTDriver;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Table model for the Details Tab
 * @author TBrady
 *
 */
public class MyTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	public MyTableModel() {
	}

	public int getColumnCount() {
		return 6;
	}

	public Class<?> getColumnClass(int col) {
		if (col == 4) {
			return Date.class;
		} else if (col == 3) {
			return Long.class;
		} else if (col == 5) {
			return Object.class;
		} else {
			return String.class;
		}
	}

	public void setColumns(JTable jt) {
		JTableHeader jth = jt.getTableHeader();
		jth.setReorderingAllowed(true);
		TableColumnModel tcm = jt.getColumnModel();
		TableColumn tc;
		Object[][] details = {
			{"Directory",300},
			{"Filename",200},
			{"Type",50},
			{"Size",100},
			{"Mod Date",100},
			{"Other",500}
		};
		for(int i=0; i<details.length; i++){
			tc = tcm.getColumn(i);
			tc.setPreferredWidth((Integer)details[i][1]);
			tc.setHeaderValue(details[i][0]);
		}
		TableCellRenderer tcr = new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
			public void setValue(Object value){
				if (value == null) return;
				if (value instanceof Long){
					long v = (Long)value;
					setText(FTDriver.nf.format(v));
					setHorizontalAlignment(JLabel.RIGHT);
				} else if (value instanceof Integer){
					int v = (Integer)value;
					setText(FTDriver.nf.format(v));
					setHorizontalAlignment(JLabel.RIGHT);
				} else {
					setText(value.toString());
					setHorizontalAlignment(JLabel.LEFT);
				}
			}
		};

		
		tc = tcm.getColumn(3);
		tc.setCellRenderer(tcr);
		tc = tcm.getColumn(5);
		tc.setCellRenderer(tcr);
	}
	
};
