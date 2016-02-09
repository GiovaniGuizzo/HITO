/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 *
 * @author vinicius
 */
public class UCBSelector {

    protected ArrayList<LowLevelHeuristic> operators;
    protected double C;
    private boolean alltried;

    public UCBSelector(ArrayList<LowLevelHeuristic> operators, double C) {
        this.operators = operators;
        this.C = C;
        this.alltried = false;
    }

    protected double sumNt(HashMap<String, Integer> nt) {
        int qtd = 0;
        for (LowLevelHeuristic op : this.operators) {
            qtd += nt.get(op.getName());
        }
        return qtd;
    }

    protected double equation(LowLevelHeuristic op, HashMap<String, Double> frr, HashMap<String, Integer> nt, double sumNt) {
        /*  KE LI code
         http://www.cs.cityu.edu.hk/~51888309/code/bandits.zip
         temp1 = 2 * Math.log(total_usage);
         temp2 = temp1 / strategy_usage[i];
         temp3 = Math.sqrt(temp2);
         quality[i] = rewards[i] + scale_ * temp3;
         */

        double numerator = 2 * Math.log(((int)sumNt));
        double denominator=nt.get(op.getName());
        double fraction=numerator/denominator;
        double sqrt = Math.sqrt(fraction);
        double frr_value=frr.get(op.getName());
        return frr_value + this.C * sqrt;
    }

    public LowLevelHeuristic selectOperator(HashMap<String, Double> frr, HashMap<String, Integer> nt) {
        if (!this.alltried) {
            for (int i = 0; i < this.operators.size(); i++) {
                LowLevelHeuristic op = this.operators.get(i);
                if (nt.get(op.getName()) == 0) {
                    //never tried
                    return op;
                }
            }
            this.alltried = true;
        }

        ArrayList<Integer> selected = new ArrayList<>();
        double biggervalue = Double.NEGATIVE_INFINITY;
        double sumNt = this.sumNt(nt);

        for (int i = 0; i < this.operators.size(); i++) {
            LowLevelHeuristic op = this.operators.get(i);
            double value=this.equation(op, frr, nt, sumNt);
            if(value > biggervalue){
                biggervalue = value;
                selected=new ArrayList<>();
                selected.add(i);
            }
            else if (value == biggervalue) {
                selected.add(i);
            }
        }
        Random rand = new Random();
        int p = rand.nextInt(selected.size());
        int select = selected.get(p);
        LowLevelHeuristic llh = this.operators.get(select);
        return llh;
    }
}
