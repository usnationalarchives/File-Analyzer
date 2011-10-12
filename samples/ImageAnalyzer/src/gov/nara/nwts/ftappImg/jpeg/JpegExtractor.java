package gov.nara.nwts.ftappImg.jpeg;

import gov.nara.nwts.ftappImg.tags.XMPExtractor;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGLOC;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.parser.image.xmp.XMPPacketScanner;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import gov.nara.nwts.ftappImg.tags.DefaultExtractor;

/**
 * Helper class for traversing JPG metadata
 * @author TBrady
 *
 */
public class JpegExtractor extends DefaultExtractor{
	Metadata m;
	JpegParser jp;
	public XMPExtractor xmpex;
	
	public JpegExtractor(File f) {
		
		ContentHandler ch = new DefaultHandler();

		m = new Metadata();
		ParseContext pc = new ParseContext();
		jp = new JpegParser();
		XMPPacketScanner xps = new XMPPacketScanner();
		
		try {
			jp.parse(new BufferedInputStream(new FileInputStream(f)), ch, m, pc);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			xps.parse(new FileInputStream(f), baos);
			if (baos.size() > 0){
				try {
					xmpex = new XMPExtractor(XMPMetaFactory.parseFromBuffer(baos.toByteArray()));
				} catch (XMPException e) {
					e.printStackTrace();
				}				
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (TikaException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public TAGS getTagDef(String tag) {
		
		for(TAGS t: TAGS.values()) {
			if (t.tiffloc == TAGLOC.JPG && tag.equals(t.path)) {
				return t;
			}
		}
		return TAGS.UNDEFINED;
	}
	public ArrayList<Object> getTags() {
		ArrayList<Object> list = new ArrayList<Object>();
		for(String s: m.names()) {
			list.add(s);
		}
		if (xmpex!=null){
			list.addAll(xmpex.getTags());			
		}
		return list;
	}
	
	public int getInt(String name, int def) {
		try {
			return Integer.parseInt(getAttribute(name));
		} catch (NumberFormatException e){
			return def;
		}
	}

	public float getFloat(String name, int def) {
		try {
			return Float.parseFloat(getAttribute(name));
		} catch (NumberFormatException e){
			return def;
		}
	}

	public void close() {
	}

	public String getAttribute(String name) {
		return m.get(name);
	}

	public String getString(TAGS tags) {
		return getAttribute(tags.path);
	}
}

