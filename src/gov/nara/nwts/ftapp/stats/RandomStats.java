package gov.nara.nwts.ftapp.stats;

import java.io.File;
import java.util.Random;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Generate a random value for each file in a set.
 * @author TBrady
 *
 */
public class RandomStats extends Stats {

	long randomVal;
	Random random;
	
	public RandomStats(String key) {
		super(key);
		random = new Random();
	}
	
	public Object compute(File f, FileTest fileTest) {
		randomVal = random.nextLong();
		if (fileTest instanceof Randomizer) {
			Randomizer r = (Randomizer)fileTest;
			while(r.getTreeSet().get(randomVal) != null) {
				randomVal = random.nextLong();				
			}
			r.getTreeSet().put(randomVal, key);
		}
		return randomVal;
	}
}
