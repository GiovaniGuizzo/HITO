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
 * @author giovaniguizzo
 */
public class LowLevelHeuristicNameComparator implements Comparator<LowLevelHeuristic> {

    @Override
    public int compare(LowLevelHeuristic o1, LowLevelHeuristic o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }

}
