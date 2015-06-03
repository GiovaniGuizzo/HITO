//  NSGAII.java
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
package hyperheuristics.algorithm;

import hyperheuristics.comparators.LowLevelHeuristicComparatorFactory;
import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.comparator.CrowdingComparator;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.problems.CITO_CAITO;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/**
 * Implementation of SPEA2.
 */
public class SPEA2Hyperheuristic extends Algorithm {

    public static final int TOURNAMENTS_ROUNDS = 1;

    private final List<LowLevelHeuristic> lowLevelHeuristics;
    private final CITO_CAITO problem_;
    private FileWriter lowLevelHeuristicsRankWriter;
    private FileWriter lowLevelHeuristicsTimeWriter;
    private FileWriter qDebugWriter;
    private FileWriter auxDebugWriter;
    private FileWriter nDebugWriter;
    private FileWriter rDebugWriter;
    private String generationsOutputDirectory;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public SPEA2Hyperheuristic(CITO_CAITO problem) {
        this.lowLevelHeuristics = new ArrayList<>();
        this.problem_ = problem;
    } // NSGAII

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
                writer.write("Name: " + lowLevelHeuristic.getName() + ":\n");
                writer.write("\tRank: " + lowLevelHeuristic.getRank() + "\n");
                writer.write("\tElapsed Time: " + lowLevelHeuristic.getElapsedTime() + "\n");
                writer.write("\tChoice Value: " + lowLevelHeuristic.getChoiceFunctionValue() + "\n");
                writer.write("\tNumber of Times Applied: " + lowLevelHeuristic.getNumberOfTimesApplied() + "\n");
                writer.write("\n");
            }
            writer.write("----------------------\n\n");
        } catch (IOException ex) {
            Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Runs the SPEA2 algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions as a result of the algorithm execution
     *
     * @throws JMException
     * @throws java.lang.ClassNotFoundException
     */
    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int archiveSize;
        int maxEvaluations;
        int evaluations;
        Comparator<LowLevelHeuristic> heuristicFunctionComparator;

        QualityIndicator indicators; // QualityIndicator object
        // indicators object (see below)

        SolutionSet population;
        SolutionSet archive;
        SolutionSet offspringPopulation;

        Distance distance = new Distance();

        //Read the parameters
        populationSize = ((Integer) getInputParameter("populationSize"));
        archiveSize = ((Integer) getInputParameter("archiveSize"));
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations"));
        String heuristicFunction = (String) getInputParameter("heuristicFunction");
        heuristicFunctionComparator = LowLevelHeuristicComparatorFactory.createComparator(heuristicFunction);
        indicators = (QualityIndicator) getInputParameter("indicators");

        //Get the selection operator
        Operator selectionOperator = operators_.get("selection");

        //Initialize the variables
        population = new SolutionSet(populationSize);
        archive = new SolutionSet(archiveSize);
        evaluations = 0;

        for (int i = 0; i < populationSize; i++) {
            Solution newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            population.add(newSolution);
        }

        int generation = 0;
        // Generations
        while (evaluations < maxEvaluations) {
            generation++;
            // Create the offSpring solutionSet
            SolutionSet union = ((SolutionSet) population).union(archive);
            Spea2Fitness spea = new Spea2Fitness(union);
            spea.fitnessAssign();
            archive = spea.environmentalSelection(archiveSize);
            // Create a new offspringPopulation
            offspringPopulation = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            for (int i = 0; i < populationSize;) {
                //Get the best hyperheuristics
                LowLevelHeuristic applyingHeuristic = getApplyingHeuristic(heuristicFunctionComparator);

                int j = 0;
                do {
                    j++;
                    parents[0] = (Solution) selectionOperator.execute(archive, null);
                } while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while                    
                int k = 0;
                do {
                    k++;
                    parents[1] = (Solution) selectionOperator.execute(archive, null);
                } while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while

                Solution[] offSpring = (Solution[]) applyingHeuristic.execute(parents, problem_);
                offSpring = new Solution[]{offSpring[0]};

                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);

                //Update rank
                applyingHeuristic.updateRank(parents, offSpring);
                if (LowLevelHeuristic.MULTI_ARMED_BANDIT.equals(heuristicFunction)) {
                    applyingHeuristic.creditAssignment(lowLevelHeuristics);
                }

                offspringPopulation.add(offSpring[0]);

                i++;
                evaluations += offSpring.length;

                if (qDebugWriter != null) {
                    try {
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            qDebugWriter.write(lowLevelHeuristic.getQ() + "\t");
                        }
                        qDebugWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (auxDebugWriter != null) {
                    try {
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            auxDebugWriter.write(lowLevelHeuristic.getAux() + "\t");
                        }
                        auxDebugWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (rDebugWriter != null) {
                    try {
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            rDebugWriter.write(lowLevelHeuristic.getR() + "\t");
                        }
                        rDebugWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (nDebugWriter != null) {
                    try {
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            nDebugWriter.write(lowLevelHeuristic.getN() + "\t");
                        }
                        nDebugWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Update time elapsed from heuristics not executed
                for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                    if (!lowLevelHeuristic.equals(applyingHeuristic)) {
                        lowLevelHeuristic.notExecuted();
                    }
                }

                if (lowLevelHeuristicsRankWriter != null) {
                    try {
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            lowLevelHeuristicsRankWriter.write(lowLevelHeuristic.getRank() + "\t");
                        }
                        lowLevelHeuristicsRankWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (lowLevelHeuristicsTimeWriter != null) {
                    try {
                        //System.out.println("Escrevendo em : "+lowLevelHeuristicsTimeWriter.);
                        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
                            lowLevelHeuristicsTimeWriter.write(lowLevelHeuristic.getElapsedTime() + "\t");
                        }
                        lowLevelHeuristicsTimeWriter.write("\n");
                    } catch (IOException ex) {
                        Logger.getLogger(SPEA2Hyperheuristic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } // for

            population = offspringPopulation;

            if (generationsOutputDirectory != null) {
                population.printObjectivesToFile(generationsOutputDirectory + "/GEN_" + generation + ".txt");
            }

        } // while

        Ranking ranking = new Ranking(archive);
        return ranking.getSubfront(0);
    } // execute

    private LowLevelHeuristic getApplyingHeuristic(Comparator<LowLevelHeuristic> comparator) {
        if (getInputParameter("heuristicFunction").equals(LowLevelHeuristic.RANDOM) || comparator == null) {
            return lowLevelHeuristics.get(PseudoRandom.randInt(0, lowLevelHeuristics.size() - 1));
        } else {
            List<LowLevelHeuristic> allLowLevelHeuristics = new ArrayList<>(lowLevelHeuristics);
            Collections.sort(allLowLevelHeuristics, comparator);
            List<LowLevelHeuristic> applyingHeuristics = new ArrayList<>();

            //Find the best tied heuristics
            Iterator<LowLevelHeuristic> iterator = allLowLevelHeuristics.iterator();
            LowLevelHeuristic heuristic;
            LowLevelHeuristic nextHeuristic = iterator.next();
            do {
                heuristic = nextHeuristic;
                applyingHeuristics.add(heuristic);
            } while (iterator.hasNext() && comparator.compare(heuristic, nextHeuristic = iterator.next()) == 0);

            return applyingHeuristics.get(PseudoRandom.randInt(0, applyingHeuristics.size() - 1));
        }
    }
} // NSGA-II
