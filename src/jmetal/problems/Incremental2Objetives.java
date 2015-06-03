package jmetal.problems;

import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.Solution;
import jmetal.base.solutionType.PermutationIncrementalSolutionType;
import jmetal.base.variable.PermutationIncremental;

public class Incremental2Objetives extends CITO_CAITO {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Incremental2Objetives(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "CAITO";
        solutionType_ = new PermutationIncrementalSolutionType(this);
        variableType_ = new Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        variableType_[0] = Class.forName("jmetal.base.variable.PermutationIncremental");

        this.readProblem(filename);

        length_[0] = numberOfElements_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    @Override
    public void evaluate(Solution solution) {
        double fitness0 = 0.0, fitness1 = 0.0;
        boolean verificador;
        int x, y;

        //percorre o vetor de solucoes
        for (int i = 0; i < numberOfElements_; i++) {
            //pega o id da classe
            x = ((PermutationIncremental) solution.getDecisionVariables()[0]).vector_[i];
            //percorre as colunas da matrix de dependencia
            for (int k = 0; k < numberOfElements_; k++) {
                //verifica se existe dependencia de x para k
                if (dependency_matrix_[x][k] == 1) {
                    verificador = false;
                    //verifica se a classe já exite
                    for (int j = 0; j <= i; j++) {
                        y = ((PermutationIncremental) solution.getDecisionVariables()[0]).vector_[j];
                        if (y == k) {
                            verificador = true;
                        }
                    }
                    //adiciona os valores ao fitnesse se a classe não tiver sido testada ainda
                    if (verificador == false) {
                        fitness0 += attribute_coupling_matrix_[x][k];
                        fitness1 += method_coupling_matrix_[x][k];
                    }
                }
            }
        }

        solution.setObjective(0, fitness0);
        solution.setObjective(1, fitness1);
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
