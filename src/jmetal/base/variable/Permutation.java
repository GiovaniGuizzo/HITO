package jmetal.base.variable;

import java.util.ArrayList;
import jmetal.base.Cluster;
import jmetal.base.Variable;
import jmetal.problems.CITO_CAITO;

public class Permutation extends Variable {

    public ArrayList<Cluster> clusters_;
    public int size_;

    public Permutation() {
        size_ = 0;
        clusters_ = null;
    }

    public Permutation(CITO_CAITO problem, int var) {
        size_ = problem.getLength(var);
        clusters_ = new ArrayList<Cluster>();

        for (int i = 0; i < problem.clusters_.size(); i++) {
            Cluster cluster = new Cluster();
            cluster.id = i;
            for (int j = 0; j < problem.clusters_.get(i).size(); j++) {
                cluster.modules.add(problem.clusters_.get(i).get(j));
            }
            clusters_.add(cluster);
        }

        for (int i = 0; i < problem.clusters_.size(); i++) {
            java.util.Collections.shuffle(clusters_.get(i).modules);
        }
        java.util.Collections.shuffle(clusters_);
    }

    public Permutation(Permutation permutation) {
        clusters_ = new ArrayList<Cluster>();

        for (int i = 0; i < permutation.clusters_.size(); i++) {

            int id = permutation.clusters_.get(i).id;
            ArrayList<Integer> modules = permutation.clusters_.get(i).modules;

            ArrayList<Integer> copy = new ArrayList<Integer>();
            for (int j = 0; j < modules.size(); j++) {
                copy.add(modules.get(j));
            }

            Cluster temp = new Cluster();
//            temp = permutation.clusters_.get(i);
            temp.id = id;
            temp.modules = copy;

            clusters_.add(i, temp);
        }
    }

    public Variable deepCopy() {
        return new Permutation(this);
    }

    public int getLength() {
        return size_;
    }
    
    public Cluster getCluster(int clusterId){
        for (int i = 0; i < clusters_.size(); i++) {
            if (clusters_.get(i).id == clusterId) {
                return clusters_.get(i);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String string;

        string = "+";

        for (int i = 0; i < clusters_.size(); i++) {
            for (int j = 0; j < clusters_.get(i).modules.size(); j++) {
                string += clusters_.get(i).modules.get(j) + " ";
            }
            string += "/ ";
        }

        return string;
    }
}
