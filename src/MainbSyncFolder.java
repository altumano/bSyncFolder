import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by jan on 07.01.16.
 *
 * Command-line synchronization app.
 * Works with 2 directories - %Main and %Target.
 * Compares %Main and %Target directories:
 *  - if %Main has new file copies them to %Target
 *  - if %Target has files that not exist in %Main deletes them
 */
public class MainbSyncFolder {
    private static File mainPath;
    private static File targetPath;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//        Welcome message
        System.out.println("Welcome to bSyncFolder!");
        System.out.println();

//        Main folder path - the folder which will be original with original files to be synced with
        System.out.println("Please specify main folder path:");
        mainPath = new File(reader.readLine());

        while (!mainPath.isDirectory()) {
            System.out.println("Something is wrong in path you specified. Please specify the correct PATH TO DIRECTORY:");
            mainPath = new File(reader.readLine());
        }

//        Target folder path - here will appear all files from main folder, also if the files which are in target
//        folder and doesn't exist in main will be deleted
        System.out.println("Please specify target folder path:");
        targetPath = new File(reader.readLine());

        while (!targetPath.isDirectory()) {
            System.out.println("Something is wrong in path you specified. Please specify the correct PATH TO DIRECTORY:");
            targetPath = new File(reader.readLine());
        }

        sync();

//        Closing all
        reader.close();
    }

    private static void sync() throws IOException { // ToDo: to implement IOException
        ArrayList<String> mainFiles = new ArrayList<>(Arrays.asList(mainPath.list()));
        ArrayList<String> targetFiles = new ArrayList<>(Arrays.asList(targetPath.list()));
        ArrayList<String> filesToBeAdded = new ArrayList<>();

        for (String file:mainFiles) {
            if (targetFiles.contains(file)) targetFiles.remove(file);
            else filesToBeAdded.add(file);
        }

        if (targetFiles.isEmpty() && filesToBeAdded.isEmpty()) {
            System.out.println("There is nothing to sync. All files are synced. Exiting... ");
            System.exit(0);
        }

        System.out.println("Files to be deleted in target folder(" + targetPath.getAbsolutePath() + "):");
        for (String fileName : targetFiles) {
            System.out.println(fileName);
        }

        System.out.println();

        System.out.println("Files to be added to target folder(" + targetPath.getAbsolutePath() + "):");
        for (String fileName : filesToBeAdded) {
            System.out.println(fileName);
        }

        System.out.println();

        System.out.println("Are you sure you want to continue? (y/n)");
        String answer = String.valueOf(new Scanner(System.in).next());

        while (!(answer.equals("n") || answer.equals("N") || answer.equals("y") || answer.equals("Y"))) {
            System.out.println("Incorrect answer. Please type correct letter: ");
            answer = String.valueOf(new Scanner(System.in).next());
        }

        if (answer.equals("n") || answer.equals("N")) {
            System.out.println("Cancelled by user. Exiting... ");
            System.exit(0);
        }

//        Checking for path separator.
        String pathSeparator;
        if (targetPath.getAbsolutePath().startsWith("/")) pathSeparator = "/";
        else pathSeparator = "\\";

//        Deleting files
        for (String fileName : targetFiles) {
            Files.delete(Paths.get(targetPath.getPath() + pathSeparator + fileName));
        }

//        Copying files
        for (String fileName : filesToBeAdded) {
            Files.copy(Paths.get(mainPath.getPath() + pathSeparator + fileName), Paths.get(targetPath.getPath() + pathSeparator + fileName));
        }

        System.out.println();

        System.out.println("All files are synchronized. Exiting... ");
    }
}
