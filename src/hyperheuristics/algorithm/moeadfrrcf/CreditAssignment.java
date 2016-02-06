/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrcf;

import hyperheuristics.algorithm.moeadfrrmab.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author vinicius
 */
public class CreditAssignment {

    protected HashMap<String, Double> reward;
    protected double D;

    public CreditAssignment(double D) {
        this.D = D;
        this.reward = null;
    }

    protected TreeMap<String, Double> rankReward() {
        ValueComparator bvc = new ValueComparator(reward);
        TreeMap<String, Double> sorted_map = new TreeMap<>(bvc);
        sorted_map.putAll(reward);
        return sorted_map;
    }

    protected HashMap<String, Double> decay(TreeMap<String, Double> rank) {
        HashMap<String, Double> decay = new HashMap<>();
        double decaysum = 0;
        for (int i = 0; i < rank.size(); i++) {
            Object key = rank.keySet().toArray()[i];
            String op = key.toString();
            int rankPosition = i+1;//posicao do maior para menor
            double decayvalue = Math.pow(D, rankPosition) * reward.get(op);
            decay.put(op, decayvalue);
            decaysum += decayvalue;
        }
        HashMap<String, Double> frr = new HashMap<>();
        for (Entry<String, Double> entry : decay.entrySet()) {
            String op = entry.getKey();
            Double value = entry.getValue();
            frr.put(op, value / decaysum);
        }
        return frr;
    }

    public HashMap<String, Double> calcFRR(HashMap<String, Double> fir) {
        this.reward = fir;
        TreeMap<String, Double> rank = this.rankReward();//Descrescente
        return this.decay(rank);
    }
}
