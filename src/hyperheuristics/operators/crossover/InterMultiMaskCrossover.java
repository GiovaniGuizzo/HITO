/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.operators.crossover;

import java.util.ArrayList;
import jmetal.base.Cluster;
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
public class InterMultiMaskCrossover extends Crossover {

    public Solution[] doCrossover(double probability,
            Solution parent1,
            Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];

        Solution[] interclusterOffspring = intercluster(parent1, parent2, probability);
        offspring[0] = interclusterOffspring[0];
        offspring[1] = interclusterOffspring[1];
        
        return offspring;
    } // doCrossover
    
    public Solution[] intercluster(Solution parent1, Solution parent2, double probability) throws JMException {
        Solution[] offspring = new Solution[2];

        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        try {
            if (parent1.getDecisionVariables()[0].getVariableType().equals(Class.forName("jmetal.base.variable.Permutation"))) {
                if (PseudoRandom.randDouble() < probability) {
                    int permutationLength = ((Permutation) parent1.getDecisionVariables()[0]).clusters_.size();

                    ArrayList<Cluster> parent1Vector = ((Permutation) parent1.getDecisionVariables()[0]).clusters_;
                    ArrayList<Cluster> parent2Vector = ((Permutation) parent2.getDecisionVariables()[0]).clusters_;
                    ArrayList<Cluster> offspring1Vector = new ArrayList<>(parent1Vector.size());
                    for (int i = 0; i < parent1Vector.size(); i++) {
                        offspring1Vector.add(null);
                    }
                    ArrayList<Cluster> offspring2Vector = new ArrayList<>(parent2Vector.size());
                    for (int i = 0; i < parent2Vector.size(); i++) {
                        offspring2Vector.add(null);
                    }

                    int[] maskArray = getMaskArray(permutationLength);
                    for (int i = 0; i < maskArray.length; i++) {
                        int mask = maskArray[i];

                        Cluster value1;
                        Cluster value2;

                        if (mask == 1) {
                            value1 = parent1Vector.get(i);
                            value2 = parent2Vector.get(i);
                        } else {
                            value1 = parent2Vector.get(i);
                            value2 = parent1Vector.get(i);
                        }

                        if (!offspring1Vector.contains(value1)) {
                            Cluster tempCluster = new Cluster();
                            tempCluster.id = value1.id;
                            tempCluster.modules = new ArrayList<>(value1.modules);
                            offspring1Vector.set(i, tempCluster);
                        }

                        if (!offspring2Vector.contains(value2)) {
                            Cluster tempCluster = new Cluster();
                            tempCluster.id = value2.id;
                            tempCluster.modules = new ArrayList<>(value2.modules);
                            offspring2Vector.set(i, tempCluster);
                        }
                    }

                    fulfilArrayClusters(offspring1Vector, parent1Vector);
                    fulfilArrayClusters(offspring2Vector, parent2Vector);
                    ((Permutation) offspring[0].getDecisionVariables()[0]).clusters_ = offspring1Vector;
                    ((Permutation) offspring[1].getDecisionVariables()[0]).clusters_ = offspring2Vector;

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
    }

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
        offspring[0] = problem.tratarRestricoes(offspring[0], problem.getConstraintMatrix());
        offspring[1] = problem.tratarRestricoes(offspring[1], problem.getConstraintMatrix());

        return offspring;
    } // execute

    private int[] getMaskArray(int length) {
        int[] mask = new int[length];
        for (int i = 0; i < length; i++) {
            mask[i] = PseudoRandom.randInt(0, 1);
        }
        return mask;
    }

    private void fulfilArrayClusters(ArrayList<Cluster> offspring, ArrayList<Cluster> parent) {
        int parentIndex = 0;
        for (int i = 0; i < parent.size(); i++) {
            Cluster value = offspring.get(i);
            if (value == null) {
                Cluster parentValue;
                do {
                    parentValue = parent.get(parentIndex);
                    parentIndex++;
                } while (offspring.contains(parentValue) && parentIndex < parent.size());
                Cluster tempCluster = new Cluster();
                tempCluster.id = parentValue.id;
                tempCluster.modules = new ArrayList<>(parentValue.modules);
                offspring.set(i, tempCluster);
            }
        }
    }
    
    private void fulfilArrayModules(ArrayList<Integer> offspring, ArrayList<Integer> parent) {
        int parentIndex = 0;
        for (int i = 0; i < parent.size(); i++) {
            Integer value = offspring.get(i);
            if (value == -1) {
                Integer parentValue;
                do {
                    parentValue = parent.get(parentIndex);
                    parentIndex++;
                } while (offspring.contains(parentValue) && parentIndex < parent.size());
                offspring.set(i, parentValue);
            }
        }
    }

}
