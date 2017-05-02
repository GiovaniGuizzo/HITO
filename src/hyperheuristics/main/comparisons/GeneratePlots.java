package hyperheuristics.main.comparisons;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneratePlots {

    public static void main(String[] args) throws IOException, InterruptedException {
        int[] numberOfObjectivesArray = new int[]{2};
        String[] objectives = new String[]{"A", "O", "R", "P"};

        String[] problems = new String[]{
            "Guava"
        };

        int[] points = new int[]{1, 2, 4, 6, 8, 12, 14, 10};
        int pointIndex = 0;

        String[] heuristicFunctions = new String[]{
            LowLevelHeuristic.CHOICE_FUNCTION
//            LowLevelHeuristic.MULTI_ARMED_BANDIT,
//            LowLevelHeuristic.RANDOM,
        };

        String[] algorithms = new String[]{
            "NSGA-II"
        };

        for (int numberOfObjectives : numberOfObjectivesArray) {
            for (String problem : problems) {
                pointIndex = 0;
                if (numberOfObjectives == 2) {
                    File inputScript = File.createTempFile("input", ".gnu");
                    inputScript.deleteOnExit();
                    try (FileWriter scriptWriter = new FileWriter(inputScript)) {
                        File outputFile = new File("experimentnew/" + numberOfObjectives + "objectives/" + problem + ".png");
                        if (!outputFile.getParentFile().exists()) {
                            outputFile.getParentFile().mkdirs();
                        }
                        outputFile.createNewFile();
                        scriptWriter.append("set terminal png size 700,500\n"
                                + "set grid\n"
                                + "set key out\n"
                                + "\n"
                                + "##########\n"
                                + "# PARETO #\n"
                                + "##########\n"
                                + "\n"
                                + "set xlabel \"Operations\"\n"
                                + "set ylabel \"Attributes\"\n"
                                + "set pointsize 2\n")
                                .append("set output '" + outputFile.getAbsolutePath() + "'\n")
                                .append("plot");
                        for (String algorithm : algorithms) {
                            String algorithmNew = algorithm.replaceAll("-", "").replaceAll("\\/", "");
                            scriptWriter.append(" \"resultado/" + algorithmNew.toLowerCase().replaceAll("-", "") + "/" + problem + "_Comb_" + numberOfObjectives + "obj/All_FUN_" + algorithmNew.toLowerCase().replaceAll("-", "") + "-" + problem + "\"")
                                    .append(" using 1:2 title \"" + algorithm + "\" with points ls " + points[pointIndex++ % points.length] + ",");
                            if (!"MOEA/DD".equals(algorithm)) {
                                for (String heuristicFunction : heuristicFunctions) {
                                    scriptWriter.append(" \"experimentnew/" + algorithmNew + "/" + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problem + "/FUN.txt\"")
                                            .append(" using 1:2 title \"HITO\" with points ls " + points[pointIndex++ % points.length] + ",");
//                                        .append(" using 1:2 title \"" + algorithm + "-" + heuristicFunction + "\"");
                                }
                            }
                        }
                        scriptWriter.append("\n");
                        scriptWriter.append("clear");
                    }
                    ProcessBuilder processBuilder = new ProcessBuilder("gnuplot", inputScript.getAbsolutePath());
                    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                    processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                    processBuilder.start().waitFor();
                    inputScript.delete();
                } else {
                    for (int obj = 2; obj < numberOfObjectives; obj++) {
                        File outputFile = new File("experimentnew/" + numberOfObjectives + "objectives/" + problem + "_" + (obj - 1) + ".gnu");
                        if (!outputFile.getParentFile().exists()) {
                            outputFile.getParentFile().mkdirs();
                        }
                        outputFile.createNewFile();
                        try (FileWriter scriptWriter = new FileWriter(outputFile)) {

                            if (!outputFile.getParentFile().exists()) {
                                outputFile.getParentFile().mkdirs();
                            }
                            outputFile.createNewFile();
                            scriptWriter.append("# Generating " + problem + "\n")
                                    .append("set terminal x11\n")
                                    .append("set key\n")
                                    .append("set grid\n");
                            for (int i = 0; i < 2; i++) {
                                scriptWriter.append("set " + (i == 0 ? "x" : "y") + "label '" + objectives[i] + "'\n");
                            }
                            scriptWriter.append("set zlabel '" + objectives[obj] + "'\n")
                                    .append("splot");
                            for (String algorithm : algorithms) {
                                for (String heuristicFunction : heuristicFunctions) {
                                    scriptWriter.append(" \"experimentnew/" + algorithm + "/" + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problem + "/FUN.txt\"")
                                            .append(" using 1:2:" + (obj + 1) + " title \"HITO-" + heuristicFunction.replaceAll("ChoiceFunction", "CF").replaceAll("MultiArmedBandit", "MAB") + "\",");
//                                        .append(" using 1:2 title \"" + algorithm + "-" + heuristicFunction + "\"");
                                }
                            }
                            scriptWriter.append("\n");
                        }
                    }
                }
            }
        }
    }

}
