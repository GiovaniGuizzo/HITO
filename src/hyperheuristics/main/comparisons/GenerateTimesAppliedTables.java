/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.main.comparisons;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author giovani
 */
public class GenerateTimesAppliedTables {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        int[] numberOfObjectivesArray = new int[]{2, 4};
        int executions = 30;
        String[] problems = new String[]{
            "OO_MyBatis",
            "OA_AJHsqldb",
            "OA_AJHotDraw",
            "OO_BCEL",
            "OO_JHotDraw",
            "OA_HealthWatcher",
            //                "OA_TollSystems",
            "OO_JBoss"
        };
        String[] heuristicFunctions = new String[]{
            LowLevelHeuristic.CHOICE_FUNCTION,
//            LowLevelHeuristic.MULTI_ARMED_BANDIT
        };

        String[] algorithms = new String[]{
            "NSGA-II",
//            "SPEA2"
        };

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        try (FileWriter fileWriter = new FileWriter("experiment/TIMES_APPLIED_TABLES.txt")) {
            for (int numberOfObjectives : numberOfObjectivesArray) {
                for (String algorithm : algorithms) {
                    fileWriter.append("\\begin{table}[!htb]\n"
                            + "\t\\centering\n"
                            //                            + "\t\\fontsize{8pt}{10pt}\\selectfont\n"
                            + "\t\\def\\arraystretch{1.5}\n"
                            + "\t\\caption{Average number of low-level heuristic applications by " + algorithm + " with " + numberOfObjectives + " objectives}\n"
                            + "\t\\label{tab:Times Applied " + algorithm + " " + numberOfObjectives + " objectives}\n"
                            + "\t\\begin{tabulary}{\\textwidth}{cCCCCCCCCC}\n");
                    fileWriter.append("\t\t\\toprule\n\t\t\\textbf{System}");
                    for (int i = 0; i < 9; i++) {
                        fileWriter.append(" & \\textbf{h" + (i + 1) + "}");
                    }
                    fileWriter.append("\\\\\\midrule\n");
                    for (String problem : problems) {
                        fileWriter.append("\t\t" + problem.replaceAll("O[OA]\\_", "") + " ");
                        double[] values = new double[9];
                        Arrays.fill(values, 0D);
                        for (String heuristicFunction : heuristicFunctions) {
                            Scanner scanner = new Scanner(new File("experiment/" + algorithm + "/" + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problem + "/LLH.txt"));

                            int i = 0;
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine().trim().replaceAll("(([a-z]+|[A-Z]+)[0-9]+)+|(\\D+)", "");
                                if (!line.isEmpty()) {
                                    values[i++] += Integer.valueOf(line);
                                }
                            }
                        }

                        double max = Double.NEGATIVE_INFINITY;
                        for (int i = 0; i < values.length; i++) {
                            values[i] = values[i] / (double) executions / (double) heuristicFunctions.length;
                            double value = values[i];
                            if (value > max) {
                                max = value;
                            }
                        }

                        for (double value : values) {
                            fileWriter.append("& ");
                            if (value == max) {
                                fileWriter.append("\\textbf{");
                            }
                            if (algorithm.equals("SPEA2")) {
                                value /= 2;
                            }
                            fileWriter.append(decimalFormat.format(value));
                            if (value == (max / 2)) {
                                fileWriter.append("}");
                            }
                            fileWriter.append(" ");
                        }
                        fileWriter.append("\\\\\n");

                    }
                    fileWriter.append("\t\t\\bottomrule\n");
                    fileWriter.append("\t\\end{tabulary}\n"
                            + "\\end{table}\n\n");
                }
            }
        }
    }

}
