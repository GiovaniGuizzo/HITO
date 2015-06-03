package jmetal.experiments.indicators;

import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.*;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class EncontraPareto2Objetives {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] softwares = {
            "combined",
            "incremental",
//            "OA_AJHotDraw",
//            "OA_AJHsqldb",
//            "OA_HealthWatcher",
//            "OA_TollSystems"
        };
        for (String software : softwares) {
            MetricsUtil mu = new MetricsUtil();

            //le o conjunto com todas as solucoes encontradas por todos os algoritmos
            SolutionSet ss = mu.readNonDominatedSolutionSet(software + ".txt");
            //remove as solucoes dominadas e repeditas formando o conjunto de pareto rela
            ss = removeDominadas(ss);
            //escreve o conjunto de pareto real em um arquivo
            ss.printObjectivesToFile(software + "_trueParetoFront.txt");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
                    System.out.println("--------------------------------------------");
                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
                    System.out.println("[" + i + "] " + result.get(i).toString());
                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
                    System.out.println("--------------------------------------------");
                    System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
                    System.out.println("[" + i + "] " + result.get(i).toString());
                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
