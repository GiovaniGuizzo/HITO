package jmetal.base.operator.mutation;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import jmetal.base.Cluster;
import jmetal.base.Solution;
import jmetal.base.variable.*;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.problems.CITO_CAITO;

public class InterSwapMutation extends Mutation {

    //--------------------------------------------------------------------------
    public InterSwapMutation() {
    }

    //--------------------------------------------------------------------------
    public InterSwapMutation(Properties properties) {
        this();
    }

    //--------------------------------------------------------------------------
    public void doMutation(double probability, Solution solution) throws JMException {
        mutationInterCluster(probability, solution);
    }

    //--------------------------------------------------------------------------
    public void mutationIntraCluster(double probability, Solution solution) throws JMException {
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {

                int clusterToMutate;
                int clustersLength;
                int permutationLength;
                ArrayList<Integer> permutation;

                clustersLength = ((Permutation) solution.getDecisionVariables()[0]).clusters_.size();
                clusterToMutate = PseudoRandom.randInt(0, clustersLength - 1);
                permutation = ((Permutation) solution.getDecisionVariables()[0]).getCluster(clusterToMutate).modules;
                permutationLength = permutation.size();

                if (PseudoRandom.randDouble() < probability) {
                    int pos1 = PseudoRandom.randInt(0, permutationLength - 1);
                    int pos2 = PseudoRandom.randInt(0, permutationLength - 1);

                    if ((permutationLength - 1) > 0) {
                        while (pos1 == pos2) {
                            pos2 = PseudoRandom.randInt(0, permutationLength - 1);
                        }
                    }

                    // swap
                    int temp = permutation.get(pos1);
                    permutation.set(pos1, permutation.get(pos2));
                    permutation.set(pos2, temp);
                }
            } else {
                Configuration.logger_.log(
                        Level.SEVERE, "SwapMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    public void mutationInterCluster(double probability, Solution solution) throws JMException {
        try {
            if (solution.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {

                int clustersLength;
                ArrayList<Cluster> permutation;

                clustersLength = ((Permutation) solution.getDecisionVariables()[0]).clusters_.size();
                permutation = ((Permutation) solution.getDecisionVariables()[0]).clusters_;

                if (PseudoRandom.randDouble() < probability) {

                    int pos1 = PseudoRandom.randInt(0, clustersLength - 1);
                    int pos2 = PseudoRandom.randInt(0, clustersLength - 1);

                    if ((clustersLength - 1) > 0) {
                        while (pos1 == pos2) {
                            pos2 = PseudoRandom.randInt(0, clustersLength - 1);
                        }
                    }

                    // swap
                    Cluster temp = permutation.get(pos1);
                    permutation.set(pos1, permutation.get(pos2));
                    permutation.set(pos2, temp);
                }
            } else {
                Configuration.logger_.log(
                        Level.SEVERE, "SwapMutation.doMutation: invalid type. "
                        + "{0}", solution.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    public Object execute(Object object, CITO_CAITO problem) throws JMException {
        Solution solution = (Solution) object;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("SwapMutation.execute: probability not specified");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        this.doMutation(probability.doubleValue(), solution);

        //rever as restricoes---------------------------------------------------
        solution = problem.tratarRestricoes(solution, problem.getConstraintMatrix());

        return solution;
    }
    //--------------------------------------------------------------------------
}
