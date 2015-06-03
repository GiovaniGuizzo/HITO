package hyperheuristics.main.comparisons;

import hyperheuristics.lowlevelheuristic.LowLevelHeuristic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.PseudoRandom;

public class GeneratePlots {

    public static void main(String[] args) throws IOException, InterruptedException {
        int[] numberOfObjectivesArray = new int[]{2, 4};
        String[] objectives = new String[]{"A", "O", "R", "P"};

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
            LowLevelHeuristic.MULTI_ARMED_BANDIT, //            LowLevelHeuristic.RANDOM
        };

        String[] algorithms = new String[]{
            "NSGA-II", //            "SPEA2"
        };

        for (int numberOfObjectives : numberOfObjectivesArray) {
            for (String problem : problems) {
                if (numberOfObjectives == 2) {
                    File inputScript = File.createTempFile("input", ".gnu");
                    inputScript.deleteOnExit();
                    try (FileWriter scriptWriter = new FileWriter(inputScript)) {
                        File outputFile = new File("experiment/" + numberOfObjectives + "objectives/" + problem + ".png");
                        if (!outputFile.getParentFile().exists()) {
                            outputFile.getParentFile().mkdirs();
                        }
                        outputFile.createNewFile();
                        scriptWriter.append("# Generating " + problem + "\n")
                                .append("set terminal png size 400,400 enhanced font 'arial' 10 butt\n")
                                .append("set output '" + outputFile.getAbsolutePath() + "'\n")
                                .append("set key\n")
                                .append("set grid\n");
                        for (int i = 0; i < 2; i++) {
                            scriptWriter.append("set " + (i == 0 ? "x" : "y") + "label '" + objectives[i] + "'\n");
                        }
                        scriptWriter.append("plot");
                        for (String algorithm : algorithms) {
                            for (String heuristicFunction : heuristicFunctions) {
                                scriptWriter.append(" \"experiment/" + algorithm + "/" + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problem + "/FUN.txt\"")
                                        .append(" using 1:2 title \"HITO-" + heuristicFunction.replaceAll("ChoiceFunction", "CF").replaceAll("MultiArmedBandit", "MAB") + "\",");
//                                        .append(" using 1:2 title \"" + algorithm + "-" + heuristicFunction + "\"");
                            }
                        }
                        scriptWriter.append("\n");
                        scriptWriter.append("clear");

                        ProcessBuilder processBuilder = new ProcessBuilder("gnuplot", inputScript.getAbsolutePath());
                        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                        processBuilder.start().waitFor();
                        inputScript.delete();
                    }
                } else {
                    for (int obj = 2; obj < numberOfObjectives; obj++) {
                        File outputFile = new File("experiment/" + numberOfObjectives + "objectives/" + problem + "_" + (obj - 1) + ".gnu");
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
                                    scriptWriter.append(" \"experiment/" + algorithm + "/" + numberOfObjectives + "objectives/" + heuristicFunction + "/" + problem + "/FUN.txt\"")
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
