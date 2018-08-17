package view.nodeTree;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import taskModel.Task;

public class Node {

    // Fields
    private Circle circle;
    private Text taskName;
    private Text taskWeight;
    private StackPane _stack;

    public Node(Task task, NodeColor color, int numofTasks) {
        // Create new StackPane
        _stack = new StackPane();

        // Elements needed for a node
        circle = createCircle(numofTasks);
        taskName = new Text(task.getName());
        taskName.setFont(new Font(25));
        taskWeight = new Text(Integer.toString(task.getWeight()));
        taskWeight.setFont(new Font(10));

        _stack.getChildren().add(circle);              // Index 0 of _stack
        _stack.getChildren().add(taskName);            // Index 1 of _stack
        _stack.getChildren().add(taskWeight);          // Index 2 of _stack

        // Move taskName up a little
        _stack.getChildren().get(1).setTranslateY(-6);
        // Move taskWeight down a little
        _stack.getChildren().get(2).setTranslateY(12);

        setColor(color);
    }

    private void setColor(NodeColor color){
        switch(color){
            case RED:
                circle.setFill(Color.RED);
                taskName.setFill(Color.WHITE);
                taskWeight.setFill(Color.WHITE);
                break;
            case ORANGE:
                circle.setFill(Color.ORANGE);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
            case YELLOW:
                circle.setFill(Color.YELLOW);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
            case GREEN:
                circle.setFill(Color.GREEN);
                taskName.setFill(Color.WHITE);
                taskWeight.setFill(Color.WHITE);
                break;
            case BLUE:
                circle.setFill(Color.BLUE);
                taskName.setFill(Color.WHITE);
                taskWeight.setFill(Color.WHITE);
                break;
            case INDIGO:
                circle.setFill(Color.INDIGO);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
            case VIOLET:
                circle.setFill(Color.VIOLET);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
            case CYAN:
                circle.setFill(Color.CYAN);
                taskName.setFill(Color.WHITE);
                taskWeight.setFill(Color.WHITE);
                break;
            case MAGENTA:
                circle.setFill(Color.MAGENTA);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
            default:
            case WHITE:
                circle.setFill(Color.WHITE);
                taskName.setFill(Color.BLACK);
                taskWeight.setFill(Color.BLACK);
                break;
        }
    }

    private Circle createCircle (int numOfTasks){
        // Scale the size of circle depending on number of tasks/nodes
        int scaledCircle = (20 + 20 / numOfTasks);
        return new Circle(0,0, scaledCircle);
    }

    public StackPane getStackPane(){
        return _stack;
    }
}
