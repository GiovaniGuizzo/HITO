/**
 * Permutation.java
 *
 * @author juanjo durillo
 * @version 1.0
 */
package jmetal.base.variable;

import java.util.ArrayList;
import jmetal.base.Variable;
import jmetal.problems.CITO_CAITO;
import jmetal.util.Configuration.*;

/**
 * Implements a permutation of integer decision variable
 */
public class Permutation extends Variable {

    /**
     * Stores a permutation of <code>int</code> values
     */
    public int[] vector_;
    /**
     * Stores the length of the permutation
     */
    public int size_;

    /**
     * Constructor
     */
    public Permutation() {
        size_ = 0;
        vector_ = null;

    } //Permutation

    /**
     * Constructor
     * @param size Length of the permutation
     * This constructor has been contributed by Madan Sathe
     */
    public Permutation(CITO_CAITO problem, int var) {
        size_ = problem.getLength(var);
        vector_ = new int[size_];

        ArrayList<Integer> randomSequence = new ArrayList<Integer>(size_);

        for (int i = 0; i < size_; i++) {
            randomSequence.add(i);
        }

        java.util.Collections.shuffle(randomSequence);

        for (int j = 0; j < randomSequence.size(); j++) {
            vector_[j] = randomSequence.get(j);
        }

        //tratar restricoes de precedencia
        vector_ = problem.tratarRestricoes(vector_, problem.getConstraintMatrix());

//        System.out.print("Initialized: \n");
//        for (int indexSolution = 0; indexSolution < size_; indexSolution++) {
//            System.out.print(vector_[indexSolution] + " ");
//        }
//        System.out.println("\n");

    } // Constructor

    /**
     * Copy Constructor
     * @param permutation The permutation to copy
     */
    public Permutation(Permutation permutation) {
        size_ = permutation.size_;
        vector_ = new int[size_];

        for (int i = 0; i < size_; i++) {
            vector_[i] = permutation.vector_[i];
        }
    } //Permutation

    /**
     * Create an exact copy of the <code>Permutation</code> object.
     * @return An exact copy of the object.
     */
    public Variable deepCopy() {
        return new Permutation(this);
    } //deepCopy

    /**
     * Returns the length of the permutation.
     * @return The length
     */
    public int getLength() {
        return size_;
    } //getNumberOfBits

    /**
     * Returns a string representing the object
     * @return The string
     */
    @Override
    public String toString() {
        String string;

        string = "+";
        for (int i = 0; i < size_; i++) {
            string += vector_[i] + " ";
        }

        return string;
    } // toString
}
