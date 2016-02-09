/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.algorithm.moeadfrrmab;

import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author vinicius
 */
public class ValueComparator implements Comparator<String> {

    HashMap<String, Double> base;

    public ValueComparator(HashMap<String, Double> base) {
        this.base = base;
    }

     
    @Override
    public int compare(String a, String b) {
        int compare = base.get(b).compareTo(base.get(a));
        if (compare == 0) {
            return 1;
        } else {
            return compare;
        }
    }
}
