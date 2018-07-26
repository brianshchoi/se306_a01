package app;

import model.TaskModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    private File file;
    private FileReader fr;

    // Input absolute path for filename
    public FileParser(String filename) {
        this.file = new File(filename);
        try {
            this.fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public TaskModel getTaskModelFromFile() throws IOException {
    public void getTaskModelFromFile() {
        // Read .dot file from file directory
        try {
            BufferedReader br = new BufferedReader(fr);
            String currentLine;

            // While there is another line, do shit
            while ((currentLine = br.readLine()) != null) {
                List<String> extract = extractDetails(currentLine);

                for (String anExtract : extract) {
                    System.out.println(anExtract);
                }
                System.out.println(currentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO:  get TaskModelInstance
        /* TODO:
        * Read through file
        * Filter out dependencies
        * Create Task objects and add to the TaskModel
        * Then iterate through the the dependencies and create them by invoking
         * addDependency() on the TaskModel object*/
//        return null;
    }

    /**
     * Use this method to take a given line in a file and extract the important details.
     * e.g.
     * Passing "    a   [Weight=2];" will return a List<String> containing "a" and "2"
     * Passing "    a -> b   [Weight=1];" will return a List<String> containing "a", "b", and 1
     * @param line
     * @return
     */
    private List<String> extractDetails(String line) {
        // Check if line is a dependency
        if (line.contains("->")){


        }

        System.out.println(line.contains("->"));
        line = line.trim();
        Pattern pattern = Pattern.compile("\\w\\s+|\\d+");
        Matcher matcher = pattern.matcher(line);
        List<String> out = new ArrayList<>();
        while (matcher.find()) {
            out.add(matcher.group());
        }

        return out;
    }

    // Returns the absolute pathname string of this abstract pathname.
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}
