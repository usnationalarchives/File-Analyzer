package gov.nara.nwts.ftapp.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;

import gov.nara.nwts.ftapp.nameValidation.RenamePassFail;
import gov.nara.nwts.ftapp.crud.CRUD;

import java.io.File;

/**
 * Stats object that reports on database ingest actions.
 * @author TBrady
 *
 */
public class CRUDStats extends Stats {

	public static Object[][] details = {
		{String.class,"Path",450},
		{String.class,"Pass/Fail",50,RenamePassFail.values()},
		{String.class,"Status",150, CRUD.values()},
	};

	
	public CRUDStats(String key) {
		super(key);
		vals.add("");
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object ret = fileTest.fileTest(f);
		return ret;
	}

	
}
