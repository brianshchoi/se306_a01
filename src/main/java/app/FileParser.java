package app;

import model.TaskModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
