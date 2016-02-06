//  MOEAD.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package hyperheuristics.algorithm.moeadfrrcf;

import hyperheuristics.algorithm.NSGAIIHyperheuristic;
import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import jmetal.metaheuristics.moead.*;
import jmetal.base.*;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.problems.CITO_CAITO;

public class MOEAD_FRRCF extends Algorithm {

    private int populationSize_;
    /**
     * Stores the population
     */
    private SolutionSet population_;
    /**
     * Z vector (ideal point)
     */
    double[] z_;
    /**
     * Lambda vectors
     */
    //Vector<Vector<Double>> lambda_ ; 
    double[][] lambda_;
    /**
     * T: neighbour size
     */
    int T_;
    /**
     * Neighborhood
     */
    int[][] neighborhood_;
    /**
     * delta: probability that parent solutions are selected from neighbourhood
     */
    double delta_;
    /**
     * nr: maximal number of solutions replaced by each child solution
     */
    int nr_;
    Solution[] indArray_;
    String functionType_;
    int evaluations_;
    /**
     * Operators
     */
    Operator crossover_;
    Operator mutation_;

    String dataDirectory_;

    private CITO_CAITO problem_;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     *
     *
     *
     */
    private FileWriter lowLevelHeuristicsRankWriter;
    private FileWriter lowLevelHeuristicsTimeWriter;
    private FileWriter qDebugWriter;
    private FileWriter auxDebugWriter;
    private FileWriter rDebugWriter;
    private FileWriter nDebugWriter;
    private String generationsOutputDirectory;

    protected HashMap<String, Double> fir;
    protected HashMap<String, Integer> estimatedTime;
    protected HashMap<String, Double> frr;
    protected Selector selector;
    protected CreditAssignment frr_calc;

    private final ArrayList<LowLevelHeuristic> lowLevelHeuristics;

    private int tipo;

    public MOEAD_FRRCF(CITO_CAITO problem) {
        this.problem_ = problem;
        functionType_ = "_TCHE1";
        this.lowLevelHeuristics = new ArrayList<>();
    } // DMOEA

    /*
     ---------------------------------------------------------------------------------------------------------
     */
    public LowLevelHeuristic addLowLevelHeuristic(HashMap<String, Object> parameters) {
        LowLevelHeuristic lowLevelHeuristic = new LowLevelHeuristic(parameters);
        if (!lowLevelHeuristics.contains(lowLevelHeuristic)) {
            lowLevelHeuristics.add(lowLevelHeuristic);
            return lowLevelHeuristic;
        } else {
            return null;
        }
    }

    public LowLevelHeuristic removeLowLevelHeuristic(String name) {
        for (int i = 0; i < lowLevelHeuristics.size(); i++) {
            LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
            if (lowLevelHeuristic.getName().equals(name)) {
                return lowLevelHeuristics.remove(i);
            }
        }
        return null;
    }

    public void clearLowLeverHeuristics() {
        lowLevelHeuristics.clear();
    }

    public void clearLowLeverHeuristicsValues() {
        LowLevelHeuristic.clearAllStaticValues();
        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
            lowLevelHeuristic.clearAllValues();
        }
    }

    public int[] getLowLevelHeuristicsNumberOfTimesApplied() {
        int[] allTimesApplied = new int[lowLevelHeuristics.size()];
        for (int i = 0; i < lowLevelHeuristics.size(); i++) {
            LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
            allTimesApplied[i] = lowLevelHeuristic.getNumberOfTimesApplied();
        }
        return allTimesApplied;
    }

    public int getLowLevelHeuristicsSize() {
        return lowLevelHeuristics.size();
    }
    //----------------------------------------------------------------------------

    public void setLowLevelHeuristicsRankPath(String path) throws IOException {
        if (lowLevelHeuristicsRankWriter != null) {
            lowLevelHeuristicsRankWriter.close();
        }
        lowLevelHeuristicsRankWriter = new FileWriter(path);
    }

    public void setLowLevelHeuristicsTimePath(String path) throws IOException {
        if (lowLevelHeuristicsTimeWriter != null) {
            lowLevelHeuristicsTimeWriter.close();
        }
        lowLevelHeuristicsTimeWriter = new FileWriter(path);
    }

    public void setDebugPath(String path) throws IOException {
        if (qDebugWriter != null) {
            qDebugWriter.close();
        }
        qDebugWriter = new FileWriter(path + "_q.txt");
        if (auxDebugWriter != null) {
            auxDebugWriter.close();
        }
        auxDebugWriter = new FileWriter(path + "_aux.txt");
        if (rDebugWriter != null) {
            rDebugWriter.close();
        }
        rDebugWriter = new FileWriter(path + "_r.txt");
        if (nDebugWriter != null) {
            nDebugWriter.close();
        }
        nDebugWriter = new FileWriter(path + "_n.txt");
    }

    public void setGenerationsOutputDirectory(String path) throws IOException {
        generationsOutputDirectory = path;
    }

    public void printLowLevelHeuristicsInformation(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                writer.append("Name: " + lowLevelHeuristic.getName() + ":\n");
                writer.append("\tRank: " + lowLevelHeuristic.getRank() + "\n");
                writer.append("\tElapsed Time: " + lowLevelHeuristic.getElapsedTime() + "\n");
                writer.append("\tChoice Value: " + lowLevelHeuristic.getChoiceFunctionValue() + "\n");
                writer.append("\tNumber of Times Applied: " + lowLevelHeuristic.getNumberOfTimesApplied() + "\n");
                writer.append("\n");
            }
            writer.append("----------------------\n\n");
        } catch (IOException ex) {
            Logger.getLogger(NSGAIIHyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeLowLevelHeuristicsRankPath() throws IOException {
        if (lowLevelHeuristicsRankWriter != null) {
            lowLevelHeuristicsRankWriter.close();
        }
    }

    public void closeDebugPath() throws IOException {
        if (qDebugWriter != null) {
            qDebugWriter.close();
        }
        if (auxDebugWriter != null) {
            auxDebugWriter.close();
        }
        if (rDebugWriter != null) {
            rDebugWriter.close();
        }
        if (nDebugWriter != null) {
            nDebugWriter.close();
        }
    }

    public void closeLowLevelHeuristicsTimePath() throws IOException {
        if (lowLevelHeuristicsTimeWriter != null) {
            lowLevelHeuristicsTimeWriter.close();
        }
    }

    /*
     ---------------------------------------------------------------------------------------------------------
     */
    protected void initializeHH() {
        double alpha = ((Double) this.getInputParameter("alpha")).doubleValue();
        double beta = ((Double) this.getInputParameter("beta")).doubleValue();
        double D = ((Double) this.getInputParameter("d")).doubleValue();
        this.tipo = ((Integer) this.getInputParameter("tipo")).intValue();

        fir = new HashMap<>();
        estimatedTime = new HashMap<>();
        frr = new HashMap<>();
        for (LowLevelHeuristic op : this.lowLevelHeuristics) {
            estimatedTime.put(op.getName(), 0);
            frr.put(op.getName(), -1.0);
            fir.put(op.getName(), -1.0);
        }
        selector = new Selector(this.lowLevelHeuristics, alpha, beta);
        frr_calc = new CreditAssignment(D);
    }

    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int maxEvaluations;

        evaluations_ = 0;
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
        populationSize_ = ((Integer) this.getInputParameter("populationSize")).intValue();
        dataDirectory_ = this.getInputParameter("dataDirectory").toString();
        //System.out.println("POPSIZE: " + populationSize_);

        population_ = new SolutionSet(populationSize_);
        indArray_ = new Solution[problem_.getNumberOfObjectives()];

        T_ = ((Integer) this.getInputParameter("T")).intValue();
        nr_ = ((Integer) this.getInputParameter("nr")).intValue();
        delta_ = ((Double) this.getInputParameter("delta")).doubleValue();


        /*
         T_ = (int) (0.1 * populationSize_);
         delta_ = 0.9;
         nr_ = (int) (0.01 * populationSize_);
         */
        neighborhood_ = new int[populationSize_][T_];

        z_ = new double[problem_.getNumberOfObjectives()];
        //lambda_ = new Vector(problem_.getNumberOfObjectives()) ;
        lambda_ = new double[populationSize_][problem_.getNumberOfObjectives()];

        crossover_ = operators_.get("crossover"); // default: DE crossover
        mutation_ = operators_.get("mutation");  // default: polynomial mutation
        Random rand=new Random();
        //MAB init
        this.initializeHH();

        // STEP 1. Initialization
        // STEP 1.1. Compute euclidean distances between weight vectors and find T
        initUniformWeight();
        //for (int i = 0; i < 300; i++)
        // 	System.out.println(lambda_[i][0] + " " + lambda_[i][1]) ;

        initNeighborhood();

        // STEP 1.2. Initialize population
        initPopulation();

        // STEP 1.3. Initialize z_
        initIdealPoint();

        // STEP 2. Update
        do {
            int[] permutation = new int[populationSize_];
            Utils.randomPermutation(permutation, populationSize_);

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
                LowLevelHeuristic op = selector.selectOperator(frr, estimatedTime);
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
                // STEP 2.3. Repair. Not necessary
                // STEP 2.4. Update z_
                updateReference(generated);
                // STEP 2.5. Update of solutions
                updateProblem(op, generated, n, type);
                this.updateFRR();
            } // for 
        } while (evaluations_ < maxEvaluations);

        return population_;
    }

    /**
     * initUniformWeight
     */
    public void initUniformWeight() {
        if ((problem_.getNumberOfObjectives() == 2) && (populationSize_ <= 300)) {
            for (int n = 0; n < populationSize_; n++) {
                double a = 1.0 * n / (populationSize_ - 1);
                lambda_[n][0] = a;
                lambda_[n][1] = 1 - a;
            } // for
        } // if
        else {
            String dataFileName;
            dataFileName = "W" + problem_.getNumberOfObjectives() + "D_"
                    + populationSize_ + ".dat";

            try {
                // Open the file
                FileInputStream fis = new FileInputStream(dataDirectory_ + "/" + dataFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                int numberOfObjectives = 0;
                int i = 0;
                int j = 0;
                String aux = br.readLine();
                while (aux != null) {
                    StringTokenizer st = new StringTokenizer(aux);
                    j = 0;
                    numberOfObjectives = st.countTokens();
                    while (st.hasMoreTokens()) {
                        double value = (new Double(st.nextToken())).doubleValue();
                        lambda_[i][j] = value;
                        //System.out.println("lambda["+i+","+j+"] = " + value) ;
                        j++;
                    }
                    aux = br.readLine();
                    i++;
                }
                br.close();
            } catch (Exception e) {
                System.out.println("initUniformWeight: failed when reading for file: " + dataDirectory_ + "/" + dataFileName);
                e.printStackTrace();
            }
        } // else

        //System.exit(0) ;
    } // initUniformWeight

    /**
     *
     */
    public void initNeighborhood() {
        double[] x = new double[populationSize_];
        int[] idx = new int[populationSize_];

        for (int i = 0; i < populationSize_; i++) {
            // calculate the distances based on weight vectors
            for (int j = 0; j < populationSize_; j++) {
                x[j] = Utils.distVector(lambda_[i], lambda_[j]);
                //x[j] = dist_vector(population[i].namda,population[j].namda);
                idx[j] = j;
                //System.out.println("x["+j+"]: "+x[j]+ ". idx["+j+"]: "+idx[j]) ;
            } // for

            // find 'niche' nearest neighboring subproblems
            Utils.minFastSort(x, idx, populationSize_, T_);
            //minfastsort(x,idx,population.size(),niche);

            System.arraycopy(idx, 0, neighborhood_[i], 0, T_);
        } // for
    } // initNeighborhood

    /**
     *
     */
    public void initPopulation() throws JMException, ClassNotFoundException {
        for (int i = 0; i < populationSize_; i++) {
            Solution newSolution = new Solution(problem_);

            problem_.evaluate(newSolution);
            evaluations_++;
            population_.add(newSolution);
        } // for
    } // initPopulation

    /**
     *
     */
    void initIdealPoint() throws JMException, ClassNotFoundException {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            z_[i] = 1.0e+30;
            indArray_[i] = new Solution(problem_);
            problem_.evaluate(indArray_[i]);
            evaluations_++;
        } // for

        for (int i = 0; i < populationSize_; i++) {
            updateReference(population_.get(i));
        } // for
    } // initIdealPoint

    /**
     *
     */
    public void matingSelection(Vector<Integer> list, int cid, int size, int type) {
        // list : the set of the indexes of selected mating parents
        // cid  : the id of current subproblem
        // size : the number of selected mating parents
        // type : 1 - neighborhood; otherwise - whole population
        int ss;
        int r;
        int p;

        ss = neighborhood_[cid].length;
        while (list.size() < size) {
            if (type == 1) {
                r = PseudoRandom.randInt(0, ss - 1);
                p = neighborhood_[cid][r];
                //p = population[cid].table[r];
            } else {
                p = PseudoRandom.randInt(0, populationSize_ - 1);
            }
            boolean flag = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == p) // p is in the list
                {
                    flag = false;
                    break;
                }
            }

            //if (flag) list.push_back(p);
            if (flag) {
                list.addElement(p);
            }
        }
    } // matingSelection

    /**
     *
     * @param individual
     */
    void updateReference(Solution individual) {
        for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
            if (individual.getObjective(n) < z_[n]) {
                z_[n] = individual.getObjective(n);

                indArray_[n] = individual;
            }
        }
    } // updateReference

    /**
     * @param individual
     * @param id
     * @param type
     */
    void updateProblem(LowLevelHeuristic op, Solution indiv, int id, int type) {
        double rank = 0.0;
        //double parentsLenght=2;
        //double offspringLenght=1;
        // indiv: child solution
        // id:   the id of current subproblem
        // type: update solutions in - neighborhood (1) or whole population (otherwise)
        int size;
        int time;

        time = 0;

        if (type == 1) {
            size = neighborhood_[id].length;
        } else {
            size = population_.size();
        }
        int[] perm = new int[size];

        Utils.randomPermutation(perm, size);

        for (int i = 0; i < size; i++) {
            int k;
            if (type == 1) {
                k = neighborhood_[id][perm[i]];
            } else {
                k = perm[i];      // calculate the values of objective function regarding the current subproblem
            }
            double f1, f2;

            f1 = fitnessFunction(population_.get(k), lambda_[k]);
            f2 = fitnessFunction(indiv, lambda_[k]);
            double comparator = 0;
            if (f2 < f1) {//f2 c f1 p
                //1
                population_.replace(k, new Solution(indiv));
                //population[k].indiv = indiv;
                time++;
                comparator = 1;
            } else if (f1 < f2) {
                //-1
                comparator = -1;
            }

            rank += comparator;
            // the maximal number of solutions updated is not allowed to exceed 'limit'
            if (time >= nr_) {
                this.updateFIR(rank, op.getName());
                return;
            }
        }
        //rank /= ((double) parentsLenght * (double) offspringLenght);
        //System.out.println(op.getName()+" "+rank);
        this.updateFIR(rank, op.getName());
    } // updateProblem

    private void updateFIR(double val, String opName) {
        if (this.tipo == 1 || this.tipo == 3) {
            fir.put(opName, val);//FIR added //last value
        } else if (this.tipo == 2 || this.tipo == 4) {
            fir.put(opName, this.fir.get(opName) + val);//FIR
        }
    }

    private void updateFRR() {
        if (this.tipo == 1 || this.tipo == 2)//last FIR or Avg FIR
        {
            this.frr = this.fir;
        } else if (this.tipo == 3 || this.tipo == 4)//last FRR and Avg FRR
        {
            this.frr = this.frr_calc.calcFRR(this.fir);
        }
    }

    double fitnessFunction(Solution individual, double[] lambda) {
        double fitness;
        fitness = 0.0;

        if (functionType_.equals("_TCHE1")) {
            double maxFun = -1.0e+30;

            for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
                double diff = Math.abs(individual.getObjective(n) - z_[n]);

                double feval;
                if (lambda[n] == 0) {
                    feval = 0.0001 * diff;
                } else {
                    feval = diff * lambda[n];
                }
                if (feval > maxFun) {
                    maxFun = feval;
                }
            } // for

            fitness = maxFun;
        } // if
        else {
            System.out.println("MOEAD.fitnessFunction: unknown type " + functionType_);
            System.exit(-1);
        }
        return fitness;
    } // fitnessEvaluation
} // MOEAD

