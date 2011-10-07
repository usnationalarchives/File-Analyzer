package gov.nara.nwts.ftapp.stats;

import java.util.TreeMap;

/**
 * Helper class for generating a random sample.
 * @author TBrady
 *
 */
public interface Randomizer {
    public TreeMap<Long,String> getTreeSet();
}
