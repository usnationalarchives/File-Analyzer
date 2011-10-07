package gov.nara.nwts.ftapp.nameValidation;

import java.io.File;

public class RenameDetails {
	public RenameStatus status;
	File newFile;
	public String note;
	public RenameDetails(RenameStatus status, File newFile){
		this(status,newFile,null);
	}
	public RenameDetails(RenameStatus status){
		this(status,null,null);
	}
	public RenameDetails(RenameStatus status, File newFile, String note){
		this.status = status;
		this.newFile = newFile;
		this.note = note;
	}
	public RenamePassFail getPassFail() {return status.getPassFail();}
	public String getMessage() {
		StringBuffer buf = new StringBuffer();
		if (note!=null) {
			buf.append(note);
			buf.append(" ");
		}
		buf.append(status.getMessage());
		return buf.toString();
	}
	public String getRenameStatus() {return status.toString();}
	public File getFile() {return newFile;}
	public String getDetailNote(File root){
		if (newFile == null) {
			return (note==null)?"":note;
		}
		String s = newFile.getAbsolutePath();
		String r = root.getAbsolutePath();
		String m = s;
		if (s.startsWith(r)) {
			m = s.substring(r.length());
		}
		return m;
	}
	public String toString() {
		return status.toString() +": " + ((note==null)?"":note);
	}
	
}


