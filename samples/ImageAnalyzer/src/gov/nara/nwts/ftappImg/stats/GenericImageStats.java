package gov.nara.nwts.ftappImg.stats;

import gov.nara.nwts.ftapp.stats.Stats;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGS;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGLOC;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGTYPE;
import gov.nara.nwts.ftappImg.tags.ImageTags.NARAREQ;
import gov.nara.nwts.ftappImg.tags.ImageTags.TAGCONTENT;
import gov.nara.nwts.ftappImg.tags.ImageTags.DUP;

/**
 * Stats object to report on metadata coming from either a TIF or a JPG using tag definitions
 * @author TBrady
 *
 */
public class GenericImageStats extends Stats {
	public static Object[][] details = {
		{String.class,"Key",60, null, false},
		{String.class,"File",150},
		{String.class,"Name",150, TAGS.values()},
		{String.class,"Path",200},
		{String.class,"Value",200},
		{TAGTYPE.class,"Type",100,TAGTYPE.values()},
		{TAGCONTENT.class,"Content",100,TAGCONTENT.values()},
		{DUP.class,"Dup Info",100,DUP.values()},
		{TAGLOC.class,"Loc",50,TAGLOC.values()},
		{NARAREQ.class,"Nara Use",120,NARAREQ.values()},
	};

	public GenericImageStats(String key) {
		this(key,"","","","",TAGLOC.NA,TAGTYPE.TIFF_TAG,NARAREQ.UNDECIDED,TAGCONTENT.UNDECIDED, DUP.NA);
	}

	public GenericImageStats(String key, String file, String name, String path, String value, TAGLOC tiffloc, TAGTYPE tagtype, NARAREQ narareq, TAGCONTENT tagcontent, DUP dup) {
		super(key);
		vals.add(file);
		vals.add(name);
		vals.add(path);
		vals.add(value);
		vals.add(tagtype);
		vals.add(tagcontent);
		vals.add(dup);
		vals.add(tiffloc);
		vals.add(narareq);
	}

}
