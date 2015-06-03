/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.operators.crossover;

import java.util.Arrays;
import jmetal.base.Solution;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.variable.Permutation;
import jmetal.problems.CITO_CAITO;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author giovaniguizzo
 */
public class MultiMaskCrossover extends Crossover {

    public Solution[] doCrossover(double probability,
            Solution parent1,
            Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];

        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        try {
            if (parent1.getDecisionVariables()[0].getVariableType().equals(Class.forName("jmetal.base.variable.Permutation"))) {
                if (PseudoRandom.randDouble() < probability) {
                    int permutationLength = ((Permutation) parent1.getDecisionVariables()[0]).getLength();

                    int parent1Vector[] = ((Permutation) parent1.getDecisionVariables()[0]).vector_;
                    int parent2Vector[] = ((Permutation) parent2.getDecisionVariables()[0]).vector_;
                    int offspring1Vector[] = ((Permutation) offspring[0].getDecisionVariables()[0]).vector_;
                    Arrays.fill(offspring1Vector, -1);
                    int offspring2Vector[] = ((Permutation) offspring[1].getDecisionVariables()[0]).vector_;
                    Arrays.fill(offspring2Vector, -1);

                    int[] maskArray = getMaskArray(permutationLength);
                    for (int i = 0; i < maskArray.length; i++) {
                        int mask = maskArray[i];

                        int value1;
                        int value2;

                        if (mask == 1) {
                            value1 = parent1Vector[i];
                            value2 = parent2Vector[i];
                        } else {
                            value1 = parent2Vector[i];
                            value2 = parent1Vector[i];
                        }

                        if (!arrayContains(offspring1Vector, value1)) {
                            offspring1Vector[i] = value1;
                        }

                        if (!arrayContains(offspring2Vector, value2)) {
                            offspring2Vector[i] = value2;
                        }
                    }

                    fulfilArray(offspring1Vector, parent1Vector);
                    fulfilArray(offspring2Vector, parent2Vector);

                } // if
            } else {
                Configuration.logger_.severe("MultiMaskCrossover.doCrossover: invalid type+"
                        + "" + parent1.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doCrossover()");
            } // else
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // if
        return offspring;
    } // doCrossover

    /**
     * Executes the operation
     *
     * @param object An object containing an array of two solutions
     * @throws JMException
     */
    public Object execute(Object object, CITO_CAITO problem) throws JMException {
        Solution[] parents = (Solution[]) object;
        Double crossoverProbability;

        crossoverProbability = (Double) getParameter("probability");

        if (parents.length < 2) {
            Configuration.logger_.severe("MultiMaskCrossover.execute: operator needs two "
                    + "parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } else if (crossoverProbability == null) {
            Configuration.logger_.severe("MultiMaskCrossover.execute: probability not "
                    + "specified");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        Solution[] offspring = doCrossover(crossoverProbability.doubleValue(), parents[0], parents[1]);

        //rever as restricoes---------------------------------------------------
        int offspring1Vector[] = ((Permutation) offspring[0].getDecisionVariables()[0]).vector_;
        offspring1Vector = problem.tratarRestricoes(offspring1Vector, problem.getConstraintMatrix());

        //rever as restricoes---------------------------------------------------
        int offspring2Vector[] = ((Permutation) offspring[1].getDecisionVariables()[0]).vector_;
        offspring2Vector = problem.tratarRestricoes(offspring2Vector, problem.getConstraintMatrix());

        return offspring;
    } // execute

    private int[] getMaskArray(int length) {
        int[] mask = new int[length];
        for (int i = 0; i < length; i++) {
            mask[i] = PseudoRandom.randInt(0, 1);
        }
        return mask;
    }

    private boolean arrayContains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    private void fulfilArray(int[] offspring, int[] parent) {
        int parentIndex = 0;
        for (int i = 0; i < parent.length; i++) {
            int value = offspring[i];
            if (value == -1) {
                int parentValue;
                do {
                    parentValue = parent[parentIndex];
                    parentIndex++;
                } while (arrayContains(offspring, parentValue) && parentIndex < parent.length);
                offspring[i] = parentValue;
            }
        }
    }

}
