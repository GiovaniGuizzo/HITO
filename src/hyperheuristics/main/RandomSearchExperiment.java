package hyperheuristics.main;

import hyperheuristics.algorithm.RandomSearch;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.SolutionSet;
import jmetal.problems.ITO;
import jmetal.util.JMException;

public class RandomSearchExperiment {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

        //Softwares
        String[] softwares = {
            "Guava"
        };

        for (String filename : softwares) {

            int runsNumber = 100;
            int maxEvaluations = 60000;
            String context = "_Comb_2obj";

            File directory = new File("resultado/random/" + filename + context);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.exit(0);
                }
            }

            ITO problem = new ITO("problemas/" + filename + ".txt");
            SolutionSet todasRuns = new SolutionSet();
            RandomSearch algorithm = new RandomSearch(problem, maxEvaluations);

            System.out.println("\n================ Random ================");
            System.out.println("Software: " + filename);
            System.out.println("Context: " + context);
            System.out.println("Params:");
            System.out.println("\tMaxEva -> " + maxEvaluations);

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

                resultFront.printObjectivesToFile("resultado/random/" + filename + context + "/FUN_random" + "-" + filename + "-" + runs + ".NaoDominadas");
                resultFront.printVariablesToFile("resultado/random/" + filename + context + "/VAR_random" + "-" + filename + "-" + runs + ".NaoDominadas");

                //armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);

            }

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);
            todasRuns.printObjectivesToFile("resultado/random/" + filename + context + "/All_FUN_random" + "-" + filename);
            todasRuns.printVariablesToFile("resultado/random/" + filename + context + "/All_VAR_random" + "-" + filename);

        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
