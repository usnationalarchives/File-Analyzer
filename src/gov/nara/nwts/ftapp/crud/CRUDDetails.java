package gov.nara.nwts.ftapp.crud;

import gov.nara.nwts.ftapp.nameValidation.RenamePassFail;
/**
 * Stores the results of a database action.
 * @author TBrady
 *
 */
public class CRUDDetails {
	CRUD action;
	RenamePassFail status;
	int id;
	String note;
	public CRUDDetails(CRUD action, RenamePassFail status, int id, String note){
		this.action = action;
		this.status = status;
		this.id = id;
		this.note = note;
	}
	public String toString() {
		return action.toString()+" "+ status.toString() +". ID=" + id+". "+((note==null)?"":note);
	}
}


