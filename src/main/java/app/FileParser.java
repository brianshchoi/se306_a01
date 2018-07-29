package app;

import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import model.Task;
import model.TaskModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileParser {
    private FileInputStream fileInputStream;
    private TaskModel taskModel = new TaskModel();

    public FileParser(File file) throws FileNotFoundException {
        fileInputStream = new FileInputStream(file);
    }

    public TaskModel getTaskModelFromFile() {
        GraphParser parser = new GraphParser(fileInputStream);

        for (GraphNode node: parser.getNodes().values()) {
            taskModel.addTask(new Task(node.getId(), (Integer) node.getAttributes().get("Weight")));
        }

        return taskModel;
    }
}
