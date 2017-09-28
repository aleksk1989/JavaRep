package lab01.main;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> fileNames = new ArrayList<>();

        for (String file: args) {
            fileNames.add(file);
        }

        ThreadsManager.getInstance().addFiles(fileNames);
        ThreadsManager.getInstance().execute();

    }

}
