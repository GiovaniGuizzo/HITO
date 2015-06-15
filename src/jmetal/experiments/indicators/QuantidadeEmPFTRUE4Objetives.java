package jmetal.experiments.indicators;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class QuantidadeEmPFTRUE4Objetives {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] abordagens = {
            "MECBA", 
//            "MECBA-Clu",
        };
        String[] algoritmos = {
            "nsgaii",
            "paes",
            "spea2"
        };
        String[] softwares = {
            "OA_AJHotDraw",
            "OA_AJHsqldb",
//            "OA_HealthWatcher",
//            "OA_TollSystems", 
            
//            "OO_BCEL",
//            "OO_JBoss",
//            "OO_JHotDraw",
//            "OO_MyBatis"
        };

        String[] contextos = {
            "_Comb_4obj",
            "_Inc_4obj"
        };
        for (String abordagem : abordagens) {
            for (String software : softwares) {
                for (String algorithm : algoritmos) {
                    for (String contexto : contextos) {
                        
                        System.out.println("\n\n--------------------------------");
                        System.out.println("Approach..: " + abordagem);
                        System.out.println("Software..: " + software);
                        System.out.println("MOEA......: " + algorithm);
                        System.out.println("Context...: " + contexto);
                        System.out.println("");

                        FileWriter os = null;
                        double value = 0;
                        int quantidadeSolucoes;
                        MetricsUtil mu = new MetricsUtil();

                        //le o conjunto com as solucoes de PFtrue - melhores encontradas por todos os MOEAS
                        double[][] PFtrue = mu.readFront("resultado/" + abordagem + "/" + software + "_trueParetoFront.txt");
                        //le o conjunto com as solucoes de PFknow - melhores encontradas por um MOEA
                        double[][] PFknown = mu.readFront("resultado/" + abordagem + "/" + algorithm + "/" + software + contexto + "/All_FUN_" + algorithm + "-" + software);

                        quantidadeSolucoes = 0;
                        
                        for (int i = 0; i < PFknown.length; i++) {
                            //System.out.println(PFknown[i][0] + " - " + PFknown[i][1] + " - " + PFknown[i][2] + " - " + PFknown[i][3]);
                            for (int j = 0; j < PFtrue.length; j++) {
                                //System.out.println("---->" + PFtrue[j][0] + " - " + PFtrue[j][1] + " - " + PFtrue[j][2] + " - " + PFtrue[j][3]);
                                if((PFknown[i][0] == PFtrue[j][0]) && (PFknown[i][1] == PFtrue[j][1]) && 
                                        (PFknown[i][2] == PFtrue[j][2]) && (PFknown[i][3] == PFtrue[j][3]) ){
                                    //System.out.println("----> Igual");
                                    quantidadeSolucoes++;
                                }
                                
                            }
                        }
                        
                        //System.out.println("");

                    }
                }
            }
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
