package jmetal.problems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import jmetal.base.Solution;
import jmetal.base.variable.Permutation;
import jmetal.util.JMException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Giovani Guizzo
 */
public class ITOTest {

    public ITOTest() {
    }

    @Test
    public void testMaps() throws ClassNotFoundException {
        ITO ito = new ITO("problemas/test.txt");
        String expected = "0,1.3,4.6,\n"
                + "1,0.2,\n"
                + "2,1.3,4.5,4.6,\n"
                + "3,0.1,0.2,\n"
                + "4,\n";
        Assert.assertEquals(expected, ito.getMethodMapAsString());
        Assert.assertEquals(expected, ito.getAttributeMapAsString());

        expected = "0,1,\n"
                + "1,2,4,\n"
                + "2,\n"
                + "3,\n"
                + "4,\n";
        Assert.assertEquals(expected, ito.getExtensionMapAsString());
    }

    @Test
    public void testUniqueIds() throws ClassNotFoundException {
        ITO ito = new ITO("problemas/test.txt");
        String expected = "0,1,2,3,4,";
        Assert.assertEquals(expected, ito.getUniqueIdsAsString());
    }

    @Test
    public void testRepairSolution() throws ClassNotFoundException, JMException {
        ITO ito = new ITO("problemas/test.txt");
        Solution solution = new Solution(ito);
        int[] variables = new int[]{0, 1, 2, 3, 4};
        ((Permutation) solution.getDecisionVariables()[0]).vector_ = variables;
        ito.evaluate(solution);
        int[] expected = new int[]{4, 2, 1, 0, 3};
        Assert.assertArrayEquals(expected, variables);
    }

    @Test
    public void testObjectives() throws ClassNotFoundException, JMException {
        ITO ito = new ITO("problemas/test.txt");
        Solution solution = new Solution(ito);
        int[] variables = new int[]{0, 1, 2, 3, 4};
        ((Permutation) solution.getDecisionVariables()[0]).vector_ = variables;
        ito.evaluate(solution);
        Assert.assertEquals(2, solution.getObjective(0), 0.00001);
        Assert.assertEquals(2, solution.getObjective(1), 0.00001);
    }

    @Test
    public void testObjectives2() throws ClassNotFoundException, JMException {
        ITO ito = new ITO("problemas/test2.txt");
        Solution solution = new Solution(ito);
        int[] variables = new int[]{0, 1, 2, 3, 4};
        ((Permutation) solution.getDecisionVariables()[0]).vector_ = variables;
        ito.evaluate(solution);
        Assert.assertEquals(3, solution.getObjective(0), 0.00001);
        Assert.assertEquals(3, solution.getObjective(1), 0.00001);
    }

    @Test
    public void testObjectives3() throws ClassNotFoundException, JMException {
        ITO ito = new ITO("problemas/Guava.txt");
        HashMap<Integer, List<String>> attributeMatrix = ito.getAttributeMatrix();
        HashMap<Integer, List<String>> methodMatrix = ito.getMethodMatrix();
        HashMap<Integer, List<String>> extensionMatrix = ito.getExtensionMatrix();
        HashSet<String> relationships = new HashSet<>();

        getUnitsDependencies(attributeMatrix, relationships);
        getUnitsDependencies(methodMatrix, relationships);
        getUnitsDependencies(extensionMatrix, relationships);

        System.out.println(relationships.size());
    }

    public void getUnitsDependencies(HashMap<Integer, List<String>> depMatrix, HashSet<String> relationships) throws NumberFormatException {
        for (Map.Entry<Integer, List<String>> entry : depMatrix.entrySet()) {
            Integer unit = entry.getKey();
            List<String> dependentUnits = entry.getValue();
            for (String dependentUnitString : dependentUnits) {
                String[] dependency = dependentUnitString.split("\\.");
                int dependentUnit = Integer.parseInt(dependency[0]);
                if (!relationships.contains(unit + "/" + dependentUnit) && !relationships.contains(dependentUnit + "/" + unit)) {
                    relationships.add(unit + "/" + dependentUnit);
                }
            }
        }
    }

}
