package gov.nara.nwts.ftapp;

import java.util.Date;
/**
 * Helper class to report on the duration of a File Analyzer action.
 * @author TBrady
 *
 */
public class Timer {
	Date start;
	Date end;
	
	public Timer() {
		start = new Date();
	}
	
	public void end() {
		end = new Date();
	}
	
	public double getDuration() {
		if (end == null) 
			end();
		return (end.getTime() - start.getTime())*.001;
	}
}
