package gov.nara.nwts.ftapp;

import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.filter.FileTestFilter;
import gov.nara.nwts.ftapp.importer.DelimitedFileImporter;
import gov.nara.nwts.ftapp.importer.Importer;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 * Base class handling the logic of the File Analyzer.
 * This is used in the command line version of the application.
 * The GUI version of the application overrides several behaviors @linkplain gov.nara.nwts.ftapp.gui.DirectoryTable.
 * @author TBrady
 *
 */
public class FTDriver {
	//The following filters are used in batch mode only
	public String prefix;
	public String suffix;
	public String contains;
	public String excludes;		
	
	public FileTraversal fileTraversal;
	public File saveDir;
	public String saveFile;
	public File lastSavedFile;
	public boolean overwrite = true;
	
	public FileTestFilter myfilter;
	public ResultFilter myresfilter;
	
	public int summaryCount;
	public static TransformerFactory tf;
	public static Transformer tDump;
	static {
		tf = TransformerFactory.newInstance();
	}
	protected boolean pause;
	public boolean isBatchProcessing;
	public Preferences getPreferences() {
		return null;
	}
	public boolean hasPreferences() {
		return getPreferences()!=null;
	}
	
	protected Vector<Vector<String>>batchItems;
	
	public static void dumpNode(Node n) {
		StreamResult sr = new StreamResult(System.err);
		try {
			tDump = tf.newTransformer();
			tDump.setParameter(OutputKeys.INDENT, true);
			tDump.transform(new DOMSource(n), sr);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		System.err.println();
	}
	public File root;
	public TreeMap<String,Stats> types;
	public File getRoot() {return root;}
	
	public FTDriver(File root) {
		batchItems = new Vector<Vector<String>>();
		this.root = root;
		saveDir = new File(System.getProperty("user.dir"));
		saveFile = "test";
		types = new TreeMap<String,Stats>();
		fileTraversal = new FileTraversal(this);
		myresfilter = new ResultFilter();
	}

	
	public void showSummary(String name, Object[][] details, TreeMap<String,Stats>types, boolean completed){
		showSummary(name, details, types, completed, "");
	}
	public void showSummary(String name, Object[][] details, TreeMap<String,Stats>types, boolean completed, String note){
		
	}
	public boolean getImporterForceKey() {
		return true;
	}
	public static NumberFormat nf;
	public static NumberFormat ndurf;
	static DateFormat df;
	
	static {
		nf = NumberFormat.getIntegerInstance();
		ndurf = NumberFormat.getNumberInstance();
		//nf.setGroupingUsed(true);
		df = SimpleDateFormat.getDateInstance();
	}
	public FilenameFilter getFileFilter(FileTest fileTest) {
		FileTestFilter filter = (myfilter==null) ? fileTest.getDefaultFilter() : myfilter;
	    return new MyFilenameFilter(
	    	true,
	    	(prefix == null) ? filter.getPrefix() : prefix,
	    	(prefix == null) ? filter.isRePrefix() : false,
	    	(contains == null) ? filter.getContains() : contains,
	    	(contains == null) ? filter.isReContains() : false,
	    	(suffix == null) ? filter.getSuffix() : suffix,
	    	(suffix == null) ? filter.isReSuffix() : false,
	    	(excludes == null) ? filter.getExclusion() : excludes,
	    	(excludes == null) ? filter.isReExclusion() : false,
	    	fileTest.isTestFiles() 
		); 
	}
	
	public FilenameFilter getDirectoryFilter(FileTest fileTest){
		return new MyDirectoryFilter(
			fileTest.isTestDirectory()
		);    	
	}
	public boolean overwrite() {
		return overwrite;
	}
	
	public boolean isSave() {
		return true;
	}
	public void traversalEnd(ActionResult res) {
		saveResult(res);
		System.out.println("\t\t * "+ndurf.format(res.duration) + " secs");
		System.out.println("\t\t * "+nf.format(fileTraversal.numProcessed)+ " items");
		processBatch();
	}
	public void saveResult(ActionResult res) { 
		if (isSave() && res.completed) {
			lastSavedFile = save(
				res.name,
				res.details,
				res.types,
				res.completed
			);
		} else {
			lastSavedFile = null;
		}
	}
	public void traversalStart() {
	}

	public String getSaveFileName() {
		return saveFile;
	}
	public File getSaveDir() {
		return saveDir;
	}
	public void reportSave(File f) {
		System.out.println(" ==> "+f.getAbsolutePath());
	}
	
	public File save(String fname, Object[][] details, TreeMap<String, Stats> mystats, boolean completed) {
		boolean writeheader = false;
		String newname = getSaveFileName();
		if (newname.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-hhmmss");
			Date d = new Date();
			String prefix = "fa-"+df.format(d);
			String suffix = fname.replaceAll("[\\s:/\\\\]", "");
			newname = prefix+"-"+suffix;			
		}
		File pf = getSaveDir();
		String newname2 = newname+".txt";
		File f = new File(pf,newname2);
		for(int i=0; !overwrite() && f.exists();i++){
			newname2 = newname+"-"+i+".txt";			
			f = new File(pf,newname2);
		}
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			boolean[] cols = new boolean[details.length];
			for(int i=0; i<details.length; i++) {
				cols[i] = (details[i].length > 4) ? (Boolean)details[i][4] : true;
			}
			boolean first = true;
			if (writeheader){
				for(int i=0; i<details.length; i++) {
					if (!cols[i]) continue;
					if (first)
						first = false;
					else
						bw.write("\t");
					bw.write("\"");
					bw.write(details[i][1].toString());
					bw.write("\"");
				}
				bw.write("\r\n");				
			}
			for (Iterator<String> i = mystats.keySet().iterator(); i.hasNext();) {
				String s = i.next();
				Vector<Object> curvals = mystats.get(s).vals;
				if (myresfilter.evaluate(curvals)){
					int c = 0;
					first = true;
					if (cols[c]) {
						bw.write("\"");
						bw.write(s);
						bw.write("\"");
						first = false;
					}
					for (Iterator<Object> j = curvals.iterator(); j.hasNext();) {
						Object o = j.next();
						String sc = (o==null)?"":o.toString();
						c++;
						if (cols[c]) {
							if (first)
								first = false;
							else
								bw.write("\t");
							bw.write("\"");
							bw.write(sc);
							bw.write("\"");
						}
					}
					bw.write("\r\n");					
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reportSave(f);
		return f;
	}
	
	public void importFileStart() {
		
	}
	public void importFile(Importer imp, File f) throws IOException {
		importFileStart();
		ActionResult res = imp.importFile(f);
		importFileEnd(res);
	}
	public void importFileEnd(ActionResult res) {
		saveResult(res);		
	}
	public void initiateFileTest(File input, File output) {
		root = input;
		saveFile = output.getName();
		saveDir = output.getParentFile();
		fileTraversal.traverseFile();		
	}
	public void loadBatch(File f) throws IOException {
		batchItems = DelimitedFileImporter.parseFile(f, "\t", false);
		batchLoaded();
	}
	
	public void batchStart(){
		pause = false;
		isBatchProcessing = true;
		processBatch();
	}
	public void batchComplete(){
		isBatchProcessing = false;
		pause = true;
	}
	public boolean isPauseBatch() {
		return pause;
	}

	public void pauseBatch(){
		pause = true;
	}
	public void logBatchSize() {
	}
	public void batchLoaded() {
	}
	public void clearBatch() {
		batchItems.clear();
		batchComplete();
	}
	
	public void resetTest(){
		
	}
	public void processBatch() {
		if (isPauseBatch()) {
			resetTest();
			return;
		}
		if (batchItems.isEmpty()) {
			batchComplete();
			return;
		}
		Vector<String> inout = batchItems.remove(0);
		logBatchSize();
		if (inout.size()!=2) return;
		File f1 = new File(inout.get(0));
		File f2 = new File(getSaveDir(), inout.get(1));
		System.out.println("  *"+f1.getAbsolutePath());
		System.out.println("\t-->\t"+f2.getAbsolutePath()+"...");
		initiateFileTest(f1, f2);
		System.out.println("\t"+batchItems.size()+" batch items remaining");
		System.out.flush();
	}
}
