package gov.nara.nwts.ftappImg.tif;

import gov.nara.nwts.ftappImg.tags.DefaultExtractor;
import gov.nara.nwts.ftappImg.tags.XMPExtractor;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGLOC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDirectory;
import com.sun.media.jai.codec.TIFFField;

/** 
 * Helper class for traversing TIF metadata
 * @author TBrady
 *
 */
public class TifExtractor extends DefaultExtractor {
	File file;
	TIFFDirectory tiffdir;
	FileSeekableStream fss; 
	public XMPExtractor xmpex;
	
	public TifExtractor(File f) {
		this.file = f;
		try {
			fss = new FileSeekableStream(f);
			if (fss != null) {
				tiffdir = new TIFFDirectory(fss, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			xmpex = new XMPExtractor();
			TIFFField tf = tiffdir.getField(TAGS.TIFF_XMP.tag);
			if (tf==null) return;
			xmpex = new XMPExtractor(XMPMetaFactory.parseFromBuffer(tf.getAsBytes()));
		} catch (XMPException e) {
			e.printStackTrace();
		}
	}
	
	public Object getTiffInt(TAGS tag) {
		if (tiffdir == null) return "";
		return getTiffInt(tiffdir.getField(tag.tag));
	}
	public Object getTiffInt(int tag) {
		if (tiffdir == null) return "";
		return getTiffInt(tiffdir.getField(tag));
	}
	public Object getTiffInt(TIFFField tf) {
		if (tf == null) return "";
		if (tf.getCount()==0) return "";
		return tf.getAsInt(0);
	}

	public String getTiffString(TAGS tag) {
		if (tiffdir == null) return "";
		if (tag.tiffloc == TAGLOC.XMP) {
			String[] path = new String[2];
			path[0] = tag.tagtype.ns;
			path[1] = tag.path;
			return getXMP(path);
		}
		return getTiffString(tiffdir.getField(tag.tag));
	}

	public String getString(TAGS tag) {
		return getTiffString(tag);
	}
	
	public String getTiffString(int tag) {
		if (tiffdir == null) return "";
		return getTiffString(tiffdir.getField(tag));
	}
	public String getTiffString(TIFFField tf) {
		if (tf == null) return "";
		if (tf.getCount()==0) return "";
		return getTiffObject(tf).toString();
		//return tf.getAsString(0);
	}
	
	public Object getTiffObject(TIFFField tf) {
		if (tiffdir == null) return "";
		try {
			if (tf.getType() == TIFFField.TIFF_SHORT) {
				return tf.getAsInt(0);
			} else if (tf.getType() == TIFFField.TIFF_DOUBLE) {
				return tf.getAsDouble(0);
			} else if (tf.getType() == TIFFField.TIFF_LONG) {
				return tf.getAsLong(0);
			} else if (tf.getType() == TIFFField.TIFF_FLOAT) {
				return tf.getAsFloat(0);
			} else if (tf.getType() == TIFFField.TIFF_BYTE) {
				return " - not shown -";
			} else if (tf.getType() == TIFFField.TIFF_RATIONAL) {
				StringBuffer buf = new StringBuffer();
				for(long l: tf.getAsRational(0)) {
					if (buf.length()>0) buf.append(",");
					buf.append(l);
				}
				return buf.toString();
			} else if (tf.getType() == TIFFField.TIFF_UNDEFINED) {
				return "Undef: ";
			} else if (tf.getType() == TIFFField.TIFF_SBYTE) {
				return "Undef: ";
			}
			return tf.getAsString(0);
		} catch (Exception e) {
			e.printStackTrace();
			return "**" + tf.getType();
		}
	}
	

	public String getTagName(TIFFField tf) {
		if (tiffdir == null) return "";
		int tag = tf.getTag();
		TAGS tags = getTagDef(tf);
		if (tags == TAGS.UNDEFINED) {
			return "Tiff Tag " + tag;
		}
		return tags.name();
	}
	public TAGS getTagDef(TIFFField tf) {
		if (tiffdir == null) return TAGS.UNDEFINED;
		int tag = tf.getTag();
		
		for(TAGS t: TAGS.values()) {
			if (t.tiffloc == TAGLOC.TAG && tag == t.tag) {
				return t;
			}
		}
		return TAGS.UNDEFINED;
	}
	
	public ArrayList<Object> getTags() {
		ArrayList<Object> list = new ArrayList<Object>();
		if (tiffdir != null){
			for(TIFFField tf: tiffdir.getFields()) {
				list.add(tf);
			}
		}
		if (xmpex != null){
			list.addAll(xmpex.getTags());			
		}
		return list;
	}
	
	
	public String getXMP(String[] xmppath) {
		return xmpex.getXMP(xmppath);
	}
	
	public void close() {
		try {
			if (fss != null) fss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
