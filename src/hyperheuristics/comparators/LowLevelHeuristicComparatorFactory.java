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
public class LowLevelHeuristicComparatorFactory {

    public static final String NAME_COMPARATOR = "Name";
    public static final String CHOICE_FUNCTION_COMPARATOR = LowLevelHeuristic.CHOICE_FUNCTION;
    public static final String MULTI_ARMED_BANDIT_COMPARATOR = LowLevelHeuristic.MULTI_ARMED_BANDIT;
    public static final String RANDOM_COMPARATOR = LowLevelHeuristic.RANDOM;

    public static Comparator<LowLevelHeuristic> createComparator(String name) {
        switch (name) {
            case CHOICE_FUNCTION_COMPARATOR:
                return new LowLevelHeuristicChoiceFunctionComparator();
            case RANDOM_COMPARATOR:
                return new LowLevelHeuristicRandomComparator();
            case NAME_COMPARATOR:
                return new LowLevelHeuristicNameComparator();
            case MULTI_ARMED_BANDIT_COMPARATOR:
                return new LowLevelHeuristicMultiArmedBanditComparator();
            default:
                return null;
        }
    }

}
