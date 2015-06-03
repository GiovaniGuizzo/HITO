package jmetal.base.solutionType;

import jmetal.base.SolutionType;
import jmetal.base.Variable;
import jmetal.base.variable.PermutationIncremental;
import jmetal.problems.CITO_CAITO;

public class PermutationIncrementalSolutionType extends SolutionType {

    public PermutationIncrementalSolutionType(CITO_CAITO problem) throws ClassNotFoundException {
        super(problem);
        problem.variableType_ = new Class[problem.getNumberOfVariables()];
        problem.setSolutionType(this);

        // Initializing the types of the variables
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            problem.variableType_[i] = Class.forName("jmetal.base.variable.PermutationIncremental");
        }
    }

    public Variable[] createVariables(int[][] constraints) {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];

        for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
            variables[var] = new PermutationIncremental((CITO_CAITO) problem_, var);
            variables[var].toString();
        }

        return variables;
    }
}
