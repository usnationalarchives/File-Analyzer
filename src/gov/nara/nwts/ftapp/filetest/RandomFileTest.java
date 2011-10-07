package gov.nara.nwts.ftapp.filetest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.YN;
import gov.nara.nwts.ftapp.ftprop.FTPropEnum;
import gov.nara.nwts.ftapp.stats.RandomStats;
import gov.nara.nwts.ftapp.stats.Randomizer;
import gov.nara.nwts.ftapp.stats.Stats;

/**
 * Random sampling engine that will compute an appropriate test batch size for a given AQL (acceptable quality level) using the Mil 105E algorithm.
 * @author TBrady
 *
 */
class RandomFileTest extends DefaultFileTest implements Randomizer {
	TreeMap<Long, String> keySet;
	public static final String LAQL="AQL";
	
	public static Object[][] details = {
		{String.class,"Path",450},
		{YN.class,"Selected",60, YN.values(),false},
	};
	
	public RandomFileTest(FTDriver dt) {
		super(dt);
		keySet = new TreeMap<Long, String>();
		ftprops.add(new FTPropEnum(this, LAQL,"AQL","Set the acceptible quality level",AQL.values(),AQL.AQLp04));
	}

	public String toString() {
		return "Random Sampling Mil 105E";
	}
	public String getKey(File f) {
		return f.getAbsolutePath();
	}
    public Stats createStats(String key){
    	Stats stats = new RandomStats(key);
    	stats.vals.add(YN.N);
    	return stats;
    }
	
    public String getShortName(){return "Random";}

    public Object[][] getStatsDetails() {
    	return details;
    }

	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "This test will return a list of files in random order for QC processing";
	}

	public Object fileTest(File f) {
		return null;
	}

	public TreeMap<Long, String> getTreeSet() {
		return keySet;
	}

	public void refineResults() {
		int count=0;
		AQL aql = (AQL)getProperty(LAQL);
		int batchSize = dt.types.size();
		int sampleSize = aql.getSampleSize(batchSize);
		
		for(Iterator<String>i=keySet.values().iterator(); i.hasNext(); ){
			Stats stats = dt.types.get(i.next());
			stats.vals.set(0, (count< sampleSize) ? YN.Y : YN.N);
			count++;
		}
		keySet.clear();
	}
	
	/**
	 * Acceptable quality levels as defined by Mil 105E.
	 * Five AQL's have been coded as an illustrative example.
	 * @author TBrady
	 *
	 */
	enum AQL {
		AQLp04("AQL 0.04%", -1,-1,-1,-1,-1,-1,-1,315,315,315,315,315,490,715,715),
		AQL1("AQL 1.00%",-1,13,13,13,13,13,20,29,34,42,50,60,74,90,102),
		AQL2p5("AQL 2.50%",5,5,5,5,7,11,13,16,19,23,29,35,40,40,40),
		AQL4("AQL 4.00%",3,3,3,5,6,7,10,11,15,18,22,29,29,29,29),
		AQL6p5("AQL 6.50%",2,2,3,5,5,6,7,9,11,13,15,15,15,15,15);
		
		String name;
		public String toString() {
			return name;
		}
		class Sample {
			int batchSize;
			int sampleSize;
			Sample(int batchSize, int sampleSize) {
				this.batchSize = batchSize;
				this.sampleSize = sampleSize;
			}
		}
		ArrayList<Sample> sampleSizes;
		
		AQL(String name,int v8, int v15, int v25,int v50, int v90,int v150, int v280, int v500, int v1200, int v3200, int v10000, int v35000, int v150000, int v500000, int vmax) {
			this.name = name;
			sampleSizes = new ArrayList<Sample>();
			sampleSizes.add(new Sample(8,v8));
			sampleSizes.add(new Sample(15,v15));
			sampleSizes.add(new Sample(25,v25));
			sampleSizes.add(new Sample(50,v50));
			sampleSizes.add(new Sample(90,v90));
			sampleSizes.add(new Sample(150,v150));
			sampleSizes.add(new Sample(280,v280));
			sampleSizes.add(new Sample(500,v500));
			sampleSizes.add(new Sample(1200,v1200));
			sampleSizes.add(new Sample(3200,v3200));
			sampleSizes.add(new Sample(10000,v10000));
			sampleSizes.add(new Sample(35000,v35000));
			sampleSizes.add(new Sample(150000,v150000));
			sampleSizes.add(new Sample(500000,v500000));
			sampleSizes.add(new Sample(-1,vmax));
		};
		
		int getSampleSize(int batchSize) {
			for(Iterator<Sample>i=sampleSizes.iterator(); i.hasNext();){
				Sample s = i.next();
				if (s.batchSize >= batchSize) {
					return (s.sampleSize == -1) ? batchSize : s.sampleSize;
				} 
				if (s.batchSize == -1) {
					return s.sampleSize;
				} 
			}
			return batchSize;
		}
	}
}
