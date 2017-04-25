package hyperheuristics.main.comparisons;

import hyperheuristics.igd.IGDHandler;
import hyperheuristics.statistics.EffectSize;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GenerateEffectSizeBoxPlot {

    public static void main(String[] args) throws IOException, InterruptedException {

        int executions = 30;

        int objectives = 4;

        String[] problems = new String[]{
            "OO_MyBatis",
            "OA_AJHsqldb",
            "OA_AJHotDraw",
            "OO_BCEL",
            "OO_JHotDraw",
            "OA_HealthWatcher",
            //            "OA_TollSystems",
            "OO_JBoss"
        };

        String[] algorithms = new String[]{
            "HITO-CF", "MOEADD", "NSGA-II", "HITO-MAB", "HITO-R", //            "NSGAII", "MOEADD"
        };

        File gnuScript = File.createTempFile("GNUPLOT", ".txt");
        gnuScript.deleteOnExit();
        File dataSet = File.createTempFile("GNUDATA", ".txt");
        dataSet.deleteOnExit();

        try (FileWriter gnuScriptWriter = new FileWriter(gnuScript);
                FileWriter dataSetWriter = new FileWriter(dataSet)) {

            dataSetWriter.write("# Effect sizes for algorithm " + algorithms[0] + ":\t" + Arrays.stream(algorithms).skip(1).collect(Collectors.joining("\t")));
            dataSetWriter.write("\n");

            gnuScriptWriter.write("set term png size 1024,768 \n"
                    + "set tics font \"Verdana,18\"\n"
                    + "set output \"experiment/" + objectives + "objectives/" + algorithms[0] + ".png\"\n"
                    + "algorithms = \"" + Arrays.stream(algorithms).skip(1).collect(Collectors.joining(" ")).replaceAll("MOEADD", "MOEA/DD") + "\"\n"
                    + "unset key\n"
                    + "set grid ytics\n"
                    + "\n"
                    + "set style fill solid 1 border -1\n"
                    + "set border 3\n"
                    + "set style data boxplot\n"
                    + "set style line 7 linecolor \"white\" lw 2\n"
                    + "\n"
                    + "set ylabel \"Effect Size Value\" font \"Verdana,18\"\n"
                    + "set ytics 0.1 nomirror\n"
                    + "set yrange [0:1]"
                    + "\n"
                    + "set xlabel \"Algorithms to which " + algorithms[0] + " was compared\" font \"Verdana,18\"\n"
                    + "set xtics 1 nomirror\n"
                    + "set format x \"\"\n"
                    + "\n"
                    + "set for[i=1:words(algorithms)] xtics add (word(algorithms,i) i)\n"
                    + "set datafile separator '\\t'\n"
                    + "plot "
                    + "0.5 lc 'black' title '', "
                    //                    + "0.56 lc 'blue' title '', "
                    //                    + "0.44 lc 'blue' title '', "
                    //                    + "0.64 lc 'green' title '', "
                    //                    + "0.36 lc 'green' title '', "
                    //                    + "0.71 lc 'red' title '', "
                    //                    + "0.29 lc 'red' title '', "
                    + "for[i=1:words(algorithms)] '" + dataSet.getAbsolutePath() + "' using (i):i linestyle 7\n");

            for (String problem : problems) {

//                HypervolumeHandler calculator = new HypervolumeHandler();
                IGDHandler calculator = new IGDHandler();

                for (String algorithm : algorithms) {
                    String path = "experiment/" + objectives + "objectives/" + algorithm + "/" + problem + "/";
                    for (int j = 0; j < executions; j++) {
                        calculator.addParetoFront(path + "EXECUTION_" + j + "/FUN.txt");
                    }
                }

                HashMap<String, double[]> valuesHashMap = new HashMap<>();

                for (String algorithm : algorithms) {
                    String path = "experiment/" + objectives + "objectives/" + algorithm + "/" + problem + "/";

                    double[] values = new double[executions];

                    for (int j = 0; j < executions; j++) {
//                        values[j] = calculator.calculateHypervolume(path + "EXECUTION_" + j + "/FUN.txt", objectives);
                        values[j] = calculator.calculateIGD(path + "EXECUTION_" + j + "/FUN.txt", objectives);
                    }

                    valuesHashMap.put(algorithm.replaceAll("-", ""), values);
                }

                HashMap<String, HashMap<String, Double>> valuesEffectSize = EffectSize.computeEffectSize(valuesHashMap);

                String groupA = algorithms[0];
                for (int j = 1; j < algorithms.length; j++) {
                    String groupB = algorithms[j];
//                    double value = valuesEffectSize.get(groupA.replaceAll("-", "")).get(groupB.replaceAll("-", ""));
                    double value = 1 - valuesEffectSize.get(groupA.replaceAll("-", "")).get(groupB.replaceAll("-", ""));
                    dataSetWriter.write(String.valueOf(value) + "\t");
                }
                dataSetWriter.write("\n");
            }
        }

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(System.getProperty("os.name").contains("win") ? "gnuplot.exe" : "gnuplot", gnuScript.getAbsolutePath());
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.start().waitFor();

        gnuScript.delete();
        dataSet.delete();
    }
}
