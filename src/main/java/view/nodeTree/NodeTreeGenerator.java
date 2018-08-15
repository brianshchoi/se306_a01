package view.nodeTree;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;
import view.nodeTree.Node;

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
    private HashMap<Task, Integer> _nodePositionX = new HashMap<>();
    private HashMap<Task, Integer> _nodePositionY = new HashMap<>();
    private double _tileWidth;
    private double _tileHeight;
    private int _totalLayers;

    public NodeTreeGenerator(TaskModel taskModel, double tileWidth, double tileHeight) {
        _taskModel = taskModel;
        this._tileWidth = tileWidth;
        this._tileHeight = tileHeight;

        makeNodePosition();
        _graphicPane = new Pane();
    }

    private Pane createNode (Task task){
        Pane node = new Node(task, NodeColor.GREEN, _taskModel.getTaskModelSize()).getStackPane();
        node.setLayoutX(_nodePositionX.get(task));
        node.setLayoutY(_nodePositionY.get(task));
        return node;
    }

    private void createEdge (Pane parent, Pane child){
        Line edge = new Line();

        // Find the starting and positions (i.e. find the middle of the parent and child nodes respectively)
        edge.startXProperty().bind(
                parent.layoutXProperty().add(parent.getBoundsInParent().getWidth() / 2.0));
        edge.startYProperty().bind(
                parent.layoutYProperty().add(parent.getBoundsInParent().getHeight() / 2.0));

        edge.endXProperty().bind(
                child.layoutXProperty().add(child.getBoundsInParent().getWidth() / 2.0));
        edge.endYProperty().bind(
                child.layoutYProperty().add(child.getBoundsInParent().getHeight() / 2.0));

        edge.setStroke(Color.AQUA);

        // Add the edge to parent pane
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


    public void makeNodePosition(){
        List<Task> scheduledTasks = new ArrayList<>();
        countLayers();
        int layer = 0;
        while(scheduledTasks.size() != _taskModel.getTasks().size()){
            List<Task> layeredTasks = layerTasks(scheduledTasks, _taskModel.getTasks());

            int count = 0;
                for (Task task : layeredTasks) {
                    int y = (int) Math.ceil(layer * ((_tileHeight * 0.9)/_totalLayers));
                    int x = (int) Math.ceil(_tileWidth/layeredTasks.size()/2 + count * _tileWidth/layeredTasks.size() - 50);
                    _nodePositionY.put(task, y);
                    _nodePositionX.put(task, x);

                    count++;
                }
            layer++;
            scheduledTasks.addAll(layeredTasks);
        }
    }

    private void countLayers() {
        List<Task> scheduledTasks = new ArrayList<>();
        _totalLayers = 0;
        while(scheduledTasks.size() != _taskModel.getTasks().size()){
            List<Task> layeredTasks = layerTasks(scheduledTasks, _taskModel.getTasks());
            _totalLayers++;
            scheduledTasks.addAll(layeredTasks);
        }
    }

    private List<Task> layerTasks(List<Task> schedule, List<Task> allTasks){
        List<Task> newLayerTasks = new ArrayList<>();
        // Create list of tasks which haven't been scheduled yet
        allTasks.removeAll(schedule);

        // Check if each unscheduled task's dependencies have been satisfied
        for (Task task: allTasks){
            if (schedule.containsAll(task.getParents())) {
                newLayerTasks.add(task);
            }
        }
        return newLayerTasks;
    }

}

