package jmetal.experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.SolutionSet;
import jmetal.base.operator.mutation.MutationFactory;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.metaheuristics.paes.PAES;
import jmetal.problems.Combined4Objectives;
import jmetal.util.JMException;

public class Combined_PAES_4obj {

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

        for (String filename : softwares) {

            int runsNumber = 1;
            int maxEvaluations = 60000;
            int archiveSize = 250;
            int biSections = 5;
            double mutationProbability = 1.0;
            String context = "_Comb_4obj";

            File directory = new File("resultado/paes/" + filename + context);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.exit(0);
                }
            }

            Combined4Objectives problem = new Combined4Objectives("problemas/" + filename + ".txt");
            Algorithm algorithm = new PAES(problem);
            SolutionSet todasRuns = new SolutionSet();
            Operator mutation;
            Operator selection;

            // Mutation
            mutation = MutationFactory.getMutationOperator("SwapMutation");
            mutation.setParameter("probability", mutationProbability);
            // Selection
            selection = SelectionFactory.getSelectionOperator("BinaryTournament");
            // Algorithm params
            algorithm.setInputParameter("maxEvaluations", maxEvaluations);
            algorithm.setInputParameter("archiveSize", archiveSize);
            algorithm.setInputParameter("biSections", biSections);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            System.out.println("\n================ PAES ================");
            System.out.println("Software: " + filename);
            System.out.println("Context: " + context);
            System.out.println("Params:");
            System.out.println("\tMaxEva -> "+maxEvaluations);
            System.out.println("\tMuta -> "+mutationProbability);
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

                resultFront.printObjectivesToFile("resultado/paes/" + filename + context + "/FUN_paes" + "-" + filename + "-" + runs + ".NaoDominadas");
                resultFront.printVariablesToFile("resultado/paes/" + filename + context + "/VAR_paes" + "-" + filename + "-" + runs + ".NaoDominadas");

                //armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);
            }

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);
            todasRuns.printObjectivesToFile("resultado/paes/" + filename + context + "/All_FUN_paes" + "-" + filename);
            todasRuns.printVariablesToFile("resultado/paes/" + filename + context + "/All_VAR_paes" + "-" + filename);

            //grava arquivo juntando funcoes e variaveis
            //problem.gravaCompleto(todasRuns, "TodasRuns-Completo_paes");
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
