package jmetal.base.variable;

import java.util.ArrayList;
import jmetal.base.Variable;
import jmetal.problems.CITO_CAITO;
import jmetal.util.Configuration.*;

public class PermutationIncremental extends Variable {

    public int[] vector_;
    public int size_;

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public PermutationIncremental(CITO_CAITO problem, int var) {
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
        //coloca aspectos no fim da ordem
        vector_ = problem.aspectosNoFimDaOrdem(vector_, problem.getAspects());

//        System.out.print("Initialized: \n");
//        for (int indexSolution = 0; indexSolution < size_; indexSolution++) {
//            System.out.print(vector_[indexSolution] + " ");
//        }
//        System.out.println("\n");
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public PermutationIncremental() {
        size_ = 0;
        vector_ = null;

    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public PermutationIncremental(PermutationIncremental permutation) {
        size_ = permutation.size_;
        vector_ = new int[size_];

        for (int i = 0; i < size_; i++) {
            vector_[i] = permutation.vector_[i];
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Variable deepCopy() {
        return new PermutationIncremental(this);
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int getLength() {
        return size_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    @Override
    public String toString() {
        String string;

        string = "+";
        for (int i = 0; i < size_; i++) {
            string += vector_[i] + " ";
        }

        return string;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
