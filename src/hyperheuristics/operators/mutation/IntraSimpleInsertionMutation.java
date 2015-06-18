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
public class IntraSimpleInsertionMutation extends Mutation {

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
        intracluster(solution);
    }

    private Solution intracluster(Solution solution) {
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {
                ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;
                Cluster cluster = clusters.get(PseudoRandom.randInt(0, clusters.size() - 1));
                int length = cluster.modules.size();

                int fromPosition = PseudoRandom.randInt(0, length - 1);
                Integer value = cluster.modules.get(fromPosition);

                int toPosition;
                do {
                    toPosition = PseudoRandom.randInt(0, length - 1);
                } while (fromPosition == toPosition);

                ArrayList<Integer> newArray = new ArrayList<>();
                for (int i = 0; i < cluster.modules.size(); i++) {
                    newArray.add(null);
                }

                int i;
                if (fromPosition > toPosition) {
                    for (i = 0; i < toPosition; i++) {
                        newArray.set(i, cluster.modules.get(i));
                    }
                    newArray.set(i, value);
                    for (i++; i <= fromPosition; i++) {
                        newArray.set(i, cluster.modules.get(i - 1));
                    }
                    for (; i < length; i++) {
                        newArray.set(i, cluster.modules.get(i));
                    }
                } else {
                    for (i = 0; i < fromPosition; i++) {
                        newArray.set(i, cluster.modules.get(i));
                    }
                    for (; i < toPosition; i++) {
                        newArray.set(i, cluster.modules.get(i + 1));
                    }
                    newArray.set(i, value);
                    for (i++; i < length; i++) {
                        newArray.set(i, cluster.modules.get(i));
                    }
                }

                cluster.modules = newArray;
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
