/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.main.comparisons;

import hyperheuristics.hypervolume.HypervolumeHandler;
import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import hyperheuristics.main.NSGAIIHyperheuristicMain;
import hyperheuristics.statistics.KruskalWallisTest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author giovaniguizzo
 */
public class CompareHypervolumes {

    public static int EXECUTIONS = 100;
    public static String outpath = "experiment/";

    public static void main(String[] args) throws IOException, InterruptedException {

        int[] numberOfObjectivesArray = new int[]{2};
        String[] problems;
        if (args.length == 0) {
            problems = new String[]{
                "Guava"
            };
        } else {
            EXECUTIONS = Integer.parseInt(args[0]);
            numberOfObjectivesArray = new int[]{Integer.parseInt(args[4])};
            problems = Arrays.copyOfRange(args, 5, args.length);
        }

        String[] heuristicFunctions = new String[]{
            LowLevelHeuristic.CHOICE_FUNCTION,
            LowLevelHeuristic.MULTI_ARMED_BANDIT,
            LowLevelHeuristic.RANDOM
        };

        String[] algorithms = new String[]{
            "NSGA-II"};

        for (int numberOfObjectives : numberOfObjectivesArray) {
//            hypervolumeComparison(problems, heuristicFunctions, numberOfObjectives, algorithms);
//            hypervolumeHyperheuristicsComparison(problems, heuristicFunctions, numberOfObjectives,);
//            hypervolumeByGeneration(problems, heuristicFunctions, numberOfObjectives);
            generateTables(problems, heuristicFunctions, numberOfObjectives, algorithms);
        }
    }

    private static void generateTables(String[] problems, String[] heuristicFunctions, int numberOfObjectives, String[] algorithms) throws InterruptedException, IOException {
        String outputDirectory = outpath;

        try (FileWriter fileWriter = new FileWriter(outputDirectory + "TABLES_" + numberOfObjectives + ".txt")) {

            StringBuilder tableString = new StringBuilder();
            DecimalFormat decimalFormatter = new DecimalFormat("0.00");
            StandardDeviation standardDeviation = new StandardDeviation();

//            pfKnown:
//            {
//                tableString.append("\\documentclass{paper}\n"
//                        + "\n"
//                        + "\\usepackage[T1]{fontenc}\n"
//                        + "\\usepackage[latin1]{inputenc}\n"
//                        + "\\usepackage[hidelinks]{hyperref}\n"
//                        + "\\usepackage{tabulary}\n"
//                        + "\\usepackage{booktabs}\n"
//                        + "\\usepackage{multirow}\n"
//                        + "\\usepackage{amsmath}\n"
//                        + "\\usepackage{mathtools}\n"
//                        + "\\usepackage{graphicx}\n"
//                        + "\\usepackage{array}\n"
//                        + "\\usepackage[linesnumbered,ruled,inoutnumbered]{algorithm2e}\n"
//                        + "\\usepackage{subfigure}\n"
//                        + "\\usepackage[hypcap]{caption}\n"
//                        + "\\usepackage{pdflscape}\n"
//                        + "\n"
//                        + "\\begin{document}\n"
//                        + "\n"
//                        + "\\begin{landscape}\n"
//                        + "\n");
//
//                tableString
//                        .append("\\begin{table}[!htb]\n"
//                                + "\t\\centering\n"
//                                + "\t\\def\\arraystretch{1.5}\n"
//                                + "\t\\setlength{\\tabcolsep}{10pt}\n"
//                                + "\t\\fontsize{8pt}{10pt}\\selectfont"
//                                + "\t\\caption{Hypervolume of the $PF_{known}$ fronts for ").append(numberOfObjectives).append(" objectives}\n"
//                        + "\t\\label{tab:Hypervolumes ").append(numberOfObjectives).append(" objectives}\n"
//                        + "\t\\begin{tabulary}{\\textwidth}{c");
//                for (String algorithm : algorithms) {
//                    tableString.append("c");
//                    if (!"MOEADD".equals(algorithm)) {
//                        for (String heuristicFunction : heuristicFunctions) {
//                            tableString.append("c");
//                        }
//                    }
//                }
//                tableString.append("}\n");
//                tableString.append("\t\t\\toprule\n");
//                tableString.append("\t\t\\textbf{System}");
//
//                for (String algorithm : algorithms) {
//                    tableString.append(" & \\textbf{").append(algorithm).append("}");
//                    if (!"MOEADD".equals(algorithm)) {
//                        for (String heuristicFunction : heuristicFunctions) {
//                            tableString.append(" & \\textbf{").append(algorithm).append("-").append(heuristicFunction).append("}");
//                        }
//                    }
//                }
//                tableString.append("\\\\\n");
//                tableString.append("\t\t\\midrule\n");
//
//                for (String problem : problems) {
//                    HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();
//
//                    for (String algorithm : algorithms) {
//                        String mecbaDirectory = "resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + numberOfObjectives + "obj/";
//
//                        //Best PFknown hypervolume
//                        //Populate HypervolueHandler
//                        hypervolumeHandler.addParetoFront(mecbaDirectory + "All_FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem);
//
//                        if (!"MOEADD".equals(algorithm)) {
//                            for (String heuristicFunction : heuristicFunctions) {
//                                String path = outpath;
//                                path += algorithm + "/" + numberOfObjectives + "objectives/";
//                                String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
//                                hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "FUN.txt");
//                            }
//                        }
//                    }
//
//                    double[] mecbaHypervolumes = new double[algorithms.length];
//                    double[] hyperheuristicHypervolumes = new double[heuristicFunctions.length * algorithms.length];
//
//                    Arrays.fill(hyperheuristicHypervolumes, 0D);
//                    for (int i = 0; i < algorithms.length; i++) {
//                        String algorithm = algorithms[i];
//                        String mecbaDirectory = "resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + numberOfObjectives + "obj/";
//                        //Calculate Hypervolume
//                        mecbaHypervolumes[i] = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "All_FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem, numberOfObjectives);
//
//                        if (!"MOEADD".equals(algorithm)) {
//                            for (int j = 0; j < heuristicFunctions.length; j++) {
//                                String heuristicFunction = heuristicFunctions[j];
//                                String path = outpath;
//                                path += algorithm + "/" + numberOfObjectives + "objectives/";
//                                String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
//                                hyperheuristicHypervolumes[i * heuristicFunctions.length + j] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "FUN.txt", numberOfObjectives);
//                            }
//                        }
//                    }
//                    //Write PFknown results
//                    double maxHypervolume = Double.NEGATIVE_INFINITY;
//
//                    for (int i = 0; i < mecbaHypervolumes.length; i++) {
//                        double hypervolume = mecbaHypervolumes[i];
//                        if (hypervolume > maxHypervolume) {
//                            maxHypervolume = hypervolume;
//                        }
//                    }
//
//                    for (int i = 0; i < heuristicFunctions.length; i++) {
//                        if (hyperheuristicHypervolumes[i] > maxHypervolume) {
//                            maxHypervolume = hyperheuristicHypervolumes[i];
//                        }
//                    }
//
//                    tableString.append("\t\t" + problem.replaceAll("\\_", "\\\\_"));
//                    for (int i = 0; i < algorithms.length; i++) {
//                        tableString.append(" & ");
//                        double mecbaHypervolume = mecbaHypervolumes[i];
//                        if (maxHypervolume == mecbaHypervolume) {
//                            tableString.append("\\textbf{");
//                        }
//                        tableString.append(decimalFormatter.format(mecbaHypervolume));
//                        if (maxHypervolume == mecbaHypervolume) {
//                            tableString.append("}");
//                        }
//
//                        if (!"MOEADD".equals(algorithms[i])) {
//                            for (int j = 0; j < heuristicFunctions.length; j++) {
//                                tableString.append(" & ");
//                                double hyperheuristicHypervolume = hyperheuristicHypervolumes[i * heuristicFunctions.length + j];
//                                if (maxHypervolume == hyperheuristicHypervolume) {
//                                    tableString.append("\\textbf{");
//                                }
//                                tableString.append(decimalFormatter.format(hyperheuristicHypervolume));
//                                if (maxHypervolume == hyperheuristicHypervolume) {
//                                    tableString.append("}");
//                                }
//                            }
//                        }
//                    }
//
//                    tableString.append("\\\\\n");
//                }
//                tableString.append("\t\t\\bottomrule\n");
//                tableString.append("\t\\end{tabulary}\n");
//                tableString.append("\\end{table}\n\n");
//            }
            //Best mean hypervolume
            mean:
            {
                tableString.append("\\begin{table}[!htb]\n"
                        + "\\centering\n"
                        //                        + "\t\\def\\arraystretch{1.5}\n"
                        //                        + "\t\\setlength{\\tabcolsep}{10pt}\n"
                        + "\t\\fontsize{8pt}{8pt}\\selectfont\n"
                        + "\t\\caption{Hypervolume average found for " + numberOfObjectives + " objectives}\n"
                        + "\t\\label{tab:Hypervolumes average " + numberOfObjectives + " objectives}\n"
                        + "\t\\begin{tabulary}{\\textwidth}{c");
                for (String algorithm : algorithms) {
                    tableString.append("c");
                    if (!"Random".equals(algorithm)) {
                        for (String heuristicFunction : heuristicFunctions) {
                            tableString.append("c");
                        }
                    }
                }
                tableString.append("}\n");
                tableString.append("\t\t\\toprule\n");
                tableString.append("\t\t\\textbf{System}");

                for (String algorithm : algorithms) {
                    tableString.append(" & \\textbf{" + algorithm + "}");
                    if (!"Random".equals(algorithm)) {
                        for (String heuristicFunction : heuristicFunctions) {
                            tableString.append(" & \\textbf{" + algorithm + "-" + heuristicFunction + "}");
                        }
                    }
                }
                tableString.append("\\\\\n");
                tableString.append("\t\t\\midrule\n");

                for (String problem : problems) {
                    System.out.println("================= " + problem + " =================");
                    HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();

                    for (String algorithm : algorithms) {
                        String mecbaDirectory = "resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + numberOfObjectives + "obj/";

                        for (int i = 0; i < EXECUTIONS; i++) {
                            hypervolumeHandler.addParetoFront(mecbaDirectory + "FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem + "-" + i + ".NaoDominadas");
                        }

                        if (!"Random".equals(algorithm)) {
                            for (String heuristicFunction : heuristicFunctions) {
                                String path = outpath;
                                path += algorithm + "/" + numberOfObjectives + "objectives/";
                                String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
                                for (int j = 0; j < EXECUTIONS; j++) {
                                    hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt");
                                }
                            }
                        }
                    }

                    double[][] mecbaHypervolumes = new double[algorithms.length][EXECUTIONS];
                    for (double[] mecbaHypervolume : mecbaHypervolumes) {
                        Arrays.fill(mecbaHypervolume, 0D);
                    }

                    double mecbaMeanHypervolume[] = new double[algorithms.length];
                    Arrays.fill(mecbaMeanHypervolume, 0D);

                    double[][] hyperheuristicHypervolumes = new double[algorithms.length * heuristicFunctions.length][EXECUTIONS];
                    for (double[] hyperheuristicHypervolume : hyperheuristicHypervolumes) {
                        Arrays.fill(hyperheuristicHypervolume, 0D);
                    }

                    double[] hyperheuristicMeanHypervolumes = new double[algorithms.length * heuristicFunctions.length];
                    Arrays.fill(hyperheuristicMeanHypervolumes, 0D);

                    for (int i = 0; i < algorithms.length; i++) {
                        String algorithm = algorithms[i];
                        String mecbaDirectory = "resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + numberOfObjectives + "obj/";
                        for (int j = 0; j < EXECUTIONS; j++) {
                            mecbaHypervolumes[i][j] = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem + "-" + j + ".NaoDominadas", numberOfObjectives);
                            mecbaMeanHypervolume[i] += mecbaHypervolumes[i][j];
                            if (!"Random".equals(algorithm)) {
                                for (int k = 0; k < heuristicFunctions.length; k++) {
                                    String path = outpath;
                                    path += algorithm + "/" + numberOfObjectives + "objectives/";
                                    String hyperheuristicDirectory = path + heuristicFunctions[k] + "/" + problem + "/";
                                    hyperheuristicHypervolumes[i * heuristicFunctions.length + k][j] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt", numberOfObjectives);
                                    hyperheuristicMeanHypervolumes[i * heuristicFunctions.length + k] += hyperheuristicHypervolumes[i * heuristicFunctions.length + k][j];
                                }
                            }
                        }
                    }

                    for (int i = 0; i < mecbaMeanHypervolume.length; i++) {
                        mecbaMeanHypervolume[i] /= (double) EXECUTIONS;
                    }
                    for (int i = 0; i < hyperheuristicMeanHypervolumes.length; i++) {
                        hyperheuristicMeanHypervolumes[i] /= (double) EXECUTIONS;
                    }

                    double maxMean = Double.NEGATIVE_INFINITY;
                    String maxHeuristic = "NULL";

                    for (int i = 0; i < mecbaMeanHypervolume.length; i++) {
                        double mean = mecbaMeanHypervolume[i];
                        if (mean > maxMean) {
                            maxMean = mean;
                            maxHeuristic = algorithms[i];
                        }
                    }

                    for (int i = 0; i < hyperheuristicMeanHypervolumes.length; i++) {
                        double hyperheuristicMeanHypervolume = hyperheuristicMeanHypervolumes[i];
                        if (hyperheuristicMeanHypervolume > maxMean) {
                            maxMean = hyperheuristicMeanHypervolume;
                            maxHeuristic = algorithms[i / heuristicFunctions.length] + heuristicFunctions[i % heuristicFunctions.length];
                        }
                    }

                    HashMap<String, double[]> values = new HashMap<>();

                    for (int i = 0; i < algorithms.length; i++) {
                        String algorithm = algorithms[i];
                        values.put(algorithm, mecbaHypervolumes[i]);
                    }
                    for (int i = 0; i < hyperheuristicHypervolumes.length; i++) {
                        double[] hyperheuristicHypervolume = hyperheuristicHypervolumes[i];
                        String heuristicFunction = heuristicFunctions[i % heuristicFunctions.length];
                        String algorithm = algorithms[i / heuristicFunctions.length];
                        values.put(algorithm + heuristicFunction, hyperheuristicHypervolume);
                    }

                    HashMap<String, HashMap<String, Boolean>> result = KruskalWallisTest.test(values);

                    tableString.append("\t\t" + problem.replaceAll("\\_", "\\\\_"));
                    for (int i = 0; i < algorithms.length; i++) {
                        String algorithm = algorithms[i];
                        tableString.append(" & ");
                        if (algorithm.equals(maxHeuristic) || !result.get(algorithm).get(maxHeuristic)) {
                            tableString.append("\\textbf{");
                        }
                        tableString.append(decimalFormatter.format(mecbaMeanHypervolume[i]));
                        tableString.append(" (").append(decimalFormatter.format(standardDeviation.evaluate(mecbaHypervolumes[i]))).append(")");
                        if (algorithm.equals(maxHeuristic) || !result.get(algorithm).get(maxHeuristic)) {
                            tableString.append("}");
                        }
                        if (!"Random".equals(algorithm)) {
                            for (int j = 0; j < heuristicFunctions.length; j++) {
                                String heuristicFunction = algorithm + heuristicFunctions[j];
                                tableString.append(" & ");
                                if (heuristicFunction.equals(maxHeuristic) || !result.get(heuristicFunction).get(maxHeuristic)) {
                                    tableString.append("\\textbf{");
                                }
                                tableString.append(decimalFormatter.format(hyperheuristicMeanHypervolumes[i * heuristicFunctions.length + j]));
                                tableString.append(" (").append(decimalFormatter.format(standardDeviation.evaluate(hyperheuristicHypervolumes[i * heuristicFunctions.length + j]))).append(")");
                                if (heuristicFunction.equals(maxHeuristic) || !result.get(heuristicFunction).get(maxHeuristic)) {
                                    tableString.append("}");
                                }
                            }
                        }
                    }
                    tableString.append("\\\\\n");
                }
                tableString.append("\t\t\\bottomrule\n");
                tableString.append("\t\\end{tabulary}\n");
                tableString.append("\\end{table}\n");
            }
            tableString.append("\n"
                    + "\\end{landscape}\n"
                    + "\n"
                    + "\\end{document}\n");
            fileWriter.write(tableString.toString().replaceAll("ChoiceFunction", "CF").replaceAll("MultiArmedBandit", "MAB"));
        }
    }

    private static void hypervolumeComparison(String[] problems, String[] heuristicFunctions, int numberOfObjectives) throws InterruptedException, IOException {
        for (String heuristicFunction : heuristicFunctions) {
            String path = outpath;
            String outputDirectory = path + numberOfObjectives + "objectives/" + heuristicFunction + "/";

            try (FileWriter fileWriter = new FileWriter(outputDirectory + "HYPERVOLUMES.txt")) {

                int hyperheuristicBest = 0;
                int mecbaBest = 0;
                int tied = 0;

                int hyperheuristicBestMean = 0;
                int mecbaBestMean = 0;
                int tiedMean = 0;
                int equivalent = 0;
                for (String problem : problems) {
                    fileWriter.append("Hypervolume comparison for " + problem + ":\n");
                    fileWriter.append("\n");
                    HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();

                    String hyperheuristicDirectory = outputDirectory + problem + "/";

                    String mecbaDirectory = "resultado/nsgaii/" + problem + "_Comb_" + numberOfObjectives + "obj/";

                    //Best hypervolume for PFknown
                    hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "FUN.txt");
                    hypervolumeHandler.addParetoFront(mecbaDirectory + "All_FUN_nsgaii-" + problem);

                    double mecbaHypervolume = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "All_FUN_nsgaii-" + problem, numberOfObjectives);
                    double hyperheuristicHypervolume = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "FUN.txt", numberOfObjectives);
                    fileWriter.append("MECBA PFknown: " + mecbaHypervolume + "\n");
                    fileWriter.append(heuristicFunction + " PFknown: " + hyperheuristicHypervolume + "\n");
                    if (mecbaHypervolume == hyperheuristicHypervolume) {
                        fileWriter.append("Best PFknown: Tied!\n");
                        tied++;
                    } else if (mecbaHypervolume > hyperheuristicHypervolume) {
                        fileWriter.append("Best PFknown: MECBA\n");
                        mecbaBest++;
                    } else {
                        fileWriter.append("Best PFknown: " + heuristicFunction + "\n");
                        hyperheuristicBest++;
                    }

                    //Best mean hypervolume
                    fileWriter.append("\n");

                    hypervolumeHandler.clear();

                    for (int i = 0; i < EXECUTIONS; i++) {
                        hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "EXECUTION_" + i + "/FUN.txt");
                        hypervolumeHandler.addParetoFront(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas");
                    }

                    double[] mecbaHypervolumes = new double[EXECUTIONS];
                    double[] hyperheuristicHypervolumes = new double[EXECUTIONS];

                    mecbaHypervolume = 0;
                    hyperheuristicHypervolume = 0;

                    for (int i = 0; i < EXECUTIONS; i++) {
                        mecbaHypervolumes[i] = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas", numberOfObjectives);
                        mecbaHypervolume += mecbaHypervolumes[i];
                        hyperheuristicHypervolumes[i] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "EXECUTION_" + i + "/FUN.txt", numberOfObjectives);
                        hyperheuristicHypervolume += hyperheuristicHypervolumes[i];
                    }

                    mecbaHypervolume /= (double) EXECUTIONS;
                    hyperheuristicHypervolume /= (double) EXECUTIONS;

                    fileWriter.append("MECBA (Mean): " + mecbaHypervolume + "\n");
                    fileWriter.append(heuristicFunction + " (Mean): " + hyperheuristicHypervolume + "\n");

                    if (mecbaHypervolume == hyperheuristicHypervolume) {
                        fileWriter.append("Best (Mean): Tied!\n");
                        tiedMean++;
                    } else {
                        if (mecbaHypervolume > hyperheuristicHypervolume) {
                            fileWriter.append("Best (Mean): MECBA\n");
                            mecbaBestMean++;
                        } else {
                            fileWriter.append("Best (Mean): " + heuristicFunction + "\n");
                            hyperheuristicBestMean++;
                        }

                        String script = "";

                        script += "MECBA <- c(";
                        for (double value : mecbaHypervolumes) {
                            script += value + ",";
                        }
                        script = script.substring(0, script.lastIndexOf(",")) + ")";

                        script += "\n";

                        script += "MECBA_Hyp <- c(";
                        for (double value : hyperheuristicHypervolumes) {
                            script += value + ",";
                        }
                        script = script.substring(0, script.lastIndexOf(",")) + ")";

                        script += "\n";

                        script += "require(pgirmess)\n";
                        script += "AR1 <- cbind(MECBA, MECBA_Hyp)\n";
                        script += "result <- friedman.test(AR1)\n";
                        script += "m <- data.frame(result$statistic,result$p.value)\n";
                        script += "pos_teste <- friedmanmc(AR1)\n";
                        script += "print(pos_teste)";

                        try (FileWriter scriptWriter = new FileWriter(hyperheuristicDirectory + "temp_input.txt")) {
                            scriptWriter.append(script);
                        }
                        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");

                        File tempOutput = new File(hyperheuristicDirectory + "temp_output.txt");
                        processBuilder.redirectOutput(tempOutput);

                        File tempInput = new File(hyperheuristicDirectory + "temp_input.txt");
                        processBuilder.redirectInput(tempInput);

                        Process process = processBuilder.start();
                        process.waitFor();

                        Scanner scanner = new Scanner(tempOutput);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.contains("FALSE")) {
                                equivalent++;
                                fileWriter.append("Statistical Equivalents (Friedman 5%)\n");
                                break;
                            }
                        }

                        tempInput.delete();
                        tempOutput.delete();
                    }

                    fileWriter.append("\n");
                    fileWriter.append("----------\n");
                    fileWriter.append("\n");
                }
                fileWriter.append("Problems: " + problems.length + "\n");
                fileWriter.append("\n");
                fileWriter.append("Tied PFknown: " + tied + "\n");
                fileWriter.append("MECBA PFknown: " + mecbaBest + "\n");
                fileWriter.append(heuristicFunction + " PFknown: " + hyperheuristicBest + "\n");
                fileWriter.append("\n");
                fileWriter.append("Tied (Mean): " + tiedMean + "\n");
                fileWriter.append("MECBA (Mean): " + mecbaBestMean + "\n");
                fileWriter.append(heuristicFunction + " (Mean): " + hyperheuristicBestMean + "\n");
                fileWriter.append("Statistically Equivalent: " + equivalent + "\n");
            }
        }
    }

    private static void hypervolumeHyperheuristicsComparison(String[] problems, String[] heuristicFunctions, int numberOfObjectives) throws InterruptedException, IOException {
        String outputDirectory = outpath + numberOfObjectives + "objectives/";

        int mecbaBestCount = 0;
        int[] hyperheuristicBestCount = new int[heuristicFunctions.length];
        Arrays.fill(hyperheuristicBestCount, 0);

        int mecbaBestMeanCount = 0;
        int[] hyperheuristicBestMeanCount = new int[heuristicFunctions.length];
        Arrays.fill(hyperheuristicBestMeanCount, 0);

        try (FileWriter fileWriter = new FileWriter(outputDirectory + "HYPERVOLUMES.txt")) {
            for (String problem : problems) {
                fileWriter.append("Hypervolume comparison for " + problem + ":\n");
                fileWriter.append("\n");

                HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();
                String mecbaDirectory = "resultado/nsgaii/" + problem + "_Comb_" + numberOfObjectives + "obj/";

                //Best PFknown hypervolume
                pfKnown:
                {
                    //Populate HypervolueHandler
                    hypervolumeHandler.addParetoFront(mecbaDirectory + "All_FUN_nsgaii-" + problem);

                    for (String heuristicFunction : heuristicFunctions) {
                        String path = outpath;
                        path += numberOfObjectives + "objectives/";
                        String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
                        hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "FUN.txt");
                    }

                    //Calculate Hypervolume
                    double mecbaHypervolume = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "All_FUN_nsgaii-" + problem, numberOfObjectives);

                    double[] hyperheuristicHypervolumes = new double[heuristicFunctions.length];
                    Arrays.fill(hyperheuristicHypervolumes, 0D);
                    for (int i = 0; i < heuristicFunctions.length; i++) {
                        String heuristicFunction = heuristicFunctions[i];
                        String path = outpath;
                        path += numberOfObjectives + "objectives/";
                        String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
                        hyperheuristicHypervolumes[i] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "FUN.txt", numberOfObjectives);
                    }

                    //Write PFknown results
                    fileWriter.append("MECBA PFknown: " + mecbaHypervolume + "\n");
                    double maxHypervolume = mecbaHypervolume;

                    for (int i = 0; i < heuristicFunctions.length; i++) {
                        String heuristicFunction = heuristicFunctions[i];

                        fileWriter.append(heuristicFunction + " PFknown: " + hyperheuristicHypervolumes[i] + "\n");
                        if (hyperheuristicHypervolumes[i] > maxHypervolume) {
                            maxHypervolume = hyperheuristicHypervolumes[i];
                        }
                    }

                    fileWriter.append("Best PFknown:");

                    if (mecbaHypervolume == maxHypervolume) {
                        fileWriter.append(" MECBA");
                        mecbaBestCount++;
                    }

                    for (int i = 0; i < heuristicFunctions.length; i++) {
                        String heuristicFunction = heuristicFunctions[i];
                        if (hyperheuristicHypervolumes[i] == maxHypervolume) {
                            fileWriter.append(" " + heuristicFunction);
                            hyperheuristicBestCount[i]++;
                        }
                    }
                    fileWriter.append("\n");
                }

                //Best mean hypervolume
                mean:
                {
                    fileWriter.append("\n");

                    hypervolumeHandler.clear();

                    for (int i = 0; i < EXECUTIONS; i++) {
                        hypervolumeHandler.addParetoFront(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas");
                    }

                    for (String heuristicFunction : heuristicFunctions) {
                        String path = outpath;
                        path += numberOfObjectives + "objectives/";
                        String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
                        for (int j = 0; j < EXECUTIONS; j++) {
                            hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt");
                        }
                    }

                    double[] mecbaHypervolumes = new double[EXECUTIONS];
                    double mecbaHypervolume = 0;

                    double[][] hyperheuristicHypervolumes = new double[heuristicFunctions.length][EXECUTIONS];
                    for (double[] hyperheuristicHypervolume : hyperheuristicHypervolumes) {
                        Arrays.fill(hyperheuristicHypervolume, 0D);
                    }

                    double[] hyperheuristicMeanHypervolumes = new double[heuristicFunctions.length];
                    Arrays.fill(hyperheuristicMeanHypervolumes, 0D);

                    for (int i = 0; i < EXECUTIONS; i++) {
                        mecbaHypervolumes[i] = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas", numberOfObjectives);
                        mecbaHypervolume += mecbaHypervolumes[i];
                        for (int j = 0; j < heuristicFunctions.length; j++) {
                            String path = outpath;
                            path += numberOfObjectives + "objectives/";
                            String hyperheuristicDirectory = path + heuristicFunctions[j] + "/" + problem + "/";
                            hyperheuristicHypervolumes[j][i] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "EXECUTION_" + i + "/FUN.txt", numberOfObjectives);
                            hyperheuristicMeanHypervolumes[j] += hyperheuristicHypervolumes[j][i];
                        }
                    }

                    mecbaHypervolume /= (double) EXECUTIONS;
                    for (int i = 0; i < hyperheuristicMeanHypervolumes.length; i++) {
                        hyperheuristicMeanHypervolumes[i] /= (double) EXECUTIONS;
                    }

                    fileWriter.append("MECBA (Mean): " + mecbaHypervolume + "\n");
                    double maxHypervolume = mecbaHypervolume;
                    for (int i = 0; i < heuristicFunctions.length; i++) {
                        String heuristicFunction = heuristicFunctions[i];
                        fileWriter.append(heuristicFunction + " (Mean): " + hyperheuristicMeanHypervolumes[i] + "\n");
                        if (hyperheuristicMeanHypervolumes[i] > maxHypervolume) {
                            maxHypervolume = hyperheuristicMeanHypervolumes[i];
                        }
                    }

                    fileWriter.append("Best (Mean):");

                    if (mecbaHypervolume == maxHypervolume) {
                        fileWriter.append(" MECBA");
                        mecbaBestMeanCount++;
                    }

                    for (int i = 0; i < heuristicFunctions.length; i++) {
                        String heuristicFunction = heuristicFunctions[i];
                        if (hyperheuristicMeanHypervolumes[i] == maxHypervolume) {
                            fileWriter.append(" " + heuristicFunction);
                            hyperheuristicBestMeanCount[i]++;
                        }
                    }
                    fileWriter.append("\n");
                }
                fileWriter.append("\n");
                fileWriter.append("----------\n");
                fileWriter.append("\n");
            }
            fileWriter.append("Problems: " + problems.length + "\n");
            fileWriter.append("\n");
            fileWriter.append("Problems MECBA PFknown problems: " + mecbaBestCount + "\n");
            for (int i = 0; i < heuristicFunctions.length; i++) {
                String heuristicFunction = heuristicFunctions[i];
                fileWriter.append(heuristicFunction + " PFknown problems: " + hyperheuristicBestCount[i] + "\n");
            }
            fileWriter.append("\n");
            fileWriter.append("MECBA (Mean): " + mecbaBestMeanCount + "\n");
            for (int i = 0; i < heuristicFunctions.length; i++) {
                String heuristicFunction = heuristicFunctions[i];
                fileWriter.append(heuristicFunction + " (Mean): " + hyperheuristicBestMeanCount[i] + "\n");
            }
        }
    }

    private static void hypervolumeByGeneration(String[] problems, String[] heuristicFunctions, int numberOfObjectives) {
        for (String heuristicFunction : heuristicFunctions) {

            String path = outpath;
            String outputDirectory = path + numberOfObjectives + "objectives/" + heuristicFunction + "/";
            for (String problem : problems) {
                HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();
                String problemDirectory = outputDirectory + problem + "/";

                for (int i = 0; i < EXECUTIONS; i++) {
                    String executionDirectory = problemDirectory + "/EXECUTION_" + i + "/";

                    File generationDirectory = new File(executionDirectory + "GENERATIONS/");

                    for (File generation : generationDirectory.listFiles()) {
                        if (generation.getName().startsWith("GEN")) {
                            hypervolumeHandler.addParetoFront(generation.getPath());
                        }
                    }

                }

                for (int i = 0; i < EXECUTIONS; i++) {
                    String executionDirectory = problemDirectory + "/EXECUTION_" + i + "/";

                    File generationDirectory = new File(executionDirectory + "GENERATIONS/");

                    try (FileWriter hypervolumeWriter = new FileWriter(executionDirectory + "GENERATIONS_HYPERVOLUME.txt")) {
                        int generations = generationDirectory.list().length;

                        double[] hypervolumes = new double[generations];

                        for (int j = 1; j <= generations; j++) {
                            double hypervolume = hypervolumeHandler.calculateHypervolume(generationDirectory.getPath() + "/GEN_" + j + ".txt", numberOfObjectives);
                            hypervolumes[j - 1] = hypervolume;
                            hypervolumeWriter.append(hypervolume + "\n");
                        }
                        hypervolumeWriter.append("\nLast change to hypervolume in generation: ");

                        int lastGeneration = generations;
                        while (hypervolumes[lastGeneration - 1] == hypervolumes[generations - 1] && lastGeneration > 0) {
                            lastGeneration--;
                        }
                        lastGeneration++;
                        hypervolumeWriter.append(lastGeneration + "\n");
                    } catch (IOException ex) {
                        Logger.getLogger(NSGAIIHyperheuristicMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}
