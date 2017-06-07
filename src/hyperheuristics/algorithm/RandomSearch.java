package hyperheuristics.algorithm;

import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.util.JMException;

/**
 *
 * @author Giovani Guizzo
 */
public class RandomSearch extends Algorithm {

    private Problem problem;
    private int maxEvaluations;

    public RandomSearch(Problem problem, int maxEvaluations) {
        this.problem = problem;
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        SolutionSet set = new SolutionSet(maxEvaluations);
        for (int i = 0; i < maxEvaluations; i++) {
            Solution solution = new Solution(problem);
            problem.evaluate(solution);
            set.add(solution);
        }
        return set;
    }

}
