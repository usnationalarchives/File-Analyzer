package gov.nara.nwts.ftapp;

import java.util.Vector;

/**
 * Container for the various filters that may be applied to a set of results allowing results to be filtered by one or more columns.
 * @author TBrady
 *
 */
public class ResultFilter {
	class ResultFilterItem {
		int col;
		String val;
		
		ResultFilterItem(int col, String val) {
			this.col = col;
			this.val = val;
		}
	}
	Vector<ResultFilterItem> resfilters;
	public ResultFilter() {
		resfilters = new Vector<ResultFilterItem>();
	}
	
	public void add(int col, String val) {
		resfilters.add(new ResultFilterItem(col, val));
	}
	
	public boolean evaluate(Vector<Object> obj) {
		for(ResultFilterItem rfi: resfilters){
			if (rfi.col < obj.size()) {
				Object o = obj.get(rfi.col);
				if (o == null) o = "";
				if (!rfi.val.equals(o.toString())){
					return false;
				}
			}
		}
		return true;
	}
}
