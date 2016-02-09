/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.ArrayList;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 *
 * @author vinicius
 */
public class UCBSelectorVariance extends UCBSelector{

    private SlidingWindow slidingwindow;
    public UCBSelectorVariance(ArrayList<LowLevelHeuristic> operators, double C, SlidingWindow slidingwindow) {
        super(operators, C);
        this.slidingwindow=slidingwindow;
    }
    
    protected double calcVariance(String op){
        ArrayList<String[]> slOp=slidingwindow.getWindowforOp(op);
        if(slOp.size() > 1){
            ArrayList<Double> values=new ArrayList<>();
            for (int i = 0; i < slOp.size(); i++) {
                String[] data=slOp.get(i);
                double FIR = Double.valueOf(data[1]);
                values.add(FIR);
            }
            double[] arr=ArrayUtils.toPrimitive(values.toArray(new Double[0]));
            return this.calcVariance(arr);
        }
        return 1.0;
    }
    
    
    protected double calcVariance(double[] values){
        Variance variancia=new Variance();
        return variancia.evaluate(values);
    }
    
}
