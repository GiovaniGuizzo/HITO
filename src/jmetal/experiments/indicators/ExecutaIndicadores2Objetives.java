package jmetal.experiments.indicators;

import jmetal.problems.Combined2Objetives;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.base.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class ExecutaIndicadores2Objetives {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        
        String[] algoritmos = {
            "nsgaii",
//            "paes",
            "spea2"
        };
        String[] softwares = {
            "OA_AJHotDraw",
            "OA_AJHsqldb",
            "OA_HealthWatcher",
            "OA_TollSystems",
//            "OO_BCEL",
//            "OO_JBoss",
//            "OO_JHotDraw",
//            "OO_MyBatis",
        };
        
        for (String software : softwares) {
            for (String algorithm : algoritmos) {
                FileWriter os = null;
                double value = 0;
                MetricsUtil mu = new MetricsUtil();
                Combined2Objetives problem = new Combined2Objetives("problemas/" + software + ".txt");


                //le o conjunto com todas as solucoes encontradas por todos os algoritmos
                SolutionSet ss = mu.readNonDominatedSolutionSet("resultado/"+software+"_AllSolutions.txt");
                //remove as solucoes dominadas e repeditas formando o conjunto de pareto rela
                ss = removeDominadas(ss);
                //escreve o conjunto de pareto real em um arquivo
                ss.printObjectivesToFile("resultado/"+software+"_trueParetoFront.txt");

                //le o arquivo com todas as melhores solucoes geradas nas 30 runs por um algoritmo
                double[][] melhoresSolucoesAlgoritmo = mu.readFront("resultado/All_FUN_"+algorithm+"-"+software);
                //retorna a solucao minima de cada objetivo do conjunto de pareto real
                double[] min = mu.getMinimumValues( ss.writeObjectivesToMatrix(), 2 ); //4 para 4 objetivos

                //comparar o mínimo em relacão as solucoes de cada algoritmo para cada problema
                os = new FileWriter("resultado/"+software+"-ED_"+algorithm+".txt", true);
                os.write("--- Minimos Objetivos: "+min[0]+" "+min[1]+" ---"+"\n");
                for (int i = 0; i < melhoresSolucoesAlgoritmo.length; i++) {
                    double distanciaEuclidiana = mu.distance(min, melhoresSolucoesAlgoritmo[i]);
                    os.write(""+ distanciaEuclidiana +"\n");
                }
                os.close();


                //calcula GD e IGD do conjuto de pareto real em relacao a cada run do algoritmo
                for (int run = 0; run < 30; run++) {
                    QualityIndicator indicators = new QualityIndicator(problem, "resultado/"+algorithm+"/"+software+"_Comb_2obj/FUN_"+algorithm+"-"+software+"-"+run+".NaoDominadas");
                    value = indicators.getIGD(ss);
                    os = new FileWriter("resultado/"+software+"-IGD_"+algorithm+".txt", true);
                    os.write("" + value + "\n");
                    os.close();
                    value = indicators.getGD(ss);
                    os = new FileWriter("resultado/"+software+"-GD_"+algorithm+".txt", true);
                    os.write("" + value + "\n");
                    os.close();
                }
            }
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    public static SolutionSet removeDominadas(SolutionSet result){
        boolean dominador,dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size()-1); i++) {
            for (int j = (i+1); j < result.size(); j++){
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++){
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if( valor1  > valor2 || dominador==false ) {
                        dominador = false;
                    }else if( valor1 <= valor2 ){
                        dominador = true;
                    }

                    if( valor2  > valor1 || dominado==false ) {
                        dominado = false;
                    }else if( valor2 < valor1 ){
                        dominado = true;
                    }
                }

                if(dominador){
                    System.out.println("--------------------------------------------");
                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
                    System.out.println("[" + i + "] " + result.get(i).toString());
                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(j);
                    j = j-1;
                }else if(dominado){
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