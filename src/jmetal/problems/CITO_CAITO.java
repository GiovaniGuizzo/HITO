package jmetal.problems;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import jmetal.base.*;
import jmetal.base.variable.Permutation;

public class CITO_CAITO extends Problem {

    public int numberOfElements_;
    public int[][] constraint_matrix_;
    public int[][] dependency_matrix_;
    public int[][] attribute_coupling_matrix_;
    public int[][] method_coupling_matrix_;
    public int[][] method_return_type_matrix_;
    public int[][] method_param_type_matrix_;
    public ArrayList<Integer> aspects_;
    public ArrayList<ArrayList<Integer>> clusters_;

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void evaluate(Solution solution) {
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int[][] getConstraintMatrix() {
        return constraint_matrix_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public ArrayList<Integer> getAspects() {
        return aspects_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void readProblem(String fileName) throws FileNotFoundException, IOException {
        Reader inputFile = new BufferedReader(
                new InputStreamReader(
                new FileInputStream(fileName)));
        StreamTokenizer token = new StreamTokenizer(inputFile);
        int lineNumber;

        try {

            // Find the string DIMENSION ---------------------------------------
            while (true) {
                token.nextToken();
                if ((token.sval != null) && ((token.sval.compareTo("DIMENSION") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
            }//while
            token.nextToken();
            numberOfElements_ = (int) token.nval;
            this.initializeMatrixes();

            // Find the string DEPENDENCY --------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("DEPENDENCY") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfElements_; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();

                        dependency_matrix_[line][row] = 1;
                        if ((token.sval.compareTo("I") == 0) || (token.sval.compareTo("It") == 0) || (token.sval.compareTo("Ag") == 0)) {
                            // I, It, Ag = constraint
                            constraint_matrix_[line][row] = 1;
//                            System.out.println((line+1)+" -> "+(row+1)+';');
                        }
                    }
                    token.nextToken();
                }
                lineNumber = token.lineno();
            }

            // Find the string ATTRIBUTE ---------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("ATTRIBUTE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }//while
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfElements_; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        attribute_coupling_matrix_[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHOD ------------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHOD") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfElements_; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        method_coupling_matrix_[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHODRETURNTYPE --------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHODRETURNTYPE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfElements_; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        method_return_type_matrix_[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string METHODPARAMTYPE ---------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("METHODPARAMTYPE") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            lineNumber = token.lineno();

            for (int i = 0; i <= numberOfElements_; i++) {
                boolean lineIndex = true;
                int line = 0, row = 0;

                while (token.lineno() == lineNumber) {
                    if (lineIndex) {
                        line = ((int) token.nval) - 1;
                        lineIndex = false;
                    } else {
                        row = ((int) token.nval) - 1;
                        token.nextToken();
                        method_param_type_matrix_[line][row] = (int) token.nval;
                    }
                    token.nextToken();
                }

                lineNumber = token.lineno();
            }

            // Find the string ASPECTS ---------------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("ASPECTS") == 0) || (token.sval.compareTo("END") == 0))) {
                    lineNumber = token.lineno() + 1;
                    break;
                }
                token.nextToken();
            }
            token.nextToken();
            while (token.lineno() == lineNumber) {
                aspects_.add(((int) token.nval) - 1);
                token.nextToken();
            }

            // Find the string CLUSTERS ---------------------------------
            while (true) {
                if ((token.sval != null) && ((token.sval.compareTo("CLUSTERS") == 0) || (token.sval.compareTo("END") == 0))) {
                    break;
                }
                token.nextToken();
            }
            token.nextToken();
            while (true) {
                if ((token.sval != null) && (token.sval.compareTo("END") == 0)) {
                    break;
                }

                lineNumber = token.lineno();
                ArrayList<Integer> cluster = new ArrayList<Integer>();

                while (lineNumber == token.lineno()) {
                    cluster.add(((int) token.nval) - 1);
                    token.nextToken();
                }

                clusters_.add(cluster);
                //System.out.println(cluster);
            }

        } catch (Exception e) {
            System.err.println("CITOProblem.readProblem():" + e);
            System.exit(1);
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void initializeMatrixes() {
        //create instances of matrixes
        this.dependency_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.attribute_coupling_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.method_coupling_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.method_return_type_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.method_param_type_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.constraint_matrix_ = new int[numberOfElements_][numberOfElements_];
        this.aspects_ = new ArrayList<Integer>();
        this.clusters_ = new ArrayList<ArrayList<Integer>>();

        //initialize matrixes with value 0
        for (int i = 0; i < numberOfElements_; i++) {
            for (int j = 0; j < numberOfElements_; j++) {
                this.dependency_matrix_[i][j] = 0;
                this.attribute_coupling_matrix_[i][j] = 0;
                this.method_coupling_matrix_[i][j] = 0;
                this.method_return_type_matrix_[i][j] = 0;
                this.method_param_type_matrix_[i][j] = 0;
                this.constraint_matrix_[i][j] = 0;
            }
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showMatrix(int mat[][]) {
        int line, row;
        System.out.print("\n\n--- --- --- --- --- ---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (mat[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + mat[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }


                if (dominador) {
//                    System.out.println("--------------------------------------------");
//                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
//                    System.out.println("[" + i + "] " + result.get(i).toString());
//                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
//                    System.out.println("--------------------------------------------");
//                    System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
//                    System.out.println("[" + i + "] " + result.get(i).toString());
//                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public SolutionSet removeRepetidas(SolutionSet result) {
        String solucao;

        for (int i = 0; i < result.size() - 1; i++) {
            solucao = result.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < result.size(); j++) {
                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
//                    System.out.println("--------------------------------------------");
//                    System.out.println("Solucao [" + i + "] e igual [" + j + "]");
//                    System.out.println(result.get(i).getDecisionVariables()[0].toString());
//                    System.out.println(result.get(j).getDecisionVariables()[0].toString());

                    result.remove(j);
                }
            }
        }

        return result;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int[] putEnd(int haystack[], int index) {
        int temp = haystack[index];

        for (int i = index; i < haystack.length - 1; i++) {
            haystack[i] = haystack[i + 1];
        }

        haystack[haystack.length - 1] = temp;

        return haystack;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Solution putClusterEnd(Solution solution, int clusterIndex) {
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;
        Cluster temp = clusters.get(clusterIndex);

//        System.out.println("cluster [" + clusters.get(clusterIndex).id + "] to end");
//        System.out.println(clusters);

        clusters.remove(clusterIndex);
        clusters.add(temp);

//        System.out.println(clusters);
        
        return solution;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Solution putModuleEnd(Solution solution, int clusterIndex, int moduleIndex) {
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;
        int temp = clusters.get(clusterIndex).modules.get(moduleIndex);

//        System.out.println("cluster [" + clusters.get(clusterIndex).id + "] module [" + clusters.get(clusterIndex).modules.get(moduleIndex) + "] to end");
//        System.out.println(clusters);

        clusters.get(clusterIndex).modules.remove(moduleIndex);
        clusters.get(clusterIndex).modules.add(temp);

//        System.out.println(clusters);

        return solution;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Solution tratarRestricoes(Solution solution, int contraints_[][]) {

//        System.out.println("iniciou");

        solution = treatConstraintsInsideCluster(solution, contraints_);
        solution = treatConstraintsAmoungClusters(solution, contraints_);

//        System.out.println(((Permutation) solution.getDecisionVariables()[0]).clusters_);
//        System.out.println("passou");

        /*
        ArrayList subVector = new ArrayList();
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;

        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
            for (int moduleIndex = 0; moduleIndex < clusters.get(clusterIndex).modules.size(); moduleIndex++) {

                //pega o id da classe para buscar as restricoes
                int moduleId = clusters.get(clusterIndex).modules.get(moduleIndex);
                boolean addInSubVector = true;

                //passa por todas as classes para verificar restricao com a classe atual
                for (int constraintIndex = 0; constraintIndex < contraints_[moduleId].length; constraintIndex++) {
                    //verifica se existe restricao
                    if (contraints_[moduleId][constraintIndex] == 1) {
                        //verifica se a classe exigida ja apareceu anteriormente
                        if (subVector.indexOf(constraintIndex) == -1) {

                            System.out.println("");
                            System.out.println(clusters.get(clusterIndex).modules);

                            boolean constraintInTheSameCluster = false;
                            for (int clusterModuleIndex = moduleIndex + 1; clusterModuleIndex < clusters.get(clusterIndex).modules.size(); clusterModuleIndex++) {
                                if (clusters.get(clusterIndex).modules.get(clusterModuleIndex) == constraintIndex) {
                                    System.out.println(" --- achou --- ");
                                    constraintInTheSameCluster = true;
                                }
                                //System.out.print(clusters.get(clusterIndex).modules.get(clusterModuleIndex)+" ");
                            }
                            if (constraintInTheSameCluster) {
                                solution = this.putModuleEnd(solution, clusterIndex, moduleIndex);
                                addInSubVector = false;
                                moduleIndex--;
                                break;
                            }

                        }
                    }
                }
                //adiciona o elemento no subVector
                if (addInSubVector) {
                    subVector.add(moduleId);
                }

            }
        }
         */

        return solution;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Solution treatConstraintsInsideCluster(Solution solution, int contraints_[][]) {

        ArrayList alreadyCheckedModules = new ArrayList();
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;

        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
            for (int moduleIndex = 0; moduleIndex < clusters.get(clusterIndex).modules.size(); moduleIndex++) {

                int moduleId = clusters.get(clusterIndex).modules.get(moduleIndex);
                boolean addInAlreadyCheckedModules = true;

                for (int constraintModuleId = 0; constraintModuleId < contraints_[moduleId].length; constraintModuleId++) {
                    if (contraints_[moduleId][constraintModuleId] == 1) {
                        if (alreadyCheckedModules.indexOf(constraintModuleId) == -1) {

                            boolean precedentInTheSameCluster = false;

                            for (int clusterModuleIndex = moduleIndex + 1; clusterModuleIndex < clusters.get(clusterIndex).modules.size(); clusterModuleIndex++) {
                                if (clusters.get(clusterIndex).modules.get(clusterModuleIndex) == constraintModuleId) {
                                    precedentInTheSameCluster = true;
                                }
                            }

                            if (precedentInTheSameCluster) {
                                solution = this.putModuleEnd(solution, clusterIndex, moduleIndex);
                                addInAlreadyCheckedModules = false;
                                moduleIndex--;
                                break;
                            }
                        }
                    }
                }

                if (addInAlreadyCheckedModules) {
                    alreadyCheckedModules.add(moduleId);
                }

            }
        }

        return solution;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public Solution treatConstraintsAmoungClusters(Solution solution, int contraints_[][]) {

        ArrayList alreadyCheckedModules = new ArrayList();
        ArrayList<Cluster> clusters = ((Permutation) solution.getDecisionVariables()[0]).clusters_;
        
        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {

//            System.out.println("clusterIndex " + clusterIndex);
//            System.out.println("checked "+alreadyCheckedModules);
            outerloop:
            for (int moduleIndex = 0; moduleIndex < clusters.get(clusterIndex).modules.size(); moduleIndex++) {

                int moduleId = clusters.get(clusterIndex).modules.get(moduleIndex);
                boolean addInAlreadyCheckedModules = true;

                for (int constraintModuleId = 0; constraintModuleId < contraints_[moduleId].length; constraintModuleId++) {
                    if (contraints_[moduleId][constraintModuleId] == 1) {
                        if (alreadyCheckedModules.indexOf(constraintModuleId) == -1) {

                            boolean precedentInTheSameCluster = false;

                            for (int clusterModuleIndex = moduleIndex + 1; clusterModuleIndex < clusters.get(clusterIndex).modules.size(); clusterModuleIndex++) {
                                if (clusters.get(clusterIndex).modules.get(clusterModuleIndex) == constraintModuleId) {
                                    precedentInTheSameCluster = true;
                                }
                            }

                            if (!precedentInTheSameCluster) {
//                            System.out.println("");
//                            System.out.println("o m�dulo [" + moduleId + "] depende de [" + constraintModuleId + "]");
//                            System.out.println("    " + moduleId + " ("+ Cluster.getClusterId(clusters, moduleId)+") -> " + constraintModuleId + "("+ Cluster.getClusterId(clusters, constraintModuleId)+");");

//                                System.out.println(alreadyCheckedModules);
//                                System.out.println("Envia " + clusterIndex + " = [" + clusters.get(clusterIndex).id + "] para o fim");

                                solution = this.putClusterEnd(solution, clusterIndex);

//                                addInAlreadyCheckedModules = false;
//                                moduleIndex = 0;
                                clusterIndex = -1;
//                                System.out.println("checked "+alreadyCheckedModules);
                                alreadyCheckedModules = new ArrayList();


//                                System.out.println("Breaking");
                                break outerloop;
                            }
                        }
                    }
                }

                if (addInAlreadyCheckedModules) {
                    alreadyCheckedModules.add(moduleId);
                }
            }
        }

        return solution;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
