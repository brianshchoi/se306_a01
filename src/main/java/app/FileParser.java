package app;

import model.TaskModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    private File file;

    // Input absolute path for filename
    public FileParser(String filename) {
        this.file = new File(filename);
    }

    public TaskModel getTaskModelFromFile() {
        //TODO:  get TaskModelInstance
        /* TODO:
        * Read through file
        * Filter out dependencies
        * Create Task objects and add to the TaskModel
        * Then iterate through the the dependencies and create them by invoking
         * addDependency() on the TaskModel object*/
        return null;
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
        line.trim();
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
}
