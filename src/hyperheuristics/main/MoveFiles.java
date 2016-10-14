package hyperheuristics.main;

import java.io.File;

public class MoveFiles {

    public static void main(String[] args) {
        int objectives = 2;
        int executions = 30;
        String moea = "nsgaii";
        String newMoeaName = "NSGA-II";
        File origin = new File("experiment/" + moea);
        File target = new File("experiment/" + objectives + "objectives/" + newMoeaName + "/");
        target.mkdir();

        for (File problemPath : origin.listFiles(file -> file.isDirectory() && file.getName().contains(objectives + "obj"))) {
            File renameToProblem = new File(target.getAbsolutePath() + "/" + problemPath.getName().replaceAll("\\_Comb\\_[\\d]obj", ""));
            problemPath.renameTo(renameToProblem);
            for (int i = 0; i < executions; i++) {
                final int execution = i;
                File executionDir = new File(renameToProblem.getAbsolutePath() + "/EXECUTION_" + execution);
                executionDir.mkdir();
                for (File resultFile : renameToProblem.listFiles(file -> file.getName().contains("-" + execution + "."))) {
                    File resultFileRenameTo = new File(executionDir.getAbsolutePath() + "/" + resultFile.getName().substring(0, 3) + ".txt");
                    resultFile.renameTo(resultFileRenameTo);
                }
            }
        }
    }

}
