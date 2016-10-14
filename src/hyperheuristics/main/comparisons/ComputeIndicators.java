/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hyperheuristics.main.comparisons;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import hyperheuristics.statistics.KruskalWallisTest;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import jmetal.base.SolutionSet;
import jmetal.qualityIndicator.Coverage;
import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.Spread;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.NonDominatedSolutionList;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author giovani
 */
public class ComputeIndicators {

    public static int EXECUTIONS = 30;

    public static void main(String[] args) throws IOException, InterruptedException {
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

        String[] algorithms = new String[]{
            "NSGAII",
            "MOEADD",};

        MetricsUtil metricsUtil = new MetricsUtil();
        DecimalFormat decimalFormatter = new DecimalFormat("0.00E0");
        Mean mean = new Mean();
        StandardDeviation standardDeviation = new StandardDeviation();

        InvertedGenerationalDistance igd = new InvertedGenerationalDistance();
        GenerationalDistance gd = new GenerationalDistance();
        Spread spread = new Spread();
        Coverage coverage = new Coverage();

        for (int objectives : numberOfObjectivesArray) {
            try (FileWriter IGDWriter = new FileWriter("experiment/IGD_" + objectives + ".tex");
                    FileWriter spreadWriter = new FileWriter("experiment/SPREAD_" + objectives + ".tex");
                    FileWriter GDWriter = new FileWriter("experiment/GD_" + objectives + ".tex");
                    FileWriter coverageWriter = new FileWriter("experiment/COVERAGE_" + objectives + ".tex")) {

                StringBuilder latexTableBuilder = new StringBuilder();

                latexTableBuilder
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
                        .append("\\usepackage{pdflscape}\n")
                        .append("\n")
                        .append("\\begin{document}\n")
                        .append("\n")
                        .append("\\begin{landscape}\n")
                        .append("\n");

                pfKnown:
                {

                    latexTableBuilder.append("\\begin{table}[!htb]\n")
                            .append("\t\\centering\n")
                            .append("\t\\def\\arraystretch{1.5}\n")
                            //                        .append("\t\\setlength{\\tabcolsep}{10pt}\n")
                            //                        .append("\t\\fontsize{8pt}{10pt}\\selectfont\n")
                            .append("\t\\caption{INDICATOR found for $PF_{known}$ for ").append(objectives).append(" objectives}\n")
                            .append("\t\\label{tab:INDICATOR ").append(objectives).append(" objectives}\n")
                            .append("\t\\begin{tabulary}{\\linewidth}{c");

                    for (String algorithm : algorithms) {
                        latexTableBuilder.append("c");
                        if (!"MOEADD".equals(algorithm)) {
                            for (String heuristicFunction : heuristicFunctions) {
                                latexTableBuilder.append("c");
                            }
                        }
                    }

                    latexTableBuilder
                            .append("}\n")
                            .append("\t\t\\toprule\n")
                            .append("\t\t\\textbf{System}");
                    for (String algorithm : algorithms) {
                        latexTableBuilder.append(" & \\textbf{").append(algorithm).append("}");
                        if (!"MOEADD".equals(algorithm)) {
                            for (String heuristicFunction : heuristicFunctions) {
                                latexTableBuilder.append(" & \\textbf{").append(algorithm).append("-").append(heuristicFunction).append("}");
                            }
                        }
                    }
                    latexTableBuilder
                            .append("\\\\\n")
                            .append("\t\t\\midrule\n");

                    for (String problem : problems) {

                        NonDominatedSolutionList trueFront = new NonDominatedSolutionList();
                        pfTrueComposing:
                        {
                            for (String algorithm : algorithms) {
                                SolutionSet mecbaFront = metricsUtil.readNonDominatedSolutionSet("resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + objectives + "obj/All_FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem);
                                trueFront.addAll(mecbaFront);
                                if (!"MOEADD".equals(algorithm)) {
                                    for (String hyperHeuristic : heuristicFunctions) {
                                        SolutionSet front = metricsUtil.readNonDominatedSolutionSet("experiment/" + algorithm + "/" + objectives + "objectives/" + hyperHeuristic + "/" + problem + "/FUN.txt");
                                        trueFront.addAll(front);
                                    }
                                }
                            }
                        }
                        double[][] trueFrontMatrix = trueFront.writeObjectivesToMatrix();

                        HashMap<String, Double> igdMap = new HashMap<>();
                        HashMap<String, Double> gdMap = new HashMap<>();
                        HashMap<String, Double> spreadMap = new HashMap<>();
                        HashMap<String, Double> coverageMap = new HashMap<>();

                        for (String algorithm : algorithms) {
                            double[][] mecbaFront = metricsUtil.readFront("resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + objectives + "obj/All_FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem);
                            igdMap.put(algorithm, igd.invertedGenerationalDistance(mecbaFront, trueFrontMatrix));
                            gdMap.put(algorithm, gd.generationalDistance(mecbaFront, trueFrontMatrix, objectives));
                            spreadMap.put(algorithm, spread.spread(mecbaFront, trueFrontMatrix, objectives));
                            coverageMap.put(algorithm, coverage.coverage(mecbaFront, trueFrontMatrix));
                            if (!"MOEADD".equals(algorithm)) {
                                for (String heuristic : heuristicFunctions) {
                                    double[][] heuristicFront = metricsUtil.readFront("experiment/" + algorithm + "/" + objectives + "objectives/" + heuristic + "/" + problem + "/FUN.txt");
                                    igdMap.put(algorithm + heuristic, igd.invertedGenerationalDistance(heuristicFront, trueFrontMatrix));
                                    gdMap.put(algorithm + heuristic, gd.generationalDistance(heuristicFront, trueFrontMatrix, objectives));
                                    spreadMap.put(algorithm + heuristic, spread.spread(heuristicFront, trueFrontMatrix, objectives));
                                    coverageMap.put(algorithm + heuristic, coverage.coverage(heuristicFront, trueFrontMatrix));
                                }
                            }
                        }

                        latexTableBuilder.append("\t\t").append(problem);

                        String latexTable = latexTableBuilder.toString();

                        latexTableBuilder = new StringBuilder();

                        latexTable = latexTable.replaceAll("O[OA]\\_", "")
                                .replaceAll("ChoiceFunction", "CF")
                                .replaceAll("MultiArmedBandit", "MAB");

                        IGDWriter.write(latexTable.replaceAll("INDICATOR", "IGD"));
                        spreadWriter.write(latexTable.replaceAll("INDICATOR", "Spread"));
                        GDWriter.write(latexTable.replaceAll("INDICATOR", "GD"));
                        coverageWriter.write(latexTable.replaceAll("INDICATOR", "Coverage"));

                        String bestHeuristicIGD = "NULL";
                        String bestHeuristicGD = "NULL";
                        String bestHeuristicSpread = "NULL";
                        String bestHeuristicCoverage = "NULL";

                        getBest:
                        {
                            double bestMeanIGD = Double.POSITIVE_INFINITY;
                            double bestMeanGD = Double.POSITIVE_INFINITY;
                            double bestMeanSpread = Double.NEGATIVE_INFINITY;
                            double bestMeanCoverage = Double.NEGATIVE_INFINITY;

                            for (String heuristic : igdMap.keySet()) {
                                double heuristicIGD = igdMap.get(heuristic);
                                double heuristicGD = gdMap.get(heuristic);
                                double heuristicSpread = spreadMap.get(heuristic);
                                double heuristicCoverage = coverageMap.get(heuristic);

                                if (heuristicIGD < bestMeanIGD) {
                                    bestMeanIGD = heuristicIGD;
                                    bestHeuristicIGD = heuristic;
                                }
                                if (heuristicGD < bestMeanGD) {
                                    bestMeanGD = heuristicGD;
                                    bestHeuristicGD = heuristic;
                                }
                                if (heuristicSpread > bestMeanSpread) {
                                    bestMeanSpread = heuristicSpread;
                                    bestHeuristicSpread = heuristic;
                                }
                                if (heuristicCoverage > bestMeanCoverage) {
                                    bestMeanCoverage = heuristicCoverage;
                                    bestHeuristicCoverage = heuristic;
                                }
                            }
                        }

                        StringBuilder igdBuilder = new StringBuilder();
                        StringBuilder gdBuilder = new StringBuilder();
                        StringBuilder spreadBuilder = new StringBuilder();
                        StringBuilder coverageBuilder = new StringBuilder();

                        String[] newHeuristicFunctions = new String[heuristicFunctions.length * (algorithms.length - 1) + algorithms.length];
                        fulfillNewHeuristics:
                        {
                            int i = 0;
                            for (String algorithm : algorithms) {
                                newHeuristicFunctions[i++] = algorithm;
                                if (!"MOEADD".equals(algorithm)) {
                                    for (String heuristicFunction : heuristicFunctions) {
                                        newHeuristicFunctions[i++] = algorithm + heuristicFunction;
                                    }
                                }
                            }
                        }

                        for (String heuristic : newHeuristicFunctions) {
                            igdBuilder.append(" & ");
                            boolean bold = heuristic.equals(bestHeuristicIGD) || igdMap.get(heuristic).equals(igdMap.get(bestHeuristicIGD));
                            if (bold) {
                                igdBuilder.append("\\textbf{");
                            }
                            igdBuilder.append(decimalFormatter.format(igdMap.get(heuristic)));
                            if (bold) {
                                igdBuilder.append("}");
                            }

                            gdBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicGD) || gdMap.get(heuristic).equals(gdMap.get(bestHeuristicGD));
                            if (bold) {
                                gdBuilder.append("\\textbf{");
                            }
                            gdBuilder.append(decimalFormatter.format(gdMap.get(heuristic)));
                            if (bold) {
                                gdBuilder.append("}");
                            }

                            spreadBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicSpread) || spreadMap.get(heuristic).equals(spreadMap.get(bestHeuristicSpread));
                            if (bold) {
                                spreadBuilder.append("\\textbf{");
                            }
                            spreadBuilder.append(decimalFormatter.format(spreadMap.get(heuristic)));
                            if (bold) {
                                spreadBuilder.append("}");
                            }

                            coverageBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicCoverage) || coverageMap.get(heuristic).equals(coverageMap.get(bestHeuristicCoverage));
                            if (bold) {
                                coverageBuilder.append("\\textbf{");
                            }
                            coverageBuilder.append(decimalFormatter.format(coverageMap.get(heuristic)));
                            if (bold) {
                                coverageBuilder.append("}");
                            }
                        }

                        IGDWriter.write(igdBuilder + "\\\\\n");
                        spreadWriter.write(spreadBuilder + "\\\\\n");
                        GDWriter.write(gdBuilder + "\\\\\n");
                        coverageWriter.write(coverageBuilder + "\\\\\n");
                    }
                    latexTableBuilder = new StringBuilder();

                    latexTableBuilder
                            .append("\t\t\\bottomrule\n")
                            .append("\t\\end{tabulary}\n")
                            .append("\\end{table}\n\n");
                }

                averages:
                {

                    latexTableBuilder.append("\\begin{table}[!htb]\n")
                            .append("\t\\centering\n")
                            .append("\t\\def\\arraystretch{1.5}\n")
                            //                        .append("\t\\setlength{\\tabcolsep}{10pt}\n")
                            //                        .append("\t\\fontsize{8pt}{10pt}\\selectfont\n")
                            .append("\t\\caption{INDICATOR averages found for ").append(objectives).append(" objectives}\n")
                            .append("\t\\label{tab:INDICATOR ").append(objectives).append(" objectives}\n")
                            .append("\t\\begin{tabulary}{\\linewidth}{c");

                    for (String algorithm : algorithms) {
                        latexTableBuilder.append("c");
                        if (!"MOEADD".equals(algorithm)) {
                            for (String heuristicFunction : heuristicFunctions) {
                                latexTableBuilder.append("c");
                            }
                        }
                    }

                    latexTableBuilder
                            .append("}\n")
                            .append("\t\t\\toprule\n")
                            .append("\t\t\\textbf{System}");
                    for (String algorithm : algorithms) {
                        latexTableBuilder.append(" & \\textbf{").append(algorithm).append("}");
                        if (!"MOEADD".equals(algorithm)) {
                            for (String heuristicFunction : heuristicFunctions) {
                                latexTableBuilder.append(" & \\textbf{").append(algorithm).append("-").append(heuristicFunction).append("}");
                            }
                        }
                    }
                    latexTableBuilder
                            .append("\\\\\n")
                            .append("\t\t\\midrule\n");

                    for (String problem : problems) {

                        NonDominatedSolutionList trueFront = new NonDominatedSolutionList();
                        pfTrueComposing:
                        {
                            for (String algorithm : algorithms) {
                                SolutionSet mecbaFront = metricsUtil.readNonDominatedSolutionSet("resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + objectives + "obj/All_FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem);
                                trueFront.addAll(mecbaFront);
                                if (!"MOEADD".equals(algorithm)) {
                                    for (String hyperHeuristic : heuristicFunctions) {
                                        SolutionSet front = metricsUtil.readNonDominatedSolutionSet("experiment/" + algorithm + "/" + objectives + "objectives/" + hyperHeuristic + "/" + problem + "/FUN.txt");
                                        trueFront.addAll(front);
                                    }
                                }
                            }
                        }
                        double[][] trueFrontMatrix = trueFront.writeObjectivesToMatrix();

                        HashMap<String, double[]> igdMap = new HashMap<>();
                        HashMap<String, double[]> gdMap = new HashMap<>();
                        HashMap<String, double[]> spreadMap = new HashMap<>();
                        HashMap<String, double[]> coverageMap = new HashMap<>();

                        mocaito:
                        {
                            for (String algorithm : algorithms) {
                                double[] mecbaIGDs = new double[EXECUTIONS];
                                double[] mecbaGDs = new double[EXECUTIONS];
                                double[] mecbaSpreads = new double[EXECUTIONS];
                                double[] mecbaCoverages = new double[EXECUTIONS];
                                for (int i = 0; i < EXECUTIONS; i++) {
                                    double[][] mecbaFront = metricsUtil.readFront("resultado/" + algorithm.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + objectives + "obj/FUN_" + algorithm.toLowerCase().replaceAll("-", "") + "-" + problem + "-" + i + ".NaoDominadas");

                                    mecbaIGDs[i] = igd.invertedGenerationalDistance(mecbaFront, trueFrontMatrix);
                                    mecbaGDs[i] = gd.generationalDistance(mecbaFront, trueFrontMatrix, objectives);
                                    mecbaSpreads[i] = spread.spread(mecbaFront, trueFrontMatrix, objectives);
                                    mecbaCoverages[i] = coverage.coverage(mecbaFront, trueFrontMatrix);
                                }
                                igdMap.put(algorithm, mecbaIGDs);
                                gdMap.put(algorithm, mecbaGDs);
                                spreadMap.put(algorithm, mecbaSpreads);
                                coverageMap.put(algorithm, mecbaCoverages);
                            }
                        }

                        for (String algorithm : algorithms) {
                            if (!"MOEADD".equals(algorithm)) {
                                for (String heuristic : heuristicFunctions) {
                                    double[] hhIGDs = new double[EXECUTIONS];
                                    double[] hhGDs = new double[EXECUTIONS];
                                    double[] hhSpreads = new double[EXECUTIONS];
                                    double[] hhCoverages = new double[EXECUTIONS];
                                    for (int i = 0; i < EXECUTIONS; i++) {
                                        double[][] hhFront = metricsUtil.readFront("experiment/" + algorithm + "/" + objectives + "objectives/" + heuristic + "/" + problem + "/EXECUTION_" + i + "/FUN.txt");

                                        hhIGDs[i] = igd.invertedGenerationalDistance(hhFront, trueFrontMatrix);
                                        hhGDs[i] = gd.generationalDistance(hhFront, trueFrontMatrix, objectives);
                                        hhSpreads[i] = spread.spread(hhFront, trueFrontMatrix, objectives);
                                        hhCoverages[i] = coverage.coverage(hhFront, trueFrontMatrix);
                                    }
                                    igdMap.put(algorithm + heuristic, hhIGDs);
                                    gdMap.put(algorithm + heuristic, hhGDs);
                                    spreadMap.put(algorithm + heuristic, hhSpreads);
                                    coverageMap.put(algorithm + heuristic, hhCoverages);
                                }
                            }
                        }

                        HashMap<String, HashMap<String, Boolean>> igdResult = KruskalWallisTest.test(igdMap);
                        HashMap<String, HashMap<String, Boolean>> gdResult = KruskalWallisTest.test(gdMap);
                        HashMap<String, HashMap<String, Boolean>> spreadResult = KruskalWallisTest.test(spreadMap);
                        HashMap<String, HashMap<String, Boolean>> coverageResult = KruskalWallisTest.test(coverageMap);

                        latexTableBuilder.append("\t\t").append(problem);

                        String latexTable = latexTableBuilder.toString();
                        latexTable = latexTable.replaceAll("O[OA]\\_", "")
                                .replaceAll("ChoiceFunction", "CF")
                                .replaceAll("MultiArmedBandit", "MAB");

                        IGDWriter.write(latexTable.replaceAll("INDICATOR", "IGD"));
                        spreadWriter.write(latexTable.replaceAll("INDICATOR", "Spread"));
                        GDWriter.write(latexTable.replaceAll("INDICATOR", "GD"));
                        coverageWriter.write(latexTable.replaceAll("INDICATOR", "Coverage"));

                        latexTableBuilder = new StringBuilder();

                        String bestHeuristicIGD = "NULL";
                        String bestHeuristicGD = "NULL";
                        String bestHeuristicSpread = "NULL";
                        String bestHeuristicCoverage = "NULL";

                        getBest:
                        {
                            double bestMeanIGD = Double.POSITIVE_INFINITY;
                            double bestMeanGD = Double.POSITIVE_INFINITY;
                            double bestMeanSpread = Double.NEGATIVE_INFINITY;
                            double bestMeanCoverage = Double.NEGATIVE_INFINITY;

                            for (String heuristic : igdMap.keySet()) {
                                double heuristicMeanIGD = mean.evaluate(igdMap.get(heuristic));
                                double heuristicMeanGD = mean.evaluate(gdMap.get(heuristic));
                                double heuristicMeanSpread = mean.evaluate(spreadMap.get(heuristic));
                                double heuristicMeanCoverage = mean.evaluate(coverageMap.get(heuristic));

                                if (heuristicMeanIGD < bestMeanIGD) {
                                    bestMeanIGD = heuristicMeanIGD;
                                    bestHeuristicIGD = heuristic;
                                }
                                if (heuristicMeanGD < bestMeanGD) {
                                    bestMeanGD = heuristicMeanGD;
                                    bestHeuristicGD = heuristic;
                                }
                                if (heuristicMeanSpread > bestMeanSpread) {
                                    bestMeanSpread = heuristicMeanSpread;
                                    bestHeuristicSpread = heuristic;
                                }
                                if (heuristicMeanCoverage > bestMeanCoverage) {
                                    bestMeanCoverage = heuristicMeanCoverage;
                                    bestHeuristicCoverage = heuristic;
                                }
                            }
                        }

                        StringBuilder igdBuilder = new StringBuilder();
                        StringBuilder gdBuilder = new StringBuilder();
                        StringBuilder spreadBuilder = new StringBuilder();
                        StringBuilder coverageBuilder = new StringBuilder();

                        String[] newHeuristicFunctions = new String[heuristicFunctions.length * (algorithms.length - 1) + algorithms.length];
                        fulfillNewHeuristics:
                        {
                            int i = 0;
                            for (String algorithm : algorithms) {
                                newHeuristicFunctions[i++] = algorithm;
                                if (!"MOEADD".equals(algorithm)) {
                                    for (String heuristicFunction : heuristicFunctions) {
                                        newHeuristicFunctions[i++] = algorithm + heuristicFunction;
                                    }
                                }
                            }
                        }

                        for (String heuristic : newHeuristicFunctions) {
                            igdBuilder.append(" & ");
                            boolean bold = heuristic.equals(bestHeuristicIGD) || !igdResult.get(heuristic).get(bestHeuristicIGD);
                            if (bold) {
                                igdBuilder.append("\\textbf{");
                            }
                            igdBuilder.append(decimalFormatter.format(mean.evaluate(igdMap.get(heuristic))) + " (" + decimalFormatter.format(standardDeviation.evaluate(igdMap.get(heuristic))) + ")");
                            if (bold) {
                                igdBuilder.append("}");
                            }

                            gdBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicGD) || !gdResult.get(heuristic).get(bestHeuristicGD);
                            if (bold) {
                                gdBuilder.append("\\textbf{");
                            }
                            gdBuilder.append(decimalFormatter.format(mean.evaluate(gdMap.get(heuristic))) + " (" + decimalFormatter.format(standardDeviation.evaluate(gdMap.get(heuristic))) + ")");
                            if (bold) {
                                gdBuilder.append("}");
                            }

                            spreadBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicSpread) || !spreadResult.get(heuristic).get(bestHeuristicSpread);
                            if (bold) {
                                spreadBuilder.append("\\textbf{");
                            }
                            spreadBuilder.append(decimalFormatter.format(mean.evaluate(spreadMap.get(heuristic))) + " (" + decimalFormatter.format(standardDeviation.evaluate(spreadMap.get(heuristic))) + ")");
                            if (bold) {
                                spreadBuilder.append("}");
                            }

                            coverageBuilder.append(" & ");
                            bold = heuristic.equals(bestHeuristicCoverage) || !coverageResult.get(heuristic).get(bestHeuristicCoverage);
                            if (bold) {
                                coverageBuilder.append("\\textbf{");
                            }
                            coverageBuilder.append(decimalFormatter.format(mean.evaluate(coverageMap.get(heuristic)))).append(" (").append(decimalFormatter.format(standardDeviation.evaluate(coverageMap.get(heuristic)))).append(")");
                            if (bold) {
                                coverageBuilder.append("}");
                            }
                        }

                        IGDWriter.write(igdBuilder + "\\\\\n");
                        spreadWriter.write(spreadBuilder + "\\\\\n");
                        GDWriter.write(gdBuilder + "\\\\\n");
                        coverageWriter.write(coverageBuilder + "\\\\\n");
                    }
                    latexTableBuilder
                            .append("\t\t\\bottomrule\n")
                            .append("\t\\end{tabulary}\n")
                            .append("\\end{table}\n\n");
                }

                latexTableBuilder.append("\\end{landscape}\n\n")
                        .append("\\end{document}");

                String latexTable = latexTableBuilder.toString();

                IGDWriter.write(latexTable);
                spreadWriter.write(latexTable);
                GDWriter.write(latexTable);
                coverageWriter.write(latexTable);
            }
        }
    }

}
