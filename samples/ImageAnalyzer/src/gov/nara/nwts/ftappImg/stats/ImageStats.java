package gov.nara.nwts.ftappImg.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.stats.Stats;
import gov.nara.nwts.ftappImg.tags.XMPExtractor;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;
import gov.nara.nwts.ftappImg.tif.TifExtractor;

import java.io.File;
/**
 * Generic container for reporting on image properties; this implementation has been superceeded by better implementations
 * @author TBrady
 *
 */
public class ImageStats extends Stats {
	public static Object[][] details = {
			{String.class,"File",200},
			{Integer.class,"Bits/Channel (258)",100},
			{Integer.class,"Color Space (262)",100},
			{String.class,"ICC Profile",120},
			{String.class,"Description (270)",300},
			{String.class,"Keywords (XMP)",200},
			{String.class,"Instructions (XMP)",200},
			{String.class,"Desc 1",150},
			{String.class,"Desc 2",150},
			{String.class,"Desc 3",150},
			{String.class,"Desc 4",150},
		};

	public ImageStats(String key) {
		super(key);
		vals.add(new Integer(0));
		vals.add(new Integer(0));
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		TifExtractor tiffext = new TifExtractor(f);
		vals.set(0,tiffext.getTiffInt(TAGS.TIFF_BITS_PER_CHANNEL));
		vals.set(1,tiffext.getTiffInt(TAGS.TIFF_COLOR_SPACE));
		vals.set(2,tiffext.getXMP(XMPExtractor.XMP_ICC));
		String tfs = tiffext.getTiffString(TAGS.TIFF_DESCRIPTION);
		vals.set(3,tfs);
		vals.set(4, tiffext.getXMP(XMPExtractor.XMP_KEY));
		vals.set(5, tiffext.getXMP(XMPExtractor.XMP_INSTR));

		String[] parts = tfs.split("(\\s\\s\\s+|\n)");
		for(int i=0; (i < parts.length) && (i <4); i++){
			vals.set(6+i,parts[i]);
		}
		tiffext.close();
		return fileTest.fileTest(f);
	}
}
