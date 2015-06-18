package jmetal.base.operator.crossover;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import jmetal.base.*;
import jmetal.base.variable.*;
import jmetal.problems.CITO_CAITO;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Configuration.*;

public class IntraTwoPointsCrossover extends Crossover {

    //--------------------------------------------------------------------------
    /**
     * PERMUTATION_SOLUTION represents class jmetal.base.solutionType.PermutationSolutionType
     */
    private static Class PERMUTATION_SOLUTION;

    //--------------------------------------------------------------------------
    public IntraTwoPointsCrossover() {
        try {
            PERMUTATION_SOLUTION = Class.forName("jmetal.base.solutionType.PermutationSolutionType");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    public IntraTwoPointsCrossover(Properties properties) {
        this();
    }

    //--------------------------------------------------------------------------
    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];
        
        Solution[] intraCluster = this.crossoverIntraCluster(probability, parent1, parent2);
        offspring[0] = intraCluster[0];
        offspring[1] = intraCluster[1];

        return offspring;
    }

    //--------------------------------------------------------------------------
    public Solution[] crossoverIntraCluster(double probability, Solution parent1, Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];

        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        try {
            if (parent1.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {
                if (PseudoRandom.randDouble() < probability) {

                    int clusterToCross;
                    int crosspoint1;
                    int crosspoint2;
                    int clustersLength;
                    int permutationLength;
                    ArrayList<Integer> parent1Vector;
                    ArrayList<Integer> parent2Vector;
                    ArrayList<Integer> offspring1Vector;
                    ArrayList<Integer> offspring2Vector;


                    clustersLength = ((Permutation) offspring[0].getDecisionVariables()[0]).clusters_.size();
                    clusterToCross = PseudoRandom.randInt(0, clustersLength - 1);

                    parent1Vector = ((Permutation) offspring[0].getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    parent2Vector = ((Permutation) offspring[1].getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    offspring1Vector = ((Permutation) offspring[0].getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    offspring2Vector = ((Permutation) offspring[1].getDecisionVariables()[0]).getCluster(clusterToCross).modules;

                    permutationLength = parent1Vector.size();


                    // STEP 1: Get cutting points
                    crosspoint1 = PseudoRandom.randInt(0, permutationLength - 1);
                    crosspoint2 = PseudoRandom.randInt(0, permutationLength - 1);

                    if ((permutationLength - 1) > 0) {
                        while (crosspoint2 == crosspoint1) {
                            crosspoint2 = PseudoRandom.randInt(0, permutationLength - 1);
                        }
                    }
                    if (crosspoint1 > crosspoint2) {
                        int swap;
                        swap = crosspoint1;
                        crosspoint1 = crosspoint2;
                        crosspoint2 = swap;
                    }

                    // STEP 2: Obtain the first child
                    int m = 0;
                    for (int j = 0; j < permutationLength; j++) {
                        boolean exist = false;
                        int temp = parent2Vector.get(j);
                        for (int k = crosspoint1; k <= crosspoint2; k++) {
                            if (temp == offspring1Vector.get(k)) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            if (m == crosspoint1) {
                                m = crosspoint2 + 1;
                            }

                            offspring1Vector.set(m++, temp);
                        }
                    }

                    // STEP 3: Obtain the second child
                    m = 0;
                    for (int j = 0; j < permutationLength; j++) {
                        boolean exist = false;
                        int temp = parent1Vector.get(j);
                        for (int k = crosspoint1; k <= crosspoint2; k++) {
                            if (temp == offspring2Vector.get(k)) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            if (m == crosspoint1) {
                                m = crosspoint2 + 1;
                            }
                            offspring2Vector.set(m++, temp);
                        }
                    }
                }
            } else {
                Configuration.logger_.log(
                        Level.SEVERE, "TwoPointsCrossover.doCrossover: "
                        + "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doCrossover()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();            // TODO Auto-generated catch block
        }

        return offspring;
    }

    //--------------------------------------------------------------------------
    public Solution[] crossoverInterCluster(double probability, Solution parent1, Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];

        offspring[0] = new Solution(parent1);       
        offspring[1] = new Solution(parent2);

        try {
            if (parent1.getDecisionVariables()[0].getVariableType() == Class.forName("jmetal.base.variable.Permutation")) {
                if (PseudoRandom.randDouble() < probability) {

                    int clusterToCross;
                    int clustersLength;
                    int permutationLength;
                    ArrayList<Integer> parent1Vector;
                    ArrayList<Integer> parent2Vector;
                    ArrayList<Integer> offspring1Vector;
                    ArrayList<Integer> offspring2Vector;

                    clustersLength = ((Permutation) offspring[0].getDecisionVariables()[0]).clusters_.size();
                    clusterToCross = PseudoRandom.randInt(0, clustersLength - 1);

                    parent1Vector = ((Permutation) parent1.getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    parent2Vector = ((Permutation) parent2.getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    offspring1Vector = ((Permutation) offspring[0].getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    offspring2Vector = ((Permutation) offspring[1].getDecisionVariables()[0]).getCluster(clusterToCross).modules;
                    permutationLength = parent1Vector.size();

                    // STEP 2: Obtain the first child
                    for (int j = 0; j < permutationLength; j++) {
                        offspring1Vector.set(j, parent2Vector.get(j));
                    }

                    // STEP 3: Obtain the second child
                    for (int j = 0; j < permutationLength; j++) {
                        offspring2Vector.set(j, parent1Vector.get(j));
                    }
                }
            } else {
                Configuration.logger_.log(
                        Level.SEVERE, "TwoPointsCrossover.doCrossover: "
                        + "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doCrossover()");
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return offspring;
    }

    //--------------------------------------------------------------------------
    public Object execute(Object object, CITO_CAITO problem) throws JMException {
        Solution[] parents = (Solution[]) object;

        if ((parents[0].getType().getClass() != PERMUTATION_SOLUTION) || (parents[1].getType().getClass() != PERMUTATION_SOLUTION)) {
            Configuration.logger_.log(
                    Level.SEVERE, "TwoPointsCrossover.execute: the solutions "
                    + "are not of the right type. The type should be 'Permutation', but "
                    + "{0} and {1} are obtained", new Object[]{parents[0].getType(), parents[1].getType()});
        }

        Double crossoverProbability = (Double) getParameter("probability");

        if (parents.length < 2) {
            Configuration.logger_.severe("SBXCrossover.execute: operator needs two parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } else if (crossoverProbability == null) {
            Configuration.logger_.severe("SBXCrossover.execute: probability not specified");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        Solution[] offspring = doCrossover(crossoverProbability.doubleValue(), parents[0], parents[1]);

        //rever as restricoes---------------------------------------------------
        offspring[0] = problem.tratarRestricoes(offspring[0], problem.getConstraintMatrix());
        offspring[1] = problem.tratarRestricoes(offspring[1], problem.getConstraintMatrix());

        return offspring;
    }

    //--------------------------------------------------------------------------
}
