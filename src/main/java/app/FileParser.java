package app;

import model.TaskModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileParser {
    private FileInputStream fileInputStream;

    public FileParser(File file) throws FileNotFoundException {
        fileInputStream = new FileInputStream(file);
    }

    public TaskModel getTaskModelFromFile() {
        return null;
    }
}
