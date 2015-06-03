package jmetal.experiments.indicators;

import java.io.*;
import java.util.ArrayList;

public class CalculaCustoStubs {

    public int[][] constraint_matrix_;
    public ArrayList<Integer> aspects_;
    public int numberOfElements_;
    public int[][] dependency_matrix_;
    public int[][] attribute_coupling_matrix_;
    public int[][] method_coupling_matrix_;
    public int[][] method_return_type_matrix_;
    public int[][] method_param_type_matrix_;
    public int totalGeneralStubs;
    public int totalClassesStubs;
    public int totalAspectsStubs;
    public int totalSolutions;

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

            // Show matrixes ---------------------------------------------------
//            this.showMatrix(this.dependency_matrix_, "dependency");
//            this.showMatrix(this.attribute_coupling_matrix_, "attribute");
//            this.showMatrix(this.method_coupling_matrix_, "method");
//            this.showMatrix(this.method_return_type_matrix_, "return");
//            this.showMatrix(this.method_param_type_matrix_, "parameter");
//            System.out.println("Aspects: " + aspects_.toString());

        } catch (Exception e) {
            System.err.println("CITOProblem.readProblem():" + e);
            System.exit(1);
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void readSolution(String fileName) throws FileNotFoundException, IOException {
        Reader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        StreamTokenizer token = new StreamTokenizer(inputFile);


        token.nextToken();
        while (true) {

            if ((token.sval != null) && (token.sval.compareTo("END") == 0)) {
                break;
            }
            
            this.totalSolutions++;
            int[] vector = new int[numberOfElements_];

            try {

                for (int i = 0; i < numberOfElements_; i++) {
                    vector[i] = ((int) token.nval);
                    token.nextToken();
                    System.out.print(vector[i] + " ");
                }
                System.out.println();
                System.out.println(this.aspects_);
                System.out.println("Modules: " + vector.length + " - Aspects: " + this.aspects_.size());
                System.out.println();

            } catch (Exception e) {
                System.err.println("CITOProblem.readSolution():" + e);
                System.exit(1);
            }


            evaluateSolution(vector);

        }

        float averageGeneralStubs = (float) this.totalGeneralStubs/this.totalSolutions;
        float averageClassStubs = (float) this.totalClassesStubs/this.totalSolutions;
        float averageAspectsStubs = (float) this.totalAspectsStubs/this.totalSolutions;
        System.out.println("\n------------------------------------------");
        System.out.println("Total Solutions......: " + this.totalSolutions);
        System.out.println("Average General Stubs..: " + averageGeneralStubs + " ("+this.totalGeneralStubs+")");
        System.out.println("Average Class Stubs..: " + averageClassStubs + " ("+this.totalClassesStubs+")");
        System.out.println("Average Aspect Stubs.: " + averageAspectsStubs + " ("+this.totalAspectsStubs+")");
        System.out.println("------------------------------------------\n");

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

        this.totalGeneralStubs = 0;
        this.totalClassesStubs = 0;
        this.totalAspectsStubs = 0;
        this.totalSolutions = 0;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showMatrix(int[][] matrix, String name) {
        int line, row;
        System.out.print("\n\n---" + name + " ---\n");
        for (int i = 0; i < matrix.length; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + matrix[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    public void evaluateSolution(int[] vector) {
        double fitness0 = 0.0;
        double fitness1 = 0.0;
        boolean verificador;
        int x, y;
        int totalGeneral = 0, totalClasses = 0, totalAspects = 0;
        int attributesClasses = 0, operationClasses = 0, returnClasses = 0, paramClasses = 0;
        int attributesAspects = 0, operationAspects = 0, returnAspects = 0, paramAspects = 0;

        //percorre o vetor de solucoes
        for (int i = 0; i < numberOfElements_; i++) {

            //pega o id da classe
            x = vector[i];

            //percorre as colunas da matrix de dependencia
            for (int k = 0; k < numberOfElements_; k++) {

                //verifica se existe dependencia de x para k
                if (dependency_matrix_[x][k] == 1) {


                    verificador = false;

                    //verifica se a classe já exite
                    for (int j = 0; j <= i; j++) {
                        y = vector[j];
                        if (y == k) {
                            verificador = true;
                        }
                    }

                    //adiciona os valores ao fitnesse se a classe não tiver sido testada ainda
                    if (verificador == false) {
                        fitness0 += attribute_coupling_matrix_[x][k];
                        fitness1 += method_coupling_matrix_[x][k];

//                        System.out.print(
//                                "Criar Stub de [" + k + "] para [" + x + "]  ("
//                                + "A=" + attribute_coupling_matrix_[x][k] + ", "
//                                + "M=" + method_coupling_matrix_[x][k] + ")");

                        if (this.aspects_.indexOf(x) > -1) {
//                            System.out.print(" --> ASPECT");
                            attributesAspects += attribute_coupling_matrix_[x][k];
                            operationAspects += method_coupling_matrix_[x][k];
                            returnAspects += method_return_type_matrix_[x][k];
                            paramAspects += method_param_type_matrix_[x][k];
                            if ((attributesAspects + operationAspects + returnAspects + paramAspects) > 0) {
                                this.totalGeneralStubs++;
                                this.totalAspectsStubs++;
                                totalAspects++;
                                totalGeneral++;
                            }
                        } else {
                            attributesClasses += attribute_coupling_matrix_[x][k];
                            operationClasses += method_coupling_matrix_[x][k];
                            returnClasses += method_return_type_matrix_[x][k];
                            paramClasses += method_param_type_matrix_[x][k];
                            if ((attributesClasses + operationClasses + returnClasses + paramClasses) > 0) {
                                this.totalGeneralStubs++;
                                this.totalClassesStubs++;
                                totalClasses++;
                                totalGeneral++;
                            }
                        }
//                        System.out.println();
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Cost: A=" + fitness0 + ", O=" + fitness1);
        System.out.println("General Stubs = " + totalGeneral);
        System.out.println("Class Stubs   = " + totalClasses + " (A=" + attributesClasses + ", O=" + operationClasses + ", R=" + returnClasses + ", P=" + paramClasses + ")");
        System.out.println("Aspect Stubs  = " + totalAspects + " (A=" + attributesAspects + ", O=" + operationAspects + ", R=" + returnAspects + ", P=" + paramAspects + ")");
        System.out.println("------------------------------------------\n");
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
