package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.FTDriver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * User interface component displaying File Test progress; the batch processing options are also visible (if enabled) on this tab.
 * @author TBrady
 *
 */
class ProgressPanel extends MyPanel {
	private static final long serialVersionUID = 1L;
	JTextArea status;
	JProgressBar progress;
	JButton cancel;
	DirectoryTable parent;
	JTable jt;
	DefaultTableModel tm;
	String[] cols = {"Name","File Test","Root","Complete","Num Items","Duration","Output File"};
	FileSelectChooser batch;
	JToggleButton pause;
	JButton doBatch;
	JButton clearBatch;
	JTextField batchCount;
	JPanel batchp;
	
	ProgressPanel(DirectoryTable dt) {
		parent = dt;
		JPanel p = addPanel("Status");
		status = new JTextArea(10,60);
		status.setEditable(false);
		p.add(new JScrollPane(status));
		p = addBorderPanel("File Processing Status");
		progress = new JProgressBar();
		setProgressColor(true);
		progress.setBackground(Color.LIGHT_GRAY);
		p.add(progress, BorderLayout.NORTH);
		p = addPanel();
		cancel = new JButton("Cancel");
		cancel.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if (parent.fileTraversal!=null){
						parent.fileTraversal.cancel(true);
					}
				}
				
			}
		);
		cancel.setEnabled(false);
		p.add(cancel);
		p = addBorderPanel("Test Log");
		tm = new DefaultTableModel(cols,0);
		jt = new JTable(tm) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		for(int i=0; i<jt.getColumnCount(); i++) {
			jt.getColumnModel().getColumn(i).setCellRenderer(
				new DefaultTableCellRenderer(){
					private static final long serialVersionUID = 1L;

					public Component getTableCellRendererComponent(JTable jt, Object val, boolean isSel, boolean hasFocus, int row, int col) {
						Component c = super.getTableCellRendererComponent(jt, val, isSel, hasFocus, row, col);
						if (val != null) {
							if (c instanceof JLabel){
								JLabel jc = (JLabel)c;
								jc.setToolTipText(val.toString());
								if (val instanceof File){
									jc.setText(((File)val).getName());
								}
							}
						}
						return c;
					}
				}
			);
		}
		
		jt.getColumnModel().getColumn(0).setPreferredWidth(100);
		jt.getColumnModel().getColumn(1).setPreferredWidth(150);
		jt.getColumnModel().getColumn(2).setPreferredWidth(150);
		jt.getColumnModel().getColumn(3).setPreferredWidth(40);
		jt.getColumnModel().getColumn(4).setPreferredWidth(50);
		jt.getColumnModel().getColumn(5).setPreferredWidth(100);
		jt.getColumnModel().getColumn(6).setPreferredWidth(200);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jt.setPreferredScrollableViewportSize(new Dimension(500, 200));
		p.add(new JScrollPane(jt), BorderLayout.CENTER);
		jt.repaint();
		batchp = addBorderPanel("Batch Processing");
		batchp.setVisible(false);
		JPanel pp1 = new JPanel();
		JPanel pp2 = new JPanel();
		batchp.add(pp1, BorderLayout.NORTH);
		batchp.add(pp2, BorderLayout.SOUTH);
		batch = new FileSelectChooser(parent.frame, "Choose Batch input file {rootdir->saveFile","");
		pp1.add(batch);
		doBatch = new JButton("Process Batch");
		pp2.add(doBatch);
		doBatch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				File f = new File(batch.tf.getText());
				if (f.exists()) {
					try {
						parent.loadBatch(f);
						parent.batchStart();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(parent.frame, e.getMessage());
					}
				}
				
			}
		});
		pause = new JToggleButton("Pause");
		pause.setEnabled(false);
		pause.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if (pause.isSelected()) {
						parent.pauseBatch();
					} else {
						parent.batchStart();
					}
				}
			}
		);
		pp2.add(pause);
		clearBatch = new JButton("Clear Batch");
		clearBatch.setEnabled(false);
		clearBatch.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					parent.clearBatch();
					pause.setSelected(false);
					parent.logBatchSize();
				}
			}
		);
		pp2.add(clearBatch);
		batchCount = new JTextField(6);
		batchCount.setEditable(false);
		batchCount.setBackground(getBackground());
		pp2.add(batchCount);
	}
	
	void logTest(String name,String action, File root, boolean status, int numitems, double duration, File file){
		Vector<Object>v=new Vector<Object>();
		v.add(name);
		v.add(action);
		v.add(root);
		v.add(status);
		v.add(numitems);
		v.add(FTDriver.ndurf.format(duration)+" secs");
		v.add(file);
		tm.addRow(v);
	}
	
	void setProgressColor(boolean success) {
		progress.setForeground(success ? Color.GREEN : Color.RED);			
	}
}
