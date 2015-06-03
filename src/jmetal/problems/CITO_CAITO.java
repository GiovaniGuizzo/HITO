package jmetal.problems;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.*;
import jmetal.experiments.Experiment;
import jmetal.qualityIndicator.QualityIndicator;

public class CITO_CAITO extends Problem {

    public int[][] constraint_matrix_;
    public ArrayList<Integer> aspects_;
    public int numberOfElements_;
    public int[][] dependency_matrix_;
    public int[][] attribute_coupling_matrix_;
    public int[][] method_coupling_matrix_;
    public int[][] method_return_type_matrix_;
    public int[][] method_param_type_matrix_;

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void evaluate(Solution solution) {
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int[][] getStrategy() {
        return constraint_matrix_;
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
//            this.showDepedencyMatrix();
//            this.showAttributeMatrix();
//            this.showMethodMatrix();
//            this.showMethodReturnTypeMatrix();
//            this.showMethodParamTypeMatrix();
//            this.showConstraintMatrix();
//            System.out.println("Aspects: "+aspects_.toString() );

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
    private void showDepedencyMatrix() {
        //SHOW DEPENDENCY
        int line, row;
        System.out.print("\n\n---DEPENDENCY ---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.dependency_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.dependency_matrix_[i][j] + "]");
                }
                //System.out.print("["+this.dependency_matrix_[i][j]+"] ");
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showAttributeMatrix() {
        //SHOW ATTRIBUTE
        int line, row;
        System.out.print("\n\n---ATTRIBUTE ---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.attribute_coupling_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.attribute_coupling_matrix_[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showMethodMatrix() {
        //SHOW METHOD
        int line, row;
        System.out.print("\n\n---METHOD ---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.method_coupling_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.method_coupling_matrix_[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showMethodReturnTypeMatrix() {
        //SHOW METHOD
        int line, row;
        System.out.print("\n\n---METHOD RETURN TYPE---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.method_return_type_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.method_return_type_matrix_[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showMethodParamTypeMatrix() {
        //SHOW METHOD
        int line, row;
        System.out.print("\n\n---METHOD PARAM TYPE---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.method_param_type_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.method_param_type_matrix_[i][j] + "]");
                }
            }
            System.out.println("");
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    private void showConstraintMatrix() {
        //SHOW CONSTRAINT
        int line, row;
        System.out.print("\n\n---CONSTRAINT ---\n");
        for (int i = 0; i < numberOfElements_; i++) {
            line = i + 1;
            System.out.print("[" + line + "] =>");
            for (int j = 0; j < numberOfElements_; j++) {
                if (this.constraint_matrix_[i][j] != 0) {
                    row = j + 1;
                    System.out.print(" [" + row + ":" + this.constraint_matrix_[i][j] + "]");
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
    public void gravaCompleto(SolutionSet result, String nomeArquivo) {
        try {
            FileOutputStream fos = new FileOutputStream("resultado/paes/" + nomeArquivo);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            int numberOfVariables = result.get(0).getDecisionVariables().length;
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.write(result.get(i).getDecisionVariables()[j].toString() + " - " + result.get(i).toString());
                }
                bw.newLine();
            }

            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public void geraIndicadores(Problem problem, String file, SolutionSet result) {
        QualityIndicator indicators;
        indicators = new QualityIndicator(problem, "resultado/paes/" + file);

        //HV
        if (true) {
            double value = indicators.getHypervolume(result);
            FileWriter os;
            try {
                os = new FileWriter("resultado/paes/medidas/HV_paes", true);
                os.write("" + value + "\n");
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //SPREAD
        if (true) {
            FileWriter os = null;
            try {
                double value = indicators.getSpread(result);
                os = new FileWriter("resultado/paes/medidas/SPREAD_paes", true);
                os.write("" + value + "\n");
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //IGD
        if (true) {
            FileWriter os = null;
            try {
                double value = indicators.getIGD(result);
                os = new FileWriter("resultado/paes/medidas/IGD_paes", true);
                os.write("" + value + "\n");
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //EPSILON
        if (true) {
            FileWriter os = null;
            try {
                double value = indicators.getEpsilon(result);
                os = new FileWriter("resultado/paes/medidas/EPSILON_paes", true);
                os.write("" + value + "\n");
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(Experiment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
    public int[] putEndCluster(int haystack[], int indexPos, int endPos) {
        int temp = haystack[indexPos];

        for (int i = indexPos; i < endPos; i++) {
            haystack[i] = haystack[i + 1];
        }

        haystack[endPos] = temp;

        return haystack;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int[] tratarRestricoes(int vector_[], int contraints_[][]) {

        int size_ = vector_.length;
        ArrayList subVector = new ArrayList();

        //System.out.println("Tamanho Vetor: " + size_);

        for (int indexSolution = 0; indexSolution < size_; indexSolution++) {
            //pega o id da classe para buscar as restricoes
            int contraintClassId = vector_[indexSolution];
            boolean addInSubVector = true;
            //passa por todas as classes para verificar restricao com a classe atual
            for (int indexConstraint = 0; indexConstraint < contraints_[contraintClassId].length; indexConstraint++) {
                //verifica se existe restricao
                if (contraints_[contraintClassId][indexConstraint] == 1) {
                    //verifica se a classe exigida já apareceu anteriormente
                    int x = subVector.indexOf(indexConstraint);
                    if (x == -1) {
                        vector_ = this.putEnd(vector_, indexSolution);
                        addInSubVector = false;
                        indexSolution--;
                        break;
                    }
                }
            }
            //adiciona o elemento no subVector
            if (addInSubVector) {
                subVector.add(vector_[indexSolution]);
            }
        }

        return vector_;
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public int[] tratarRestricoesIncremental(int vector_[], int contraints_[][], ArrayList<Integer> aspects) {

        int permutationLength = vector_.length;
        int aspectsLength = aspects.size();
        int classesClusterEndPos = (permutationLength - aspectsLength) - 1;
        int aspectsClusterEndPos = permutationLength - 1;
        int endPos = 0;
        ArrayList subVector = new ArrayList();

        for (int indexSolution = 0; indexSolution < permutationLength; indexSolution++) {
            //pega o id da classe para buscar as restricoes
            int contraintClassId = vector_[indexSolution];
            boolean addInSubVector = true;
            //passa por todas as classes para verificar restricao com a classe atual
            for (int indexConstraint = 0; indexConstraint < contraints_[contraintClassId].length; indexConstraint++) {
                //verifica se existe restricao
                if (contraints_[contraintClassId][indexConstraint] == 1) {
                    //verifica se a classe exigida já apareceu anteriormente
                    int x = subVector.indexOf(indexConstraint);
                    if (x == -1) {
                        if (indexSolution <= classesClusterEndPos) {
                            endPos = classesClusterEndPos;
                        } else if (indexSolution <= aspectsClusterEndPos) {
                            endPos = aspectsClusterEndPos;
                        }
                        //System.out.println(vector_[indexSolution]+" / "+contraintClassId+" / "+indexConstraint);
                        vector_ = this.putEndCluster(vector_, indexSolution, endPos);
                        addInSubVector = false;
                        indexSolution--;
                        break;
                    }
                }
            }
            //adiciona o elemento no subVector
            if (addInSubVector) {
                subVector.add(vector_[indexSolution]);
            }
        }

        return vector_;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    public int[] aspectosNoFimDaOrdem(int vector_[], ArrayList<Integer> aspects) {

        int lastPossition = vector_.length - aspects.size();

        for (int indexSolution = 0; indexSolution < lastPossition; indexSolution++) {
            if (aspects.indexOf(vector_[indexSolution]) > -1) {
                //System.out.println("Aspecto foi para o fim: "+vector_[indexSolution]);
                vector_ = this.putEnd(vector_, indexSolution);
                indexSolution--;
            }
        }

        return vector_;
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
