import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HadoopCLI {

    public static void executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void createDirectory(String path) throws IOException {
        executeCommand("hadoop fs -mkdir " + path);
    }

    public static void addFile(String sourceLocal, String destHDFS) throws IOException {
        executeCommand("hadoop fs -copyFromLocal " + sourceLocal + " " + destHDFS);
    }

    public static void listFiles(String directoryPath) throws IOException {
        executeCommand("hadoop fs -ls " + directoryPath);
    }

    public static void retrieveFile(String sourceHDFS, String destLocal) throws IOException {
        executeCommand("hadoop fs -copyToLocal " + sourceHDFS + " " + destLocal);
    }

    public static void deleteFile(String path) throws IOException {
        executeCommand("hadoop fs -rm -r " + path);
    }

    public static void main(String[] args) {
        try {
            String directoryPath = "/user/hadoop/testdir";
            String filePathHDFS = "/user/hadoop/testdir/example.txt";
            String localFilePath = "./example.txt"; // Ensure this file exists on your local system
            String localFileDestination = "./example_downloaded.txt";

            createDirectory(directoryPath);
            addFile(localFilePath, filePathHDFS);
            listFiles(directoryPath);
            retrieveFile(filePathHDFS, localFileDestination);
            // deleteFile(filePathHDFS);
            // Optionally, delete the directory if needed
            // deleteFile(directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
