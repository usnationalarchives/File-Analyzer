package gov.nara.nwts.ftappImg.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftappImg.jpeg.JpegExtractor;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Generic container for reporting on JPG properties; this implementation has been superceeded by a better implementation
 * @author TBrady
 *
 */
public class JpegStats extends Stats {
	public static Object[][] details = {
			{String.class,"File",200},
			{Integer.class,"tiff:BitsPerSample",100},
			{String.class,"Color Space",100},
			{String.class,"Headline",300},
			{String.class,"Caption/Abstract",200},
			{String.class,"Keywords",200},

			{String.class,"X Resolution",200},
			{String.class,"Y Resolution",200},

			{Float.class,"tiff:XResolution",200},
			{Float.class,"tiff:YResolution",200},
			{String.class,"tiff:ImageWidth",200},
			{String.class,"tiff:ImageLength",200},

			{String.class,"Image Width",200},
			{String.class,"Image Height",200},
			{String.class,"Exif Image Width",200},
			{String.class,"Exif Image Height",200},
		};

	public JpegStats(String key) {
		super(key);
		vals.add(new Integer(0));
		vals.add(""); //1
		vals.add(""); //2
		vals.add(""); //3
		vals.add(""); //4

		vals.add(""); //5
		vals.add(""); //6

		vals.add(new Float(0)); //7
		vals.add(new Float(0)); //8
		vals.add(""); //9
		vals.add(""); //10

		vals.add(""); //11
		vals.add(""); //12
		vals.add(""); //13
		vals.add(""); //14
	}
	
	
	public Object compute(File f, FileTest fileTest) {
		JpegExtractor jext = new JpegExtractor(f);
		vals.set(0,jext.getInt("tiff:BitsPerSample", 0));
		vals.set(1, jext.getAttribute("Color Space"));
		vals.set(2, jext.getAttribute("Headline"));
		vals.set(3, jext.getAttribute("Caption/Abstract"));
		vals.set(4, jext.getAttribute("Keywords"));

		vals.set(5, jext.getAttribute("X Resolution"));
		vals.set(6, jext.getAttribute("Y Resolution"));

		vals.set(7, jext.getFloat("tiff:XResolution", 0));
		vals.set(8, jext.getFloat("tiff:YResolution", 0));
		vals.set(9, jext.getAttribute("tiff:ImageWidth"));
		vals.set(10, jext.getAttribute("tiff:ImageLength"));

		vals.set(11, jext.getAttribute("Image Width"));
		vals.set(12, jext.getAttribute("Image Height"));
		vals.set(13, jext.getAttribute("Exif Image Width"));
		vals.set(14, jext.getAttribute("Exif Image Height"));
		jext.close();
		return fileTest.fileTest(f);
	}
}
