/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.base.operator.comparator;

/**
 *
 * @author giovani
 */
public class EqualSolutionsByObjectives {

    public boolean equals(double[] solutionA, double[] solutionB) {
        if (solutionA.length != solutionB.length) {
            return false;
        }
        for (int i = 0; i < solutionA.length; i++) {
            if (solutionA[i] != solutionB[i]) {
                return false;
            }
        }
        return true;
    }

}
