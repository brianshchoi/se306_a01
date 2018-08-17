package view.nodeTree;

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
    private TaskModel taskModel;
//    private ISchedule _schedule;
    private Pane graphicPane;
    private HashMap<Task, Integer> nodePositionX = new HashMap<>();
    private HashMap<Task, Integer> nodePositionY = new HashMap<>();
    private double tileWidth;
    private double tileHeight;
    private int totalLayers;

    public NodeTreeGenerator(TaskModel taskModel, double tileWidth, double tileHeight) {
        this.taskModel = taskModel;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        makeNodePosition();
        graphicPane = new Pane();
    }

    /**
     * Takes a task object and make a node in right position
     * @param task
     * @return Pane
     */
    private Pane createNode (Task task){
        Pane node = new Node(task, NodeColor.GREEN, taskModel.getTaskModelSize()).getStackPane();
        node.setLayoutX(nodePositionX.get(task));
        node.setLayoutY(nodePositionY.get(task));
        return node;
    }

    /**
     * Takes the Parent and Child node and create a line between them
     * @param parent
     * @param child
     */
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
        graphicPane.getChildren().add(edge);
    }

    /**
     * Render the Nodes and Edges on a Pane
     * @return Pane
     */
    public Pane getGraphicPane() {
        // Draw Nodes
        for (Task task: taskModel.getTasks()){
            // create a graphical node that is represented by a pane
            Pane taskNode = createNode(task);
            // Add the 'node' pane onto the tree pane.
            graphicPane.getChildren().add((taskNode));

            taskMap.put(task.getName(), taskNode);
        }

        // Draw edges
        for (Task task: taskModel.getTasks()) {
            if (task.getParents().size() > 0) {
                Pane childNode = taskMap.get(task.getName());
                List<Task> parentsInOrderByName = new ArrayList<>(task.getParents());
                Collections.sort(parentsInOrderByName);
                for (Task parent : parentsInOrderByName) {
                    Pane parentNode = taskMap.get(parent.getName());

                    createEdge(parentNode, childNode);
                }
            }
        }
        return graphicPane;
    }

    /**
     * Calculates and set the X and Y position of each node to HashMaps
     */
    public void makeNodePosition(){
        List<Task> scheduledTasks = new ArrayList<>();
        countLayers();
        int layer = 0;
        while(scheduledTasks.size() != taskModel.getTasks().size()){
            List<Task> layeredTasks = layerTasks(scheduledTasks, taskModel.getTasks());
            // Divide each layer with number of nodes and place them accordingly
            int count = 0;
                for (Task task : layeredTasks) {
                    int y = (int) Math.ceil(layer * ((tileHeight * 0.9)/ totalLayers));
                    int x = (int) Math.ceil(tileWidth /layeredTasks.size()/2 + count * tileWidth /layeredTasks.size());
                    nodePositionY.put(task, y);
                    nodePositionX.put(task, x);
                    count++;
                }
            layer++;
            scheduledTasks.addAll(layeredTasks);
        }
    }

    /**
     * Count the depth of the input graph
     */
    private void countLayers() {
        List<Task> scheduledTasks = new ArrayList<>();
        totalLayers = 0;
        while(scheduledTasks.size() != taskModel.getTasks().size()){
            List<Task> layeredTasks = layerTasks(scheduledTasks, taskModel.getTasks());
            totalLayers++;
            scheduledTasks.addAll(layeredTasks);
        }
    }

    /**
     * Takes list of currently placed tasks and all tasks to find out
     * the next free list of nodes that should be place in the next depth
     * of the input graph
     * @param schedule
     * @param allTasks
     * @return List<Task>
     */
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

