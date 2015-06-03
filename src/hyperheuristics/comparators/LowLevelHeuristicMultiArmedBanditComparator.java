/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.comparators;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.Comparator;

/**
 *
 * @author gian
 */
public class LowLevelHeuristicMultiArmedBanditComparator  implements Comparator<LowLevelHeuristic> {

    @Override
    public int compare(LowLevelHeuristic o1, LowLevelHeuristic o2) {
        if (o1.getMultiArmedBanditValue() > o2.getMultiArmedBanditValue()) {
            return -1;
        } else if (o1.getMultiArmedBanditValue() < o2.getMultiArmedBanditValue()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
