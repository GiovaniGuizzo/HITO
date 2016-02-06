/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author vinicius
 */
public class UCB_VSelector extends UCBSelectorVariance{

    public UCB_VSelector(ArrayList<LowLevelHeuristic> operators, double C, SlidingWindow slidingwindow) {
        super(operators, C, slidingwindow);
    }
    
    @Override
    protected double equation(LowLevelHeuristic op, HashMap<String, Double> frr, HashMap<String, Integer> nt, double sumNt) {
        //Jackson Code
        //heuristicReward /*FRRop*/ + this.c * 
        //Math.sqrt((2 * Math.log(getNumberOfIterations()) * variance )/(numPlayed)) 
        //+ 3 * (getNumberOfIterations()/(numPlayed)) ;
        double numerator = 2 * Math.log(sumNt) * this.calcVariance(op.getName());
        double denominator=nt.get(op.getName());
        double fraction=numerator/denominator;
        double sqrt = Math.sqrt(fraction);
        double frr_value=frr.get(op.getName());
        double numerator2=super.sumNt(nt);
        double denominator2=nt.get(op.getName());
        double fraction2=numerator2/denominator2;
        return frr_value + this.C * sqrt + 3 * fraction2;
    }
}
