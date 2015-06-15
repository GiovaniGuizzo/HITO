package jmetal.experiments.indicators;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class ExeInd4Obj_HyperVolumeNew {

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
                MetricsUtil mu = new MetricsUtil();
                double[][] todasSolucoes = mu.readFront("resultado/" + abordagem + "/all_solutions/" + software + "_Comb_4obj_AllSolutions.txt");
                double[] max = mu.getMaximumValues(todasSolucoes, 4);
                System.out.println(software+": "+max[0]+" "+max[1]+" "+max[2]+" "+max[3]);
            }   
        }
        
        /*
        for (String abordagem : abordagens) {
            for (String software : softwares) {
                for (String algorithm : algoritmos) {


                    FileWriter os = null;
                    double value = 0;

                    for (int run = 0; run < 30; run++) {
                        Hypervolume qualityIndicator = new Hypervolume();
                        double[][] solutionFront = qualityIndicator.utils_.readFront("resultado/" + abordagem + "/" + algorithm + "/" + software + "_Comb_4obj/FUN_" + algorithm + "-" + software + "-" + run + ".NaoDominadas");
                        double[][] trueFront = qualityIndicator.utils_.readFront("resultado/" + abordagem + "/" + software + "_Comb_4obj_trueParetoFront.txt");

                        //double[] maximumValues = qualityIndicator.utils_.getMaximumValues(solutionFront, 4);
                        //double[] minimumValues = qualityIndicator.utils_.getMinimumValues(solutionFront, 4);
                        //double [][] normalizedFront = qualityIndicator.utils_.getNormalizedFront(solutionFront, maximumValues, minimumValues);
                        //solutionFront = qualityIndicator.utils_.invertedFront(solutionFront);
                        //value = qualityIndicator.calculateHypervolume(solutionFront, solutionFront.length, 4);

                        value = qualityIndicator.hypervolume(solutionFront, trueFront, 4);

                        os = new FileWriter("resultado/" + abordagem + "/" + software + "-HP_" + algorithm + ".txt", true);
                        os.write("" + value + "\n");
                        os.close();
                    }


                }
            }
        }
        */
        
        
    }
}