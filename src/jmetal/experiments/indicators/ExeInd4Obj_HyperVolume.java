package jmetal.experiments.indicators;

import java.io.*;
import jmetal.base.SolutionSet;
import jmetal.problems.Combined4Objectives;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;
import java.util.*;

public class ExeInd4Obj_HyperVolume {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] abordagens = {
            "MECBA-Clu"
        };
        String[] algoritmos = {
            "nsgaii",
            "paes",
            "spea2"
        };
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

        for (String abordagem : abordagens) {
            for (String software : softwares) {
                for (String algorithm : algoritmos) {
                    
                    FileWriter os = null;
                    os = new FileWriter("resultado/" + abordagem + "/" + software + "-HP_" + algorithm + "_wfg.txt", true);
                    os.write("#\n");

                    for (int run = 0; run < 30; run++) {
                        File arq = new File("resultado/" + abordagem + "/" + algorithm + "/" + software + "_Comb_4obj/FUN_" + algorithm + "-" + software + "-" + run + ".NaoDominadas");
                        FileReader fileReader = new FileReader(arq);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        String linha;
                        while ((linha = bufferedReader.readLine()) != null) {
                            os.write(linha + "\n");
                        }

                        fileReader.close();
                        bufferedReader.close();

                        os.write("#\n");
                    }
                    os.close();


                    /*
                     * FileWriter os = null; double value = 0; MetricsUtil mu =
                     * new MetricsUtil();
                     *
                     * //Combined4Objectives problem = new
                     * Combined4Objectives("problemas/" + software + ".txt");
                     * //QualityIndicator indicators = new
                     * QualityIndicator(problem, "resultado/" + abordagem + "/"
                     * + software + "_Comb_4obj_trueParetoFront.txt");
                     * //SolutionSet ss =
                     * mu.readNonDominatedSolutionSet("resultado/" + abordagem +
                     * "/" + algorithm + "/" + software + "_Comb_4obj/FUN_" +
                     * algorithm + "-" + software + "-" + run +
                     * ".NaoDominadas");
                     *
                     * QualityIndicator indicators; Combined4Objectives problem
                     * = new Combined4Objectives("problemas/" + software +
                     * ".txt"); indicators = new QualityIndicator(problem,
                     * "resultado/" + abordagem + "/" + software +
                     * "_Comb_4obj_trueParetoFront.txt");
                     *
                     * //calcula HP do conjuto de pareto real em relacao a cada
                     * run do algoritmo for (int run = 0; run < 30; run++) {
                     * Hypervolume qualityIndicator = new Hypervolume();
                     *
                     * double[][] solutionFront =
                     * qualityIndicator.utils_.readFront("resultado/" +
                     * abordagem + "/" + algorithm + "/" + software +
                     * "_Comb_4obj/FUN_" + algorithm + "-" + software + "-" +
                     * run + ".NaoDominadas"); double[][] trueFront =
                     * qualityIndicator.utils_.readFront("resultado/" +
                     * abordagem + "/" + software +
                     * "_Comb_4obj_trueParetoFront.txt");
                     *
                     * value = qualityIndicator.hypervolume(solutionFront,
                     * trueFront, 4);
                     *
                     * //SolutionSet ss =
                     * mu.readNonDominatedSolutionSet("resultado/" + abordagem +
                     * "/" + algorithm + "/" + software + "_Comb_4obj/FUN_" +
                     * algorithm + "-" + software + "-" + run +
                     * ".NaoDominadas"); //value =
                     * indicators.getHypervolume(ss);
                     *
                     * os = new FileWriter("resultado/" + abordagem + "/" +
                     * software + "-HP_" + algorithm + ".txt", true);
                     * os.write("" + value + "\n"); os.close(); }
                     */

                }
            }
        }
    }

}