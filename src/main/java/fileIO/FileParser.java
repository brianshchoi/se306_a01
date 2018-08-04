package fileIO;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.Schedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FileParser {
    private FileInputStream fileInputStream;
    private TaskModel taskModel;

    public FileParser(File file) throws FileNotFoundException {
        fileInputStream = new FileInputStream(file);
    }

    public TaskModel getTaskModelFromFile() {
        GraphParser parser = new GraphParser(fileInputStream);
        String graphId = parser.getGraphId();

        // Remove quotation marks from title if there are any
        if ((graphId.charAt(0) == '"') && (graphId.charAt(graphId.length() - 1) == '"')) {
            graphId = graphId.substring(1, graphId.length() - 1);
        }
        taskModel = new TaskModel(graphId);

        // Parse all the nodes into Task objects
        for (GraphNode node: parser.getNodes().values()) {
            String name = node.getId();
            int weight = Integer.parseInt(node.getAttribute("Weight").toString());
            taskModel.addTask(new Task(name, weight));
        }

        // Add all the dependencies between the Task objects
        for (GraphEdge edge: parser.getEdges().values()) {
            String nameOfParent = edge.getNode1().getId();
            String nameOfChild = edge.getNode2().getId();
            int cost = Integer.parseInt(edge.getAttribute("Weight").toString());

            Task parent = taskModel.get(nameOfParent);
            Task child = taskModel.get(nameOfChild);
            taskModel.addDependency(parent, child, cost);
        }

        return taskModel;
    }
}
