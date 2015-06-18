/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.operators.mutation;

import java.util.ArrayList;
import jmetal.base.Cluster;
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
public class InterSimpleInsertionMutation extends Mutation {

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
        solution = problem.tratarRestricoes(solution, problem.getConstraintMatrix());

//        System.out.println(solution.getDecisionVariables()[0].toString());
        return solution;
    }

    private void doMutation(Solution solution) {
        intercluster(solution);
    }

    private Solution intercluster(Solution solution) {
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {
                ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;
                int length = clusters.size();

                int fromPosition = PseudoRandom.randInt(0, length - 1);
                Cluster value = clusters.get(fromPosition);

                int toPosition;
                do {
                    toPosition = PseudoRandom.randInt(0, length - 1);
                } while (fromPosition == toPosition);

                ArrayList<Cluster> newArray = new ArrayList<>();
                for (int i = 0; i < clusters.size(); i++) {
                    newArray.add(null);
                }

                int i;
                if (fromPosition > toPosition) {
                    for (i = 0; i < toPosition; i++) {
                        newArray.set(i, clusters.get(i));
                    }
                    newArray.set(i, value);
                    for (i++; i <= fromPosition; i++) {
                        newArray.set(i, clusters.get(i - 1));
                    }
                    for (; i < length; i++) {
                        newArray.set(i, clusters.get(i));
                    }
                } else {
                    for (i = 0; i < fromPosition; i++) {
                        newArray.set(i, clusters.get(i));
                    }
                    for (; i < toPosition; i++) {
                        newArray.set(i, clusters.get(i + 1));
                    }
                    newArray.set(i, value);
                    for (i++; i < length; i++) {
                        newArray.set(i, clusters.get(i));
                    }
                }

                ((Permutation) solution.getDecisionVariables()[0]).clusters_ = newArray;
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
        return solution;
    }

}
