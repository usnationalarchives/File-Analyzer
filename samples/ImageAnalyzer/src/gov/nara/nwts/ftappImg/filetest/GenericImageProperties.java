package gov.nara.nwts.ftappImg.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.filetest.DefaultFileTest;
import gov.nara.nwts.ftapp.filter.JpegFileTestFilter;
import gov.nara.nwts.ftapp.filter.TiffFileTestFilter;
import gov.nara.nwts.ftapp.filter.TiffJpegFileTestFilter;
import gov.nara.nwts.ftappImg.jpeg.JpegExtractor;
import gov.nara.nwts.ftappImg.stats.GenericImageStats;
import gov.nara.nwts.ftappImg.tif.TifExtractor;
import gov.nara.nwts.ftappImg.tags.XMPExtractor;
import gov.nara.nwts.ftappImg.tags.ImageTags.DUP;
import gov.nara.nwts.ftappImg.tags.ImageTags.NARAREQ;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGCONTENT;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGTYPE;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGLOC;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
import java.util.ArrayList;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.sun.media.jai.codec.TIFFField;

/**
 * Extract all metadata fields from a TIF or JPG using categorized tag defintions.
 * @author TBrady
 *
 */
class GenericImageProperties extends DefaultFileTest { 
	long counter = 1000000;
	public GenericImageProperties(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Image Properties";
	}
	public String getKey(File f) {
		return f.getName();
	}
	
    public String getShortName(){return "Img";}

    public boolean isTestable(File f) {
    	return !f.getName().startsWith(".");
    }
    
	public Object fileTest(File f) {
		if (f.getName().toLowerCase().endsWith(".tif")) {
			return doTiff(f);
		}
		if (f.getName().toLowerCase().endsWith(".jpg")) {
			return doJpg(f);
		}
		return null;
	}
	public Object doJpg(File f) {
		String filekey = getKey(f);
		JpegExtractor jpegext = new JpegExtractor(f);
		ArrayList<Object> list = jpegext.getTags();
		for(Object obj: list){
			if (obj instanceof String) {
				addObject(doJpgObject(filekey, jpegext, (String)obj));
			} else if (obj instanceof XMPPropertyInfo) {
				addObject(doXMPObject(filekey, jpegext.xmpex, (XMPPropertyInfo)obj));
			}
		}
		dt.types.remove(filekey);
		jpegext.close();
		return null;
	}
	public Object doTiff(File f) {
		String filekey = getKey(f);
		TifExtractor tiffext = new TifExtractor(f);
		ArrayList<Object> list = tiffext.getTags();
		for(Object obj: list){
			if (obj instanceof TIFFField) {
				addObject(doTifObject(filekey, tiffext, (TIFFField)obj));
			} else if (obj instanceof XMPPropertyInfo) {
				addObject(doXMPObject(filekey, tiffext.xmpex, (XMPPropertyInfo)obj));
			}
		}
		dt.types.remove(filekey);
		tiffext.close();
		return null;
	}
	
	public GenericImageStats doJpgObject(String filekey, JpegExtractor jpegext, String tag) {
		String val = jpegext.getAttribute(tag);
		TAGLOC tiffloc = TAGLOC.JPG;
		TAGTYPE tagtype = TAGTYPE.JPG;
		NARAREQ narareq = NARAREQ.UNDECIDED;
		TAGCONTENT tagcontent = TAGCONTENT.UNDECIDED;
		DUP dupfield = DUP.NA;
		String name = tag;

		TAGS tagdef = jpegext.getTagDef(tag);
		if (tagdef != TAGS.UNDEFINED) {
			tag = tagdef.name();
			tiffloc = tagdef.tiffloc;
			tagtype = tagdef.tagtype;
			narareq = tagdef.narareq;
			tagcontent = tagdef.tagcontent;
			dupfield = tagdef.dup;			
		}

		return new GenericImageStats(""+counter,filekey,tag,name, val,tiffloc,tagtype,narareq,tagcontent, dupfield);		
	}
	public GenericImageStats doTifObject(String filekey, TifExtractor tiffext, TIFFField tf) {
		String tag = "";
		TAGLOC tiffloc = TAGLOC.TAG;
		TAGTYPE tagtype = TAGTYPE.TIFF_TAG;
		NARAREQ narareq = NARAREQ.UNDECIDED;
		TAGCONTENT tagcontent = TAGCONTENT.UNDECIDED;
		DUP dupfield = DUP.NA;
		String path ="";

		TAGS tagdef = tiffext.getTagDef(tf);
		if (tagdef == TAGS.UNDEFINED) {
			tag=tiffext.getTagName(tf);					
		} else {
			tag = tagdef.name();
			path = "Tag: "+tagdef.tag;
			tiffloc = tagdef.tiffloc;
			tagtype = tagdef.tagtype;
			narareq = tagdef.narareq;
			tagcontent = tagdef.tagcontent;
			dupfield = tagdef.dup;
		}
		String val = tiffext.getTiffObject(tf).toString();
		return new GenericImageStats(""+counter,filekey,tag,path,val,tiffloc,tagtype,narareq,tagcontent, dupfield);
	}
	public GenericImageStats doXMPObject(String filekey, XMPExtractor xmpex, XMPPropertyInfo xpi) {
		String tag = "";
		String val = "";
		
		TAGLOC tiffloc = TAGLOC.XMP;
		TAGTYPE tagtype = TAGTYPE.OTHER_XMP;
		NARAREQ narareq = NARAREQ.UNDECIDED;
		TAGCONTENT tagcontent = TAGCONTENT.UNDECIDED;
		DUP dupfield = DUP.NA;

		String[] path = new String[2];
		path[0] = xpi.getNamespace();
		path[1] = xpi.getPath();
		tag=xpi.getPath();
		if (tag == null) {
			return null;
		} else if (tag.endsWith("xml:lang")) {
			return null;
		} else if (tag.equals("")) {
			return null;
		} else {
			for(TAGS t: TAGS.values()){
				if ((t.tiffloc == TAGLOC.XMP) && tag.equals(t.path)) {
					tag = t.name();
					tagtype = t.tagtype;
					narareq = t.narareq;
					tagcontent = t.tagcontent;
					dupfield = t.dup;
					break;
				}
			}
			
			if (tagcontent == TAGCONTENT.CONTAINER) 
				return null;
			
			if (tagtype == TAGTYPE.OTHER_XMP) {
				tag=xpi.getPath()+" --> "+xpi.getNamespace();						
			}
			
			val = xmpex.getXMP(path);
			return new GenericImageStats(""+counter,filekey,tag,path[1],val,tiffloc,tagtype,narareq,tagcontent, dupfield);
		}
	}
	
	public void addObject(GenericImageStats gis) {
		if (gis == null) return;
		dt.types.put(gis.key, gis);
		counter++;
	}
	
    public Stats createStats(String key){ 
    	return new GenericImageStats(key);
    }
    public Object[][] getStatsDetails() {
    	return GenericImageStats.details;
    }

	public void initFilters() {
		filters.add(new TiffJpegFileTestFilter());
		filters.add(new TiffFileTestFilter());
		filters.add(new JpegFileTestFilter());
	}

	public String getDescription() {
		return "";
	}

}
