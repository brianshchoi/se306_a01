package app;

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
    private TaskModel taskModel = new TaskModel();

    public FileParser(File file) throws FileNotFoundException {
        fileInputStream = new FileInputStream(file);
    }

    public TaskModel getTaskModelFromFile() {
        GraphParser parser = new GraphParser(fileInputStream);

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

    // TODO: remove static modifier
    // TODO: maybe need to add a field for graph name in TaskModel??
    private static void writeScheduleToFile(ISchedule schedule, TaskModel taskModel) {
        DotGraph graph = new DotGraph("man", schedule, taskModel);
        graph.render();
    }

    //Temporary testing method
    public static void main (String[] args){
        // Static model created based on model digraph on project description pdf
        TaskModel model = new TaskModel();

        Task a = new Task("a", 2);
        Task b = new Task("b", 3);
        Task c = new Task("c", 3);
        Task d = new Task("d", 2);

        model.addTask(a);
        model.addTask(b);
        model.addTask(c);
        model.addTask(d);

        model.addDependency(a,b,1);
        model.addDependency(a,c,2);
        model.addDependency(b,d,2);
        model.addDependency(b,d,1);

        ISchedule s = new Schedule(2);

        // Needed here to get processor object
        List<IProcessor> processors = s.getProcessors();
        IProcessor p1 = processors.get(0);
        IProcessor p2 = processors.get(1);

        s.schedule(a, p1, 0);
        s.schedule(b, p1, 2);
        s.schedule(c, p2, 4);
        s.schedule(d, p2, 7);

        writeScheduleToFile(s, model);
    }
}
