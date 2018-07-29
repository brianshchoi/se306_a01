package app;

import model.Task;
import model.TaskModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    private File file;
    private FileReader fr;

    public FileParser(String filename) {
        this.file = new File(filename);
        try {
            this.fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TaskModel getTaskModelFromFile() {
        return null;
    }
}
