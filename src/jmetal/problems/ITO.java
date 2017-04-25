package jmetal.problems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.solutionType.PermutationSolutionType;
import jmetal.base.variable.Permutation;
import jmetal.util.JMException;

/**
 *
 * @author Giovani Guizzo
 */
public class ITO extends Problem {

    protected HashMap<Integer, List<String>> methodMatrix;
    protected HashMap<Integer, List<String>> attributeMatrix;
    protected HashMap<Integer, List<String>> extensionMatrix;
    protected Set<Integer> uniqueUnitIds;

    public ITO(String methodMatrixFile) throws ClassNotFoundException {
        this.methodMatrix = new HashMap<>();
        this.attributeMatrix = new HashMap<>();
        this.extensionMatrix = new HashMap<>();
        this.uniqueUnitIds = new HashSet();
        this.readFile(methodMatrixFile);

        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "ITO";
        solutionType_ = new PermutationSolutionType(this);
        variableType_ = new Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        variableType_[0] = Class.forName("jmetal.base.variable.Permutation");
        length_[0] = uniqueUnitIds.size();
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        int methodFitness = 0;
        int attributeFitness = 0;

//        repairSolution(solution);
        Set<Integer> testedUnits = new HashSet<>();
        Set<Integer> emulatedMethods = new HashSet<>();
        Set<Integer> emulatedAttributes = new HashSet<>();
        int[] variables = ((Permutation) solution.getDecisionVariables()[0]).vector_;
        for (int unit : variables) {
            testedUnits.add(unit);

            System.out.println("Unit: " + unit);
            for (String methodDependency : methodMatrix.get(unit)) {
                String[] dependency = methodDependency.split("\\.");
                int dependentUnit = Integer.parseInt(dependency[0]);
                if (!testedUnits.contains(dependentUnit)) {
                    Integer method = Integer.parseInt(dependency[1]);
                    if (!emulatedMethods.contains(method)) {
                        emulatedMethods.add(method);
                        methodFitness++;
                    }
                }
            }

            for (String attributeDependency : attributeMatrix.get(unit)) {
                String[] dependency = attributeDependency.split("\\.");
                int dependentUnit = Integer.parseInt(dependency[0]);
                if (!testedUnits.contains(dependentUnit)) {
                    Integer attribute = Integer.parseInt(dependency[1]);
                    if (!emulatedAttributes.contains(attribute)) {
                        emulatedAttributes.add(attribute);
                        attributeFitness++;
                    }
                }
            }
        }

        solution.setObjective(0, methodFitness);
        solution.setObjective(1, attributeFitness);
    }

    private void repairSolution(Solution solution) {
        Set<Integer> testedUnits = new HashSet<>();
        int[] variables = ((Permutation) solution.getDecisionVariables()[0]).vector_;
        List<Integer> variablesList = new ArrayList<>();

        for (int i = 0; i < variables.length; i++) {
            int unit = variables[i];
            variablesList.add(unit);
        }

        for (int i = 0; i < variablesList.size(); i++) {
            Integer unit = variablesList.get(i);
            testedUnits.add(unit);
            List<String> extendedUnits = extensionMatrix.get(unit);
            boolean decrement = false;
            if (extendedUnits != null) {
                for (String extendedUnit : extendedUnits) {
                    Integer extendedUnitId = Integer.parseInt(extendedUnit.trim());
                    if (!testedUnits.contains(extendedUnitId)) {
                        testedUnits.remove(unit);
                        variablesList.remove(extendedUnitId);
                        variablesList.add(i, extendedUnitId);
                        decrement = true;
                    }
                }
            }
            if (decrement) {
                i--;
            }
        }

        int i = 0;
        for (Integer variable : variablesList) {
            variables[i++] = variable;
        }
    }

    protected final void readFile(String methodMatrixFile) {
        try (Scanner matrixScanner = new Scanner(new File(methodMatrixFile))) {
            List<HashMap<Integer, List<String>>> workingList = new ArrayList<>();
            workingList.add(methodMatrix);
            workingList.add(attributeMatrix);
            workingList.add(extensionMatrix);
            int workingIndex = 0;
            HashMap<Integer, List<String>> workingMap = workingList.get(workingIndex++);
            while (matrixScanner.hasNextLine()) {
                String line = matrixScanner.nextLine();
                if (!line.isEmpty()) {
                    if (line.equals("END")) {
                        workingMap = workingList.get(workingIndex++);
                        continue;
                    }

                    String[] split = line.split(",");
                    Integer unit = Integer.parseInt(split[0]);
                    uniqueUnitIds.add(unit);
                    ArrayList<String> listOfDependencies = new ArrayList<>();
                    for (int i = 1; i < split.length; i++) {
                        listOfDependencies.add(split[i]);
                    }
                    workingMap.put(unit, listOfDependencies);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ITO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMethodMapAsString() {
        return getMapAsString(methodMatrix);
    }

    public String getAttributeMapAsString() {
        return getMapAsString(attributeMatrix);
    }

    public String getExtensionMapAsString() {
        return getMapAsString(extensionMatrix);
    }

    private String getMapAsString(HashMap<Integer, List<String>> map) {
        String result = "";
        for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
            result += entry.getKey() + ",";
            for (String string : entry.getValue()) {
                result += string + ",";
            }
            result += "\n";
        }
        return result;
    }

    public String getUniqueIdsAsString() {
        String result = "";
        for (Integer uniqueUnitId : uniqueUnitIds) {
            result += uniqueUnitId + ",";
        }
        return result;
    }

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

}
