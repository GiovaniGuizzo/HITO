package jmetal.problems;

import java.io.FileNotFoundException;
import java.io.IOException;
import jmetal.base.*;
import jmetal.base.solutionType.PermutationSolutionType;
import jmetal.base.variable.Permutation;

public class Combined4Objectives extends CITO_CAITO {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Combined4Objectives(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        numberOfVariables_ = 1;
        numberOfObjectives_ = 4;
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
        double fitness2 = 0.0;
        double fitness3 = 0.0;
        boolean verificador;
        int x;
        int y;

        //percorre o vetor de solucoes
        for (int i = 0; i < numberOfElements_; i++) {

            //pega o id da classe
            x = ((Permutation) solution.getDecisionVariables()[0]).vector_[i];

            //percorre as colunas da matrix de dependencia
            for (int k = 0; k < numberOfElements_; k++) {

                //verifica se existe dependencia de x para k
                if (dependency_matrix_[x][k] == 1) {
                    verificador = false;

                    //verifica se a classe já exite
                    for (int j = 0; j <= i; j++) {
                        y = ((Permutation) solution.getDecisionVariables()[0]).vector_[j];
                        if (y == k) {
                            verificador = true;
                        }
                    }

                    //adiciona os valores ao fitnesse se a classe não tiver sido testada ainda
                    if (verificador == false) {
                        fitness0 += attribute_coupling_matrix_[x][k];
                        fitness1 += method_coupling_matrix_[x][k];
                        fitness2 += method_return_type_matrix_[x][k];
                        fitness3 += method_param_type_matrix_[x][k];

                    }
                }
            }
        }

        //System.out.println(fitness0+" "+fitness1+" "+fitness2+" "+fitness3);

        solution.setObjective(0, fitness0);
        solution.setObjective(1, fitness1);
        solution.setObjective(2, fitness2);
        solution.setObjective(3, fitness3);
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
