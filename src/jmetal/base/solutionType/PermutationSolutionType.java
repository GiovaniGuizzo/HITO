/**
 * PermutationType
 *
 * @author Antonio J. Nebro
 * @version 1.0
 * 
 * Class representing the solution type of solutions composed of Permuation
 * variables 
 */
package jmetal.base.solutionType;

import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.Permutation;
import jmetal.problems.CITO_CAITO;

public class PermutationSolutionType extends SolutionType {

    /**
     * Constructor
     * @param problem
     * @throws ClassNotFoundException
     */
    public PermutationSolutionType(CITO_CAITO problem) throws ClassNotFoundException {
        super(problem);
        problem.variableType_ = new Class[problem.getNumberOfVariables()];
        problem.setSolutionType(this);

        // Initializing the types of the variables
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            problem.variableType_[i] = Class.forName("jmetal.base.variable.Permutation");
        }
    } // PermutationSolution

    /**
     * Creates the variables of the solution
     * @param decisionVariables
     */
    public Variable[] createVariables(int[][] constraints) {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];

        for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
            variables[var] = new Permutation((CITO_CAITO) problem_, var);
        }

        return variables;
    } // createVariables
} // PermutationSolution

