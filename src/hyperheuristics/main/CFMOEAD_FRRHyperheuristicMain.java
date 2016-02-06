package hyperheuristics.main;

import hyperheuristics.algorithm.moeadfrrcf.MOEAD_DRA_FRRCF;
import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import jmetal.base.Operator;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.Crossover;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.problems.CITO_CAITO;
import jmetal.problems.Combined2Objetives;
import jmetal.problems.Combined4Objectives;
import jmetal.util.JMException;

public class CFMOEAD_FRRHyperheuristicMain {

    public static void main(String[] args) throws
            JMException,
            SecurityException,
            IOException,
            ClassNotFoundException {

        String[] problems;

        String[] crossovers;

        String[] mutations;

        int executions;
        int populationSize;
        int maxEvaluations;
        int numberOfObjectives;
        double crossoverProbability;
        double mutationProbability;
        double alpha;
        double beta;
        String heuristicFunction;
        int w;
        double c, d;
        int t, nr;
        boolean debug;
        String path;
        int tipo = 0;
        double delta = 0.9;

        if (args.length == 20) {
            populationSize = Integer.parseInt(args[0]);
            maxEvaluations = Integer.parseInt(args[1]);
            crossoverProbability = Double.parseDouble(args[2]);
            mutationProbability = Double.parseDouble(args[3]);
            alpha = Double.parseDouble(args[4]);
            beta = Double.parseDouble(args[5]);
            crossovers = args[6].split(",");
            mutations = args[7].split(",");
            problems = args[8].split(",");
            heuristicFunction = args[9];
            w = Integer.parseInt(args[10]);
            c = Double.parseDouble(args[11]);
            numberOfObjectives = Integer.parseInt(args[12]);
            debug = Boolean.parseBoolean(args[13]);
            executions = Integer.parseInt(args[14]);
            path = args[15];
            t = Integer.parseInt(args[16]);
            nr = Integer.parseInt(args[17]);
            d = Double.parseDouble(args[18]);
            tipo = Integer.parseInt(args[19]);
        } else {
            System.out.println("Not enough parameters. Inform the following:");
            System.out.println("\t 1 - Population Size (int);");
            System.out.println("\t 2 - Max Evaluations (int);");
            System.out.println("\t 3 - Crossover Probability (double);");
            System.out.println("\t 4 - Mutation Probability (double);");
            System.out.println("\t 5 - Alpha (double);");
            System.out.println("\t 6 - Beta (double);");
            System.out.println("\t 7 - Crossover Operators (String[] - comma separated, no spaces);");
            System.out.println("\t 8 - Mutation Operators (String[] - comma separated, no spaces);");
            System.out.println("\t 9 - Problems (String[] - comma separated, no spaces);");
            System.out.println("\t 10 - Heuristic Function (ChoiceFunction or MultiArmedBandit);");
            System.out.println("\t 11 - Sliding window size W (int);");
            System.out.println("\t 12 - Scaling factor C (double);");
            System.out.println("\t 13 - Number of objectives (int - 2 or 4);");
            System.out.println("\t 14 - Save debugging information (rank changes, generation, etc)? (boolean);");
            System.out.println("\t 15 - Executions (int);");
            System.out.println("\t 16 - Output Path (String);");
            System.out.println();
            System.out.println("Would you like to execute the default parameters ('y' for 'yes' or anything for 'no')?");

            Scanner scanner = new Scanner(System.in);
            if (!"y".equals(scanner.nextLine())) {
                System.exit(0);
            }
            System.out.println();

            problems = new String[]{
                "OO_MyBatis",
                "OA_AJHsqldb",};
            /*
             "OA_AJHotDraw",
             "OO_BCEL",
             "OO_JHotDraw",
             "OA_HealthWatcher",
             "OA_TollSystems",
             "OO_JBoss"
             */
            crossovers = new String[]{
                "TwoPointsCrossover",
                "MultiMaskCrossover",
                "PMXCrossover"
            };

            mutations = new String[]{
                "SwapMutation",
                "SimpleInsertionMutation"
            };

            numberOfObjectives = 2;
            populationSize = 300;
            maxEvaluations = 60000;
            crossoverProbability = 1;
            mutationProbability = 1;
            alpha = 1;
            beta = 0.00005;

            heuristicFunction = LowLevelHeuristic.CHOICE_FUNCTION;

            w = populationSize / 2;
            //MOEADCF_1.0_0.10_30_0.9_6_0.5

            c = 5;
            d = 0.5;
            t = 30;
            nr = 6;
            //beta = 0.10;
            debug = true;
            executions = 1;
            tipo = 1;
            path = "experiment/MOEAD/";
        }

        String[] temp = new String[mutations.length + 1];
        temp[0] = null;
        System.arraycopy(mutations, 0, temp, 1, mutations.length);
        mutations = temp;

        System.out.println("Initializing experiments.");
        System.out.println("Parameters:");
        System.out.println("\tPopulationSize = " + populationSize);
        System.out.println("\tMax Evaluations = " + maxEvaluations);
        System.out.println("\tCrossover Probability = " + crossoverProbability);
        System.out.println("\tMutation Probability = " + mutationProbability);
        System.out.println("\tAlpha = " + alpha);
        System.out.println("\tBeta = " + beta);
        System.out.println("\tCrossover Operators = " + Arrays.toString(crossovers));
        System.out.println("\tMutation Operators = " + Arrays.toString(mutations));
        System.out.println("\tProblems = " + Arrays.toString(problems));
        System.out.println("\tHeuristic Function = " + heuristicFunction);
        System.out.println("\tSliding window size W = " + w);
        System.out.println("\tScaling factor C = " + c);

        for (String problemName : problems) {
            System.out.println();
            System.out.println("-------------------");
            System.out.println();
            System.out.println("Problem: " + problemName);
            System.out.println();

            String outputDirectory = path + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problemName + "/";
            createDirectory(outputDirectory);

            CITO_CAITO problem; // The problem to solve
            MOEAD_DRA_FRRCF algorithm; // The algorithm to use
            Operator selection; // Selection operator

            if (numberOfObjectives == 2) {
                problem = new Combined2Objetives("problemas/" + problemName + ".txt");
            } else if (numberOfObjectives == 4) {
                problem = new Combined4Objectives("problemas/" + problemName + ".txt");
            } else {
                problem = null;
                System.err.println("Wrong number of objectives (" + numberOfObjectives + "). Available values: 2 or 4.");
                System.exit(1);
            }
            algorithm = new MOEAD_DRA_FRRCF(problem);

            //algorithm = new ssNSGAII(problem);
            // Algorithm parameters
            algorithm.setInputParameter("populationSize", populationSize);
            algorithm.setInputParameter("maxEvaluations", maxEvaluations);
            algorithm.setInputParameter("heuristicFunction", heuristicFunction);

            // Selection Operator
            selection = SelectionFactory.getSelectionOperator("BinaryTournament2");

            // Add the operators to the algorithm
            algorithm.addOperator("selection", selection);

            algorithm.setInputParameter("beta", beta);
            algorithm.setInputParameter("alpha", alpha);
            algorithm.setInputParameter("d", d);
            algorithm.setInputParameter("finalSize", populationSize); // used by MOEAD_DRA
            algorithm.setInputParameter("T", t);//30
            algorithm.setInputParameter("delta", delta);
            algorithm.setInputParameter("tipo", tipo);
            algorithm.setInputParameter("nr", nr);//30
            algorithm.setInputParameter("dataDirectory", "/home/vinicius/Projetos/HITO/moead_weight");//30
            algorithm.setInputParameter("dataDirectory", "moead_weight");//30
            //Create low level heuristics
            int lowLevelHeuristicNumber = 1;
            String[] lowLevelHeuristicNames = new String[crossovers.length * mutations.length + crossovers.length];
            for (String crossoverName : crossovers) {
                for (String mutationName : mutations) {
                    HashMap<String, Object> parameters = new HashMap<>();

                    String name = "h" + lowLevelHeuristicNumber + " [" + crossoverName;
                    if (mutationName != null) {
                        Mutation mutation = MutationFactory.getMutationOperator(mutationName);
                        mutation.setParameter("probability", mutationProbability);
                        parameters.put("mutation", mutation);

                        name += ", " + mutationName;
                    }
                    name += "]";
                    lowLevelHeuristicNames[lowLevelHeuristicNumber - 1] = name;

                    parameters.put("name", name);
                    parameters.put("alpha", alpha);
                    parameters.put("beta", beta);
                    parameters.put("w", w);
                    parameters.put("c", c);

                    Crossover crossover = CrossoverFactory.getCrossoverOperator(crossoverName);
                    crossover.setParameter("probability", crossoverProbability);
                    parameters.put("crossover", crossover);

                    algorithm.addLowLevelHeuristic(parameters);

                    lowLevelHeuristicNumber++;
                }
            }
            
            HashMap<Integer, HashMap<String, Integer>> escolhas = new HashMap<>();
            for (int i = 0; i <= maxEvaluations; i++) {
                HashMap<String, Integer> llhhash = new HashMap<>();
                for (String llh : lowLevelHeuristicNames) {
                    llhhash.put(llh, 0);
                }
                escolhas.put(i, llhhash);
            }
            algorithm.setEscolhas(escolhas);

            new File(outputDirectory + "LLH.txt").delete();
            try (FileWriter timeWriter = new FileWriter(outputDirectory + "TIME_EXECUTION.txt")) {

                SolutionSet allRuns = new SolutionSet();
                long allExecutionTime = 0;
                int[] allTimesApplied = new int[algorithm.getLowLevelHeuristicsSize()];

                for (int execution = 0; execution < executions; execution++) {
                    String executionDirectory = outputDirectory + "EXECUTION_" + execution + "/";
                    createDirectory(executionDirectory);

                    System.out.println("Execution: " + (execution + 1));
                    algorithm.clearLowLeverHeuristicsValues();

                    if (debug) {
                        algorithm.setLowLevelHeuristicsRankPath(executionDirectory + "RANK.txt");
                        algorithm.setLowLevelHeuristicsTimePath(executionDirectory + "TIME.txt");
                        algorithm.setDebugPath(executionDirectory + "DEBUG");

                        String generationsDirectory = executionDirectory + "GENERATIONS/";
                        createDirectory(generationsDirectory);
                        algorithm.setGenerationsOutputDirectory(generationsDirectory);
                    }

                    // Execute the Algorithm
                    long initTime = System.currentTimeMillis();
                    SolutionSet population = algorithm.execute();
                    long estimatedTime = System.currentTimeMillis() - initTime;

                    problem.removeDominadas(population);
                    problem.removeRepetidas(population);

                    // Result messages
                    //population.printVariablesToFile(executionDirectory + "VAR.txt");
                    population.printObjectivesToFile(executionDirectory + "FUN.txt");
                    algorithm.printLowLevelHeuristicsInformation(executionDirectory + "LLH.txt");

                    timeWriter.append(estimatedTime + "\n");
                    timeWriter.flush();
                    allExecutionTime += estimatedTime;

                    allRuns = allRuns.union(population);

                    int[] executionTimesApplied = algorithm.getLowLevelHeuristicsNumberOfTimesApplied();
                    for (int i = 0; i < executionTimesApplied.length; i++) {
                        allTimesApplied[i] += executionTimesApplied[i];
                    }

                    if (debug) {
                        algorithm.closeLowLevelHeuristicsRankPath();
                        algorithm.closeLowLevelHeuristicsTimePath();
                        algorithm.closeDebugPath();
                    }
                }

                System.out.println();
                System.out.println("End of execution for problem " + problemName + ".");
                System.out.println("Total time (seconds): " + allExecutionTime / 1000);
                System.out.println("Writing results.");
                problem.removeDominadas(allRuns);
                problem.removeRepetidas(allRuns);

                //allRuns.printVariablesToFile(outputDirectory + "VAR.txt");
                allRuns.printObjectivesToFile(outputDirectory + "FUN.txt");

                timeWriter.append("\n");
                timeWriter.append("Total: " + allExecutionTime + "\n");
                timeWriter.append("Average: " + (double) ((double) allExecutionTime / (double) executions) + "\n");

                try (FileWriter timesAppliedWriter = new FileWriter(outputDirectory + "LLH.txt")) {
                    for (int i = 0; i < allTimesApplied.length; i++) {
                        timesAppliedWriter.append(lowLevelHeuristicNames[i] + " " + allTimesApplied[i] + "\n");
                    }
                }
            }
            
            try (FileWriter timesAppliedWriter = new FileWriter(outputDirectory + "Escolhas_"+problemName+".txt")) {
                for (int i = populationSize; i <= maxEvaluations; i++) {
                    HashMap<String, Integer> llhhash = escolhas.get(i);
                    String toPrint;
                    //toPrint=String.valueOf(i+1);
                    toPrint="";
                    for (int j=0; j < lowLevelHeuristicNames.length; j++) {
                        String llh=lowLevelHeuristicNames[j];
                        int qtd=llhhash.get(llh);
                        //double avg=((double)qtd)/((double)executions);
                        //toPrint+=";"+String.valueOf(j+1)+";"+qtd;
                        toPrint+=String.valueOf(j+1)+" "+qtd+" ";
                    }
                    timesAppliedWriter.append(toPrint.trim()+"\n");
                }    
                        
                    
            }
        }
    } //main

    private static void createDirectory(String directory) {
        File outputDirectoryFile = new File(directory);
        if (!outputDirectoryFile.exists()) {
            if (!outputDirectoryFile.getParentFile().exists()) {
                outputDirectoryFile.mkdirs();
            }
            outputDirectoryFile.mkdir();
        }
    }

} // NSGAII_main
