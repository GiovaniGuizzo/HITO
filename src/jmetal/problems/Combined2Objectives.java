package jmetal.problems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import jmetal.base.*;
import jmetal.base.solutionType.PermutationSolutionType;
import jmetal.base.variable.Permutation;

public class Combined2Objectives extends CITO_CAITO {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Combined2Objectives(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "CITO";
        solutionType_ = new PermutationSolutionType(this);
        variableType_ = new Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        variableType_[0] = Class.forName("jmetal.base.variable.Permutation");

        this.readProblem(filename);

        length_[0] = numberOfElements_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    @Override
    public void evaluate(Solution solution) {
        double fitness0 = 0.0;
        double fitness1 = 0.0;
        ArrayList<Integer> alreadyComputedModules = new ArrayList();
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;

        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
            for (int moduleIndex = 0; moduleIndex < clusters.get(clusterIndex).modules.size(); moduleIndex++) {
                int moduleId = clusters.get(clusterIndex).modules.get(moduleIndex);

                for (int dependencyModuleId = 0; dependencyModuleId < numberOfElements_; dependencyModuleId++) {
                    if (dependency_matrix_[moduleId][dependencyModuleId] == 1) {

                        if (alreadyComputedModules.indexOf(dependencyModuleId) == -1) {
                            fitness0 += attribute_coupling_matrix_[moduleId][dependencyModuleId];
                            fitness1 += method_coupling_matrix_[moduleId][dependencyModuleId];
                        }
                    }
                }

                alreadyComputedModules.add(moduleId);
            }
        }

        solution.setObjective(0, fitness0);
        solution.setObjective(1, fitness1);
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
