/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.Random;
import java.util.Vector;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.metaheuristics.moead.Utils;
import jmetal.problems.CITO_CAITO;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author vinicius
 */
public class MOEAD_GR_FRRMAB extends MOEAD_FRRMAB{

    protected int generation;
    protected int maxgeneration;
    
    public MOEAD_GR_FRRMAB(CITO_CAITO problem) {
        super(problem);
    }
    
    //overhide it to create AGR
    protected int selectNrValue(){
        return this.nr_;
    }
    
    protected int findMostSuitableProblem(Solution x){
        double betterFitness=Double.POSITIVE_INFINITY;
        int chosen=-1;
        for (int k = 0; k < populationSize_; k++) {
            double value=fitnessFunction(x, lambda_[k]);
            if(value < betterFitness){
                betterFitness=value;
                chosen=k;
            }
        }
        return chosen;
    }
    
    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        //MOEAD Init    
        this.initializeParameters();
        this.generation=0;
        this.maxgeneration=this.maxEvaluations/this.populationSize_;
        //MAB init
        this.initializeHH();
        Random rand=new Random();
        // STEP 1. Initialization
        // STEP 1.1. Compute euclidean distances between weight vectors and find T
        initUniformWeight();
        initNeighborhood();
        // STEP 1.2. Initialize population
        initPopulation();
        // STEP 1.3. Initialize z_
        initIdealPoint();
        // STEP 2. Update
        do {
            int[] permutation = new int[populationSize_];
            Utils.randomPermutation(permutation, populationSize_);
            this.generation++;
            //Select Nr value if applicable
            this.nr_=this.selectNrValue();
            //System.out.println(this.generation+" "+nr_);
            for (int i = 0; i < populationSize_; i++) {
                int n = permutation[i]; // or int n = i;
                //int n = i ; // or int n = i;
                int type;
                double rnd = PseudoRandom.randDouble();

                // STEP 2.1. Mating selection based on probability
                if (rnd < delta_) // if (rnd < realb)    
                {
                    type = 1;   // neighborhood
                } else {
                    type = 2;   // whole population
                }
                Vector<Integer> p = new Vector<Integer>();
                matingSelection(p, n, 1, type);

                //select LOW LEVEL HEURISTIC
                LowLevelHeuristic op = selector.selectOperator(frr, nt);
                // STEP 2.2. Reproduction
                Solution[] parents = new Solution[2];

                parents[0] = population_.get(p.get(0));
                parents[1] = population_.get(n);

                Solution[] offSpring = (Solution[]) op.execute(parents, problem_);
                
                int randompos=rand.nextInt(offSpring.length);
                Solution generated=offSpring[randompos];
                problem_.evaluate(generated);
                problem_.evaluateConstraints(generated);
                evaluations_++;
                //FIND MOST SUITABLE SUBPROBLEM
                int suitableSubProblem=this.findMostSuitableProblem(generated);
                
                // STEP 2.4. Update z_
                updateReference(generated);
                // STEP 2.5. Update of solutions
                updateProblem(op, generated, suitableSubProblem, type);

                //MAB tasks
                sl.addItem(op.getName(), fir.get(op.getName()), this.nt);
                frr = credit.calcFRR(sl);

            } // for 
        } while (evaluations_ < maxEvaluations);

        return population_;
    }
}
