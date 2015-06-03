package jmetal.qualityIndicator;

import jmetal.base.operator.comparator.DominanceComparator;
import jmetal.base.operator.comparator.EqualSolutionsByObjectives;

/**
 *
 * @author giovani
 */
public class Coverage {

    private final DominanceComparator dominanceComparator;
    private final EqualSolutionsByObjectives equalSolutionsByObjectives;

    public Coverage() {
        dominanceComparator = new DominanceComparator();
        equalSolutionsByObjectives = new EqualSolutionsByObjectives();
    }

    public double coverage(double[][] frontA, double[][] frontB) {
        double coverage = 0;
        for (double[] solutionB : frontB) {
            for (double[] solutionA : frontA) {
                int flag = dominanceComparator.compare(solutionA, solutionB);
                if (flag == -1 || equalSolutionsByObjectives.equals(solutionA, solutionB)) {
                    coverage++;
                    break;
                }
            }
        }
        return coverage / frontB.length;
    }
}
