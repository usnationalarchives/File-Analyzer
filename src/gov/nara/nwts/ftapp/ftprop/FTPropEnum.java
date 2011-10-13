package gov.nara.nwts.ftapp.ftprop;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import gov.nara.nwts.ftapp.filetest.FileTest;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * File Test Property object presenting enumerated values as a choice
 * @author TBrady
 *
 */

public class FTPropEnum extends DefaultFTProp {
	JComboBox combo;

	public FTPropEnum(FileTest ft, String name, String shortname, String description, Object[]vals, Object def) {
		super(ft, name, shortname, description, def);
		init(vals);
		combo = new JComboBox();
		initCombo(vals);
		combo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				Object obj = combo.getSelectedItem();
				if (obj == null) return;
				if (FTPropEnum.this.ft.getFTDriver().hasPreferences()){
					FTPropEnum.this.ft.getFTDriver().getPreferences().put(getPrefString(), combo.getSelectedItem().toString());				
				}
			}
		});
	}

	public void initCombo(Object[] vals) {
		for(Object obj: vals) {
			combo.addItem(obj);
		}
		setValue(def);
	}
	
	public JComponent getEditor() {
		return combo;
	}

	public Object getValue() {
		return combo.getSelectedItem();
	}

	public Object validate(String s) {
		for(int i=0; i<combo.getItemCount(); i++){
			Object obj = combo.getItemAt(i);
			if (obj == null) continue;
			if (obj.toString().equals(s)) {
				return obj;
			}
		}
		return combo.getSelectedItem();
	}
	public void setValue(Object obj) {
		combo.setSelectedItem(obj);
	}
	public String describeFormatted() {
		StringBuffer sb = new StringBuffer("\t\t\t");
		sb.append(description);
		sb.append("\n\n");
		for(int i=0; i<combo.getItemCount();i++) {
			sb.append("\t\t\t\t");
			sb.append(combo.getItemAt(i).toString());
			sb.append("\n");
		}
		return sb.toString();
	}


}
