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
public class UCB_TunnedSelector extends UCBSelectorVariance{

    public UCB_TunnedSelector(ArrayList<LowLevelHeuristic> operators, double C, SlidingWindow slidingwindow) {
        super(operators, C, slidingwindow);
    }

    protected HashMap<String, Double> VCalc(HashMap<String, Integer> nt, double sumNt) {
        //double vop = variance + Math.sqrt(2 * Math.log(getNumberOfIterations()) / (numPlayed));
        HashMap<String, Double> V_values=new HashMap<>();
        for (int i = 0; i < this.operators.size(); i++) {
            LowLevelHeuristic op = this.operators.get(i);
            double temp1 = 2 * Math.log(sumNt);
            double temp2 = temp1 / nt.get(op.getName());
            double temp3 = Math.sqrt(temp2);
            double value= this.calcVariance(op.getName()) + temp3;
            V_values.put(op.getName(), value);
        }
        return V_values;
    }
    
    @Override
    protected double equation(LowLevelHeuristic op, HashMap<String, Double> frr, HashMap<String, Integer> nt, double sumNt) {
        //return heuristicReward /*FRRop*/ + this.c 
        //* Math.sqrt((Math.log(getNumberOfIterations()) / (numPlayed)) * Math.min((1/4), vop));
        HashMap<String, Double> V_values=this.VCalc(nt, sumNt);
        double numerator = Math.log(sumNt);
        double denominator=nt.get(op.getName());
        double fraction=numerator/denominator;
        double vop=V_values.get(op.getName());
        double sqrt = Math.sqrt(fraction * Math.min(1/4, vop));
        double frr_value=frr.get(op.getName());
        return frr_value + this.C * sqrt;
    }
}
