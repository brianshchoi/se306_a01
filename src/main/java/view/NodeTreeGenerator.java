package view;

import javafx.scene.layout.Pane;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class takes in a TaskModel and generates a graphical representation of the dependencies
 */
public class NodeTreeGenerator {

    private TaskModel _taskModel;
    private ISchedule _schedule;
    private Pane _graphicPane;
    private int x;

    NodeTreeGenerator(TaskModel taskModel) {
        _taskModel = taskModel;
        _graphicPane = new Pane();
        x = 0;
    }

    private void createNode (Task task){
        Pane node = new Node(task.getName(), task.getWeight(), "blue").getStackPane();
        node.setLayoutX(x);
        x = x + 100;

        _graphicPane.getChildren().add(
                node
        );
    }

    //TODO: create edge between child and parent
    private Node createEdge (){
        return null;
    }

    public void a() {

        for (Task task : _taskModel.getTasks()) {
            int startTime = _schedule.getFinishTimeOf(task) - task.getWeight();
            IProcessor processor = _schedule.getProcessorOf(task);

            // Add all task
//            output.append(DotRenderer.addNode(task, startTime, processor));

            // Add dependencies and their weight
            if (task.getParents().size() > 0) {
                List<Task> parentsInOrderByName = new ArrayList<>(task.getParents());
                Collections.sort(parentsInOrderByName);
                for (Task parent : parentsInOrderByName) {
//                    output.append(DotRenderer.addDependency(parent, task));
                }
            }
        }
    }

    public Pane getGraphicPane() {
        for (Task task: _taskModel.getTasks()){
            createNode(task);
        }


        return _graphicPane;
    }
}

