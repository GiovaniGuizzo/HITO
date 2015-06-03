/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.operators.mutation;

import jmetal.base.Solution;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.variable.Permutation;
import jmetal.problems.CITO_CAITO;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author giovaniguizzo
 */
public class SimpleInsertionMutation extends Mutation {

    @Override
    public Object execute(Object object, CITO_CAITO problem) throws JMException {
        Solution solution = (Solution) object;

        Double probability = (Double) getParameter("probability");
        if (probability == null) {
            Configuration.logger_.severe("SimpleInsertionMutation.execute: probability "
                    + "not specified");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        if (PseudoRandom.randDouble() < probability) {
            this.doMutation(solution);
        }

        //rever as restricoes---------------------------------------------------
        int solutionVector[] = ((Permutation) solution.getDecisionVariables()[0]).vector_;
        solutionVector = problem.tratarRestricoes(solutionVector, problem.getConstraintMatrix());

//        System.out.println(solution.getDecisionVariables()[0].toString());
        return solution;
    }

    private void doMutation(Solution solution) {
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {
                int[] decisionVariables = ((Permutation) solution.getDecisionVariables()[0]).vector_;
                int length = decisionVariables.length;

                int fromPosition = PseudoRandom.randInt(0, length - 1);
                int value = decisionVariables[fromPosition];

                int toPosition;
                do {
                    toPosition = PseudoRandom.randInt(0, length - 1);
                } while (fromPosition == toPosition);

                int[] newArray = new int[length];

                int i;
                if (fromPosition > toPosition) {
                    for (i = 0; i < toPosition; i++) {
                        newArray[i] = decisionVariables[i];
                    }
                    newArray[i] = value;
                    for (i++; i <= fromPosition; i++) {
                        newArray[i] = decisionVariables[i - 1];
                    }
                    for (; i < length; i++) {
                        newArray[i] = decisionVariables[i];
                    }
                } else {
                    for (i = 0; i < fromPosition; i++) {
                        newArray[i] = decisionVariables[i];
                    }
                    for (; i < toPosition; i++) {
                        newArray[i] = decisionVariables[i + 1];
                    }
                    newArray[i] = value;
                    for (i++; i < length; i++) {
                        newArray[i] = decisionVariables[i];
                    }
                }

                ((Permutation) solution.getDecisionVariables()[0]).vector_ = newArray;
            } else {
                Configuration.logger_.severe("SimpleInsertionMutation.doMutation: invalid type+"
                        + "" + solution.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            } // else
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
