package jmetal.experiments.indicators;

import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.util.JMException;

public class CalculaCusto {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] softwares = {
//            "OA_AJHotDraw/OA_AJHotDraw_aga_comb", 
//            "OA_AJHotDraw/OA_AJHotDraw_aga_inc", 
//            "OA_AJHotDraw/OA_AJHotDraw_ag_comb", 
//            "OA_AJHotDraw/OA_AJHotDraw_ag_inc", 
//            "OA_AJHotDraw/OA_AJHotDraw_ago_comb", 
//            "OA_AJHotDraw/OA_AJHotDraw_ago_inc", 
//            "OA_AJHotDraw/OA_AJHotDraw_nsgaii_comb", 
//            "OA_AJHotDraw/OA_AJHotDraw_nsgaii_inc", 

//            "OA_AJHsqldb/OA_AJHsqldb_aga_comb", 
//            "OA_AJHsqldb/OA_AJHsqldb_aga_inc", 
//            "OA_AJHsqldb/OA_AJHsqldb_ag_comb", 
//            "OA_AJHsqldb/OA_AJHsqldb_ag_inc", 
//            "OA_AJHsqldb/OA_AJHsqldb_ago_comb", 
//            "OA_AJHsqldb/OA_AJHsqldb_ago_inc", 
//            "OA_AJHsqldb/OA_AJHsqldb_nsgaii_comb", 
//            "OA_AJHsqldb/OA_AJHsqldb_nsgaii_inc", 
            
//            "OA_AJHotDraw/OA_AJHotDraw_nsgaii_comb",
//            "OA_AJHotDraw/OA_AJHotDraw_nsgaii_inc",
//            "OA_AJHotDraw/OA_AJHotDraw_paes_comb",
//            "OA_AJHotDraw/OA_AJHotDraw_paes_inc",
//            "OA_AJHotDraw/OA_AJHotDraw_spea2_comb",
//            "OA_AJHotDraw/OA_AJHotDraw_spea2_inc",
            
            "OA_AJHsqldb/OA_AJHsqldb_nsgaii_comb",
            "OA_AJHsqldb/OA_AJHsqldb_nsgaii_inc",
            "OA_AJHsqldb/OA_AJHsqldb_paes_comb",
            "OA_AJHsqldb/OA_AJHsqldb_paes_inc",
            "OA_AJHsqldb/OA_AJHsqldb_spea2_comb",
            "OA_AJHsqldb/OA_AJHsqldb_spea2_inc",
        };
        for (String software : softwares) {
            System.out.println("\n\n---------------" + software + "---------------\n\n");

            
            //OA_AJHotDraw, OA_AJHsqldb, OA_HealthWatcher, OA_TollSystems
            CalculaCustoStubs ccs = new CalculaCustoStubs();
            ccs.readProblem("problemas/OA_AJHsqldb.txt");

            ccs.readSolution("resultado/" + software + ".txt");


        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
