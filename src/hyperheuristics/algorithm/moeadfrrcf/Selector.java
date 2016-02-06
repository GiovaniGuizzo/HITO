/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrcf;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author vinicius
 */
public class Selector {

    protected ArrayList<LowLevelHeuristic> operators;
    protected double alpha;
    protected double beta;
    protected String last;
    protected int sequence;
    private int qtdStarted;

    public Selector(ArrayList<LowLevelHeuristic> operators, double alpha, double beta) {
        this.operators = operators;
        this.alpha = alpha;
        this.beta = beta;
        this.last = "";
        this.qtdStarted = 0;
    }

    private void incrementEstimatedTime(String opName, HashMap<String, Integer> estimatedTime) {
        for (LowLevelHeuristic op : this.operators) {
            if (opName != op.getName()) {
                estimatedTime.put(op.getName(), estimatedTime.get(op.getName()) + 1);
            } else {
                estimatedTime.put(op.getName(), 0);
            }
        }
    }

    public LowLevelHeuristic selectOperator(HashMap<String, Double> frr, HashMap<String, Integer> estimatedTime) {
        if (this.qtdStarted < operators.size()) {
            return this.operators.get(this.qtdStarted++);
        }
        ArrayList<Integer> selected = new ArrayList<>();
        double biggervalue = Double.NEGATIVE_INFINITY;
        //System.out.println("------------------------------------------------------------------------");
        for (int i = 0; i < this.operators.size(); i++) {
            LowLevelHeuristic op = this.operators.get(i);
            double performance = frr.get(op.getName());
            double value = this.alpha * performance + this.beta * estimatedTime.get(op.getName());
            //System.out.println(op.getName() + " value=" + this.alpha + "*" + performance + "+" + this.beta + "*" + estimatedTime.get(op.getName()) + "=" + value);
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
        this.incrementEstimatedTime(llh.getName(), estimatedTime);
        if (llh.getName() == last) {
            sequence++;
        } else {
            sequence = 0;
            last = llh.getName();
        }

        //System.out.println("Chosen " + llh.getName() + " " + sequence);

        //System.out.println("------------------------------------------------------------------------");
        return llh;

    }
}
