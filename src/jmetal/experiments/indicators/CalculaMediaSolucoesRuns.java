package jmetal.experiments.indicators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import jmetal.util.JMException;

public class CalculaMediaSolucoesRuns {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        //String[] softwares = {"OO_BCEL", "OO_JBoss", "OO_JHotDraw", "OO_MyBatis"};
        String[] softwares = {
            "OA_AJHotDraw",
            "OA_AJHsqldb",
            "OA_HealthWatcher",
            "OA_TollSystems",
        };
        String[] algorithms = {
            "nsgaii",
            "spea2",
            //"paes"
        };
        float total;

        for (String software : softwares) {
            System.out.println("\n" + software);
            for (String algorithm : algorithms) {
                total = 0;
                for (int run = 0; run < 30; run++) {
                    BufferedReader buff = new BufferedReader(new FileReader("resultado/" + algorithm + "/" + software + "_Comb_2obj/FUN_" + algorithm + "-" + software + "-" + run + ".NaoDominadas"));

                    int contador = 0;
                    while (buff.ready()) {
                        contador++;
                        String line = buff.readLine();
                    }
                    total = total + contador;
                    System.out.println(" " + algorithm + " / " + run + ": " + contador);
                }
                float media = total/30;
                System.out.println(" " + software + " / " + algorithm + "  --> Média: " + media + " - Total: " + total +"\n");
            }
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
