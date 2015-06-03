package jmetal.base.operator.mutation;

import java.util.ArrayList;
import java.util.Properties;
import jmetal.base.Solution;
import jmetal.base.variable.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.problems.CITO_CAITO;

public class SwapMutationIncremental extends Mutation {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public SwapMutationIncremental() {
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public SwapMutationIncremental(Properties properties) {
        this();
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void doMutation(double probability, Solution solution, CITO_CAITO problem) throws JMException {
        
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.PermutationIncremental")) {

                int permutationLength = ((PermutationIncremental) solution.getDecisionVariables()[0]).getLength();
                int permutation[] = ((PermutationIncremental) solution.getDecisionVariables()[0]).vector_;

                if (PseudoRandom.randDouble() < probability) {
                    int pos1 = PseudoRandom.randInt(0, permutationLength - 1);
                    int pos2 = pos1;

                    while (pos1 == pos2) {
                        if (pos1 <= (permutationLength - problem.getAspects().size() - 1)) {
                            pos2 = PseudoRandom.randInt(0, permutationLength - problem.getAspects().size() - 1);
                        } else {
                            pos2 = PseudoRandom.randInt(permutationLength - problem.getAspects().size(), permutationLength - 1);
                        }
                    }

                    // swap
                    int temp = permutation[pos1];
                    permutation[pos1] = permutation[pos2];
                    permutation[pos2] = temp;
                }
            } else {
                Configuration.logger_.severe("SwapMutation.doMutation: invalid type. " + solution.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
        } catch (ClassNotFoundException e) {
        }

    } // doMutation

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Object execute(Object object, CITO_CAITO problem) throws JMException {
        Solution solution = (Solution) object;

        Double probability = (Double) getParameter("probability");
        if (probability == null) {
            Configuration.logger_.severe("SwapMutation.execute: probability not specified");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        this.doMutation(probability.doubleValue(), solution, problem);

        int constraints[][] = problem.getConstraintMatrix();
        ArrayList<Integer> aspects = problem.getAspects();

//        System.out.println(solution.getDecisionVariables()[0].toString());
        
        //rever as restricoes---------------------------------------------------
        int solutionVector[] = ((PermutationIncremental) solution.getDecisionVariables()[0]).vector_;
        solutionVector = problem.tratarRestricoesIncremental(solutionVector, constraints, aspects);

//        System.out.println(solution.getDecisionVariables()[0].toString());

        return solution;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
