/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author giovani
 */
public class FriedmanTest {

    public static HashMap<String, HashMap<String, Boolean>> test(HashMap<String, double[]> values) throws IOException, InterruptedException {
        String script = "";
        for (Map.Entry<String, double[]> entrySet : values.entrySet()) {
            String key = entrySet.getKey();
            double[] keyValues = entrySet.getValue();
            script += key + " <- c(";
            for (Double value : keyValues) {
                script += value + ",";
            }
            script = script.substring(0, script.lastIndexOf(",")) + ")";

            script += "\n";
        }

        script += "require(pgirmess)\n";
        script += "VECTOR <- cbind(";
        for (Map.Entry<String, double[]> entrySet : values.entrySet()) {
            String key = entrySet.getKey();
            script += key + ",";
        }
        script = script.substring(0, script.lastIndexOf(","));
        script += ")\n";
        script += "result <- friedman.test(VECTOR)\n";
        script += "m <- data.frame(result$statistic,result$p.value)\n";
        script += "pos_teste <- friedmanmc(VECTOR)\n";
        script += "print(pos_teste)";

        String tempInputPath = "temp" + System.nanoTime() + ".txt";
        String tempOutputPath = "temp" + System.nanoTime() + ".txt";

        try (FileWriter scriptWriter = new FileWriter(tempInputPath)) {
            scriptWriter.append(script);
        }
        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");

        File tempOutput = new File(tempOutputPath);
        tempOutput.deleteOnExit();
        processBuilder.redirectOutput(tempOutput);

        File tempInput = new File(tempInputPath);
        tempInput.deleteOnExit();
        processBuilder.redirectInput(tempInput);

        Process process = processBuilder.start();
        process.waitFor();

        Scanner scanner = new Scanner(tempOutput);

        ArrayList<Map.Entry<String, double[]>> entrySets = new ArrayList<>(values.entrySet());

        HashMap<String, HashMap<String, Boolean>> result = new HashMap<>();

        for (int i = 0; i < entrySets.size() - 1; i++) {
            String entry1 = entrySets.get(i).getKey();
            for (int j = i + 1; j < entrySets.size(); j++) {
                String entry2 = entrySets.get(j).getKey();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("TRUE") || line.contains("FALSE")) {
                        HashMap<String, Boolean> entry1Map = result.get(entry1);
                        if (entry1Map == null) {
                            entry1Map = new HashMap<>();
                            result.put(entry1, entry1Map);
                        }
                        HashMap<String, Boolean> entry2Map = result.get(entry2);
                        if (entry2Map == null) {
                            entry2Map = new HashMap<>();
                            result.put(entry2, entry2Map);
                        }
                        if (line.contains("TRUE")) {
                            entry1Map.put(entry2, true);
                            entry2Map.put(entry1, true);
                            break;
                        } else {
                            entry1Map.put(entry2, false);
                            entry2Map.put(entry1, false);
                            break;
                        }
                    }
                }
            }
        }

        tempInput.delete();
        tempOutput.delete();

        return result;
    }

}
