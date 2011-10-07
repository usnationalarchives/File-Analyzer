package gov.nara.nwts.ftapp.crud;

/**
 *Categorizes the resulting status of a database action.
 * @author TBrady
 *
 */
public enum CRUD implements Comparable<CRUD> {
	NA,
	Created,
	Replaced,
	Updated,
	Deleted,
	Skipped,
	NotFound,
	AlreadyExists;
}
