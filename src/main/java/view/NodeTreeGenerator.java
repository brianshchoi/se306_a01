package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class takes in a TaskModel and generates a graphical representation of the dependencies
 */
public class NodeTreeGenerator {

    private HashMap<String, Pane> taskMap = new HashMap<>();
    private TaskModel _taskModel;
//    private ISchedule _schedule;
    private Pane _graphicPane;

    NodeTreeGenerator(TaskModel taskModel) {
        _taskModel = taskModel;
        _graphicPane = new Pane();
    }

    private Pane createNode (Task task){
        Pane node = new Node(task, "blue").getStackPane();
        node.setLayoutX((int) Math.ceil(Math.random() * 800));
        node.setLayoutY((int) Math.ceil(Math.random() * 800));
        return node;
    }

    //TODO: create edge between child and parent
    private void createEdge (Pane parent, Pane child){
        Line edge = new Line();

        // Find the starting and e
        edge.startXProperty().bind(
                parent.layoutXProperty().add(parent.getBoundsInParent().getWidth() / 2.0));
        edge.startYProperty().bind(
                parent.layoutYProperty().add(parent.getBoundsInParent().getHeight() / 2.0));

        edge.endXProperty().bind(
                child.layoutXProperty().add(child.getBoundsInParent().getWidth() / 2.0));
        edge.endYProperty().bind(
                child.layoutYProperty().add(child.getBoundsInParent().getHeight() / 2.0));

        edge.setStroke(Color.AQUA);
        _graphicPane.getChildren().add(edge);
    }

    public Pane getGraphicPane() {
        for (Task task: _taskModel.getTasks()){
            // create a graphical node that is represented by a pane
            Pane taskNode = createNode(task);
            // Add the 'node' pane onto the tree pane.
            _graphicPane.getChildren().add((taskNode));


            taskMap.put(task.getName(), taskNode);

            if (task.getParents().size() > 0) {
                List<Task> parentsInOrderByName = new ArrayList<>(task.getParents());
                Collections.sort(parentsInOrderByName);
                for (Task parent : parentsInOrderByName) {
                    Pane parentNode = taskMap.get(parent.getName());

                    createEdge(parentNode, taskNode);
                }
            }



        }
        return _graphicPane;
    }
}

