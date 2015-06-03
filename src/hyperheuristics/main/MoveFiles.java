package hyperheuristics.main;

import java.io.File;

public class MoveFiles {

    private static void changeName(File file) {
        File renameTo = new File(file.getAbsolutePath().replaceAll("nsgaii", "spea2"));
        file.renameTo(renameTo);
        File[] listFiles = renameTo.listFiles();
        if (listFiles != null) {
            for (File dir : listFiles) {
                changeName(dir);
            }
        }
    }

    public static void main(String[] args) {
        File root = new File("resultado/spea2/");
        changeName(root);
    }

}
