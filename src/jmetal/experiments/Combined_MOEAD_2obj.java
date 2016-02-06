package jmetal.experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.CrossoverFactory;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.metaheuristics.moead.Genetic_MOEAD_DRA;
import jmetal.problems.Combined2Objetives;
import jmetal.util.JMException;

public class Combined_MOEAD_2obj {

    private static void createDirectory(String directory) {
        File outputDirectoryFile = new File(directory);
        if (!outputDirectoryFile.exists()) {
            if (!outputDirectoryFile.getParentFile().exists()) {
                outputDirectoryFile.mkdirs();
            }
            outputDirectoryFile.mkdir();
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

        //Softwares
        String[] softwares = {
            "OA_AJHotDraw",
            "OA_AJHsqldb",
            "OA_HealthWatcher",
            "OA_TollSystems",
            "OO_BCEL",
            "OO_JBoss",
            "OO_JHotDraw",
            "OO_MyBatis"
        };

        int nr=3;
        if(args.length==1)
            nr=Integer.parseInt(args[0]);
        int t = 30;
        double delta = 0.9;

        int runsNumber = 30;
        int populationSize = 300;
        int maxEvaluations = 60000;
        double crossoverProbability = 1.0;
        double mutationProbability = 0.02; //0.2;
        String context = "_Comb_2obj";

        
            for (String filename : softwares) {
                Combined_MOEAD_2obj.createDirectory("resultado/moead_" + nr + "/" + filename + context);
                Combined2Objetives problem = new Combined2Objetives("problemas/" + filename + ".txt");
                Algorithm algorithm = new Genetic_MOEAD_DRA(problem);
                SolutionSet todasRuns = new SolutionSet();
                Operator crossover;
                Operator mutation;
                Operator selection;

                // Crossover
                crossover = CrossoverFactory.getCrossoverOperator("TwoPointsCrossover");
                crossover.setParameter("probability", crossoverProbability);
                // Mutation
                mutation = MutationFactory.getMutationOperator("SwapMutation");
                mutation.setParameter("probability", mutationProbability);
                // Selection
                selection = SelectionFactory.getSelectionOperator("BinaryTournament");
                // Algorithm params
                algorithm.setInputParameter("populationSize", populationSize);
                algorithm.setInputParameter("maxEvaluations", maxEvaluations);
                algorithm.addOperator("crossover", crossover);
                algorithm.addOperator("mutation", mutation);
                algorithm.addOperator("selection", selection);

                algorithm.setInputParameter("finalSize", populationSize); // used by MOEAD_DRA
                algorithm.setInputParameter("T", t);//30
                algorithm.setInputParameter("delta", delta);
                algorithm.setInputParameter("nr", nr);//30
                algorithm.setInputParameter("dataDirectory", "./moead_weight") ;

                System.out.println("\n================ MOEA/D ================");
                System.out.println("Software: " + filename);
                System.out.println("Context: " + context);
                System.out.println("Params:");
                System.out.println("\tPop -> " + populationSize);
                System.out.println("\tMaxEva -> " + maxEvaluations);
                System.out.println("\tCross -> " + crossoverProbability);
                System.out.println("\tMuta -> " + mutationProbability);
                System.out.println("Number of elements: " + problem.numberOfElements_);

                long heapSize = Runtime.getRuntime().totalMemory();
                heapSize = (heapSize / 1024) / 1024;
                System.out.println("Heap Size: " + heapSize + "Mb\n");

                for (int runs = 0; runs < runsNumber; runs++) {

                    // Execute the Algorithm
                    long initTime = System.currentTimeMillis();
                    SolutionSet resultFront = algorithm.execute();
                    long estimatedTime = System.currentTimeMillis() - initTime;
                    System.out.println("Iruns: " + runs + "\tTotal time: " + estimatedTime);

                    resultFront = problem.removeDominadas(resultFront);
                    resultFront = problem.removeRepetidas(resultFront);

                    resultFront.printObjectivesToFile("resultado/moead_" + nr + "/" + filename + context + "/FUN_moead" + "-" + filename + "-" + runs + ".NaoDominadas");
                    resultFront.printVariablesToFile("resultado/moead_" + nr + "/" + filename + context + "/VAR_moead" + "-" + filename + "-" + runs + ".NaoDominadas");

                    //armazena as solucoes de todas runs
                    todasRuns = todasRuns.union(resultFront);

                }

                todasRuns = problem.removeDominadas(todasRuns);
                todasRuns = problem.removeRepetidas(todasRuns);
                todasRuns.printObjectivesToFile("resultado/moead_" + nr + "/" + filename + context + "/All_FUN_moead" + "-" + filename);
                todasRuns.printVariablesToFile("resultado/moead_" + nr + "/" + filename + context + "/All_VAR_moead" + "-" + filename);
            }
        
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
