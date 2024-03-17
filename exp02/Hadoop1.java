import java.io.*;

public class Hadoop1 {
    public static void main(String[] args) {
        String localFilePath = "sample.txt";
        String hdfsFilePath = "/exp02/newfile.txt";
        try {
            String content = "BDA Programming lab";
            writeToFile(localFilePath, content);
            System.out.println("Content written to local file successfully.");
            uploadFileToHDFS(localFilePath, hdfsFilePath);
            System.out.println("File uploaded to HDFS successfully.");
            String hdfsContent = readFileFromHDFS(hdfsFilePath);
            System.out.println("File content from HDFS:");
            System.out.println(hdfsContent);
            deleteFileFromHDFS(hdfsFilePath);
            System.out.println("File deleted from HDFS successfully.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static void uploadFileToHDFS(String localFilePath, String hdfsFilePath) throws IOException, InterruptedException {
        executeHadoopCommand(new String[]{"hadoop", "fs", "-put", localFilePath, hdfsFilePath});
    }

    private static String readFileFromHDFS(String hdfsFilePath) throws IOException, InterruptedException {
        return executeHadoopCommand(new String[]{"hadoop", "fs", "-cat", hdfsFilePath});
    }

    private static void deleteFileFromHDFS(String hdfsFilePath) throws IOException, InterruptedException {
        executeHadoopCommand(new String[]{"hadoop", "fs", "-rm", hdfsFilePath});
    }

    private static String executeHadoopCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exitCode == 0 ? process.getInputStream() : process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        if (exitCode != 0) {
            throw new IOException("Failed to execute Hadoop command. Exit code: " + exitCode + ". Error: " + output);
        }
        return output.toString().trim();
    }
}
