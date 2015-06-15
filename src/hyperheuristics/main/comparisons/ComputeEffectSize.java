/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.main.comparisons;

import hyperheuristics.hypervolume.HypervolumeHandler;
import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import hyperheuristics.statistics.EffectSize;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.NonDominatedSolutionList;

/**
 *
 * @author giovani
 */
public class ComputeEffectSize {

    public static void main(String[] args) throws IOException, InterruptedException {
        int executions = 30;

        int[] numberOfObjectivesArray = new int[]{2, 4};

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
            LowLevelHeuristic.MULTI_ARMED_BANDIT,
            LowLevelHeuristic.RANDOM
        };

        MetricsUtil metricsUtil = new MetricsUtil();
        DecimalFormat decimalFormatter = new DecimalFormat("0.00E0");
        for (int objectives : numberOfObjectivesArray) {

            try (FileWriter hypervolumeTableWriter = new FileWriter("experiment/" + objectives + "objectives/HYPERVOLUME_ES.txt")) {

                StringBuilder hypervolumeLatexTableBuilder = new StringBuilder();

                hypervolumeLatexTableBuilder
                        .append("\\documentclass{paper}\n")
                        .append("\n")
                        .append("\\usepackage[T1]{fontenc}\n")
                        .append("\\usepackage[latin1]{inputenc}\n")
                        .append("\\usepackage[hidelinks]{hyperref}\n")
                        .append("\\usepackage{tabulary}\n")
                        .append("\\usepackage{booktabs}\n")
                        .append("\\usepackage{multirow}\n")
                        .append("\\usepackage{amsmath}\n")
                        .append("\\usepackage{mathtools}\n")
                        .append("\\usepackage{graphicx}\n")
                        .append("\\usepackage{array}\n")
                        .append("\\usepackage[linesnumbered,ruled,inoutnumbered]{algorithm2e}\n")
                        .append("\\usepackage{subfigure}\n")
                        .append("\\usepackage[hypcap]{caption}\n")
                        .append("\n")
                        .append("\\begin{document}")
                        .append("\n")
                        .append("\\begin{table*}[!htb]\n")
                        .append("\t\\centering\n")
                        .append("\t\\def\\arraystretch{1.5}\n")
                        .append("\t\\setlength{\\tabcolsep}{10pt}\n")
                        .append("\t\\fontsize{8pt}{10pt}\\selectfont\n")
                        .append("\t\\caption{Effect Size for ").append(objectives).append(" objectives}\n")
                        .append("\t\\label{tab:Effect Size ").append(objectives).append(" objectives}\n")
                        .append("\t\\begin{tabulary}{\\linewidth}{c");

                for (int i = 0; i < heuristicFunctions.length; i++) {
                    for (int j = i + 1; j < heuristicFunctions.length + 1; j++) {
                        hypervolumeLatexTableBuilder.append("c");
                    }
                }

                hypervolumeLatexTableBuilder.append("}\n");
                hypervolumeLatexTableBuilder.append("\t\t\\toprule\n");
                hypervolumeLatexTableBuilder.append("\t\t\\textbf{System}");

                for (String heuristicFunction : heuristicFunctions) {
                    hypervolumeLatexTableBuilder.append(" & \\textbf{MOEA/").append(heuristicFunction).append("}");
                }

                for (int i = 0; i < heuristicFunctions.length - 1; i++) {
                    for (int j = i + 1; j < heuristicFunctions.length; j++) {
                        hypervolumeLatexTableBuilder.append(" & \\textbf{").append(heuristicFunctions[i]).append("/").append(heuristicFunctions[j]).append("}");
                    }
                }

                hypervolumeLatexTableBuilder.append("\\\\\n");
                hypervolumeLatexTableBuilder.append("\t\t\\midrule\n");

                for (String problem : problems) {
                    hypervolumeLatexTableBuilder.append("\t\t").append(problem.replaceAll("O[OA]\\_", ""));

                    NonDominatedSolutionList truePareto = new NonDominatedSolutionList();

                    String mecbaDirectory = "resultado/nsgaii/" + problem + "_Comb_" + objectives + "obj/";

                    HypervolumeHandler hypervolumeHandler = new HypervolumeHandler();
                    for (int i = 0; i < executions; i++) {
                        hypervolumeHandler.addParetoFront(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas");
                        truePareto.addAll(metricsUtil.readNonDominatedSolutionSet(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas"));
                    }

                    for (String heuristicFunction : heuristicFunctions) {
                        String path = "experiment/";
                        path += objectives + "objectives/";
                        String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";
                        for (int j = 0; j < executions; j++) {
                            hypervolumeHandler.addParetoFront(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt");
                            truePareto.addAll(metricsUtil.readNonDominatedSolutionSet(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt"));
                        }
                    }

                    HashMap<String, double[]> hypervolumeHashMap = new HashMap<>();

                    double[] mecbaHypervolumes = new double[executions];
                    for (int i = 0; i < executions; i++) {
                        mecbaHypervolumes[i] = hypervolumeHandler.calculateHypervolume(mecbaDirectory + "FUN_nsgaii-" + problem + "-" + i + ".NaoDominadas", objectives);
                    }

                    hypervolumeHashMap.put("MOEA", mecbaHypervolumes);

                    for (String heuristicFunction : heuristicFunctions) {
                        String path = "experiment/";
                        path += objectives + "objectives/";
                        String hyperheuristicDirectory = path + heuristicFunction + "/" + problem + "/";

                        double[] hhHypervolumes = new double[executions];

                        for (int j = 0; j < executions; j++) {
                            hhHypervolumes[j] = hypervolumeHandler.calculateHypervolume(hyperheuristicDirectory + "EXECUTION_" + j + "/FUN.txt", objectives);
                        }

                        hypervolumeHashMap.put(heuristicFunction, hhHypervolumes);
                    }

                    HashMap<String, HashMap<String, Double>> hypervolumeEffectSize = EffectSize.computeEffectSize(hypervolumeHashMap);

                    String[] newHeuristicFunctions = new String[heuristicFunctions.length + 1];
                    newHeuristicFunctions[0] = "MOEA";
                    System.arraycopy(heuristicFunctions, 0, newHeuristicFunctions, 1, heuristicFunctions.length);

                    for (int i = 0; i < newHeuristicFunctions.length - 1; i++) {
                        String groupA = newHeuristicFunctions[i];
                        for (int j = i + 1; j < newHeuristicFunctions.length; j++) {
                            String groupB = newHeuristicFunctions[j];

                            double hypervolumeValue = hypervolumeEffectSize.get(groupA).get(groupB);

                            hypervolumeLatexTableBuilder.append(" & ").append(decimalFormatter.format(hypervolumeValue)).append(" (").append(EffectSize.interpretEffectSize(hypervolumeValue)).append(")");
                        }
                    }
                    hypervolumeLatexTableBuilder.append("\\\\\n");
                }

                hypervolumeLatexTableBuilder
                        .append("\t\t\\bottomrule\n")
                        .append("\t\\end{tabulary}\n")
                        .append("\\end{table*}\n")
                        .append("\\end{document}");

                hypervolumeTableWriter.write(hypervolumeLatexTableBuilder.toString().replaceAll("ChoiceFunction", "HITO-CF").replaceAll("MultiArmedBandit", "HITO-MAB").replaceAll("Random", "HITO-R"));
            }
        }
    }

}
