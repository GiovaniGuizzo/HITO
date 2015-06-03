package jmetal.experiments.indicators;

import jmetal.problems.Combined2Objetives;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.base.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class Hypervolume {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

        FileWriter os = null;
        double value = 0;
        MetricsUtil mu = new MetricsUtil();
        Combined2Objetives problem = new Combined2Objetives("problemas/OA_AJHotDraw.txt");

        SolutionSet ss = mu.readNonDominatedSolutionSet("arion/ref");
        QualityIndicator indicators = new QualityIndicator(problem, "arion/ref");
        value = indicators.getHypervolume(ss);
        System.out.println(value);
        
        ss = mu.readNonDominatedSolutionSet("arion/broad");
        indicators = new QualityIndicator(problem, "arion/broad");
        value = indicators.getHypervolume(ss);
        System.out.println(value);
        
        
        System.out.println("-------------------------------");
        ss = mu.readNonDominatedSolutionSet("arion/solu");
        indicators = new QualityIndicator(problem, "arion/true");
        value = indicators.getHypervolume(ss);
        System.out.println(value);
        
        value = indicators.getTrueParetoFrontHypervolume();
        System.out.println(value);
        
//        ss = mu.readNonDominatedSolutionSet("arion/solu");
//        indicators = new QualityIndicator(problem, "arion/solu");
//        value = indicators.getHypervolume(ss);
//        System.out.println(value);


    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}