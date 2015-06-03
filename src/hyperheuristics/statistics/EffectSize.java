package hyperheuristics.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EffectSize {

    public static final String NEGLIGIBLE = "negligible";
    public static final String SMALL = "small";
    public static final String MEDIUM = "medium";
    public static final String LARGE = "large";

    public static HashMap<String, HashMap<String, Double>> computeEffectSize(HashMap<String, double[]> values) throws IOException, InterruptedException {
        HashMap<String, HashMap<String, Double>> result = new HashMap<>();

        StringBuilder script = new StringBuilder();
        script.append("require(\"effsize\")\n");

        for (Map.Entry<String, double[]> entrySet : values.entrySet()) {
            String group = entrySet.getKey();
            double[] groupValues = entrySet.getValue();

            script.append(group).append(" <- c(");
            for (double value : groupValues) {
                script.append(value).append(",");
            }
            script.deleteCharAt(script.length() - 1).append(")\n");
        }

        String[] groupArray = (String[]) values.keySet().toArray(new String[0]);

        for (int i = 0; i < groupArray.length - 1; i++) {
            String groupA = groupArray[i];
            for (int j = i + 1; j < groupArray.length; j++) {
                String groupB = groupArray[j];
                script.append("cohen.d(").append(groupA).append(",").append(groupB).append(")\n");
            }
        }
        script.append("q()");

        File inputFile = File.createTempFile("script", ".R");
        File outputFile = File.createTempFile("output", ".R");
        inputFile.deleteOnExit();
        outputFile.deleteOnExit();

        new FileWriter(inputFile).append(script).flush();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("R", "--slave", "-f", inputFile.getAbsolutePath());
        builder.redirectOutput(outputFile);
        builder.start().waitFor();

        Scanner scanner = new Scanner(outputFile);

        List<Double> comparisonValues = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("d estimate:")) {
                String[] split = line.split(" ");

                Double doubleValue = Double.parseDouble(split[2]);
                if (doubleValue.isNaN()) {
                    doubleValue = 0.0;
                }

                comparisonValues.add(doubleValue);
            }
        }

        Iterator<Double> iterator = comparisonValues.iterator();
        for (int i = 0; i < groupArray.length - 1; i++) {
            String groupA = groupArray[i];
            for (int j = i + 1; j < groupArray.length; j++) {
                String groupB = groupArray[j];
                HashMap<String, Double> groupAMap = result.get(groupA);
                if (groupAMap == null) {
                    groupAMap = new HashMap<>();
                    result.put(groupA, groupAMap);
                }
                HashMap<String, Double> groupBMap = result.get(groupB);
                if (groupBMap == null) {
                    groupBMap = new HashMap<>();
                    result.put(groupB, groupBMap);
                }
                Double value = iterator.next();
                groupAMap.put(groupB, value);
                groupBMap.put(groupA, value * -1);
            }
        }

        inputFile.delete();
        outputFile.delete();

        return result;
    }

    public static String interpretEffectSize(double effectSize) {
        effectSize = StrictMath.abs(effectSize);
        if (effectSize < 0.2) {
            return NEGLIGIBLE;
        } else if (effectSize < 0.5) {
            return SMALL;
        } else if (effectSize < 0.8) {
            return MEDIUM;
        } else {
            return LARGE;
        }
    }
}
