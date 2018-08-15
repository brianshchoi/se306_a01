package view.nodeTree;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import taskModel.Task;

public class Node {

    private Circle _circle;
    private Text _taskName;
    private Text _taskWeight;
    private StackPane _stack;

    public Node(Task task, NodeColor color, int numofTasks) {
        // Create new StackPane
        _stack = new StackPane();

        // Elements needed for a node
        _circle = createCircle(numofTasks);
        _taskName = new Text(task.getName());
        _taskName.setFont(new Font(25));
        _taskWeight = new Text(Integer.toString(task.getWeight()));
        _taskWeight.setFont(new Font(10));

        _stack.getChildren().add(_circle);              // Index 0 of _stack
        _stack.getChildren().add(_taskName);            // Index 1 of _stack
        _stack.getChildren().add(_taskWeight);          // Index 2 of _stack

        // Move taskName up a little
        _stack.getChildren().get(1).setTranslateY(-6);
        // Move taskWeight down a little
        _stack.getChildren().get(2).setTranslateY(12);

        setColor(color);
    }

    private void setColor(NodeColor color){
        switch(color){
            case RED:
                _circle.setFill(Color.RED);
                _taskName.setFill(Color.WHITE);
                _taskWeight.setFill(Color.WHITE);
                break;
            case ORANGE:
                _circle.setFill(Color.ORANGE);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
                break;
            case YELLOW:
                _circle.setFill(Color.YELLOW);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
                break;
            case GREEN:
                _circle.setFill(Color.GREEN);
                _taskName.setFill(Color.WHITE);
                _taskWeight.setFill(Color.WHITE);
                break;
            case BLUE:
                _circle.setFill(Color.BLUE);
                _taskName.setFill(Color.WHITE);
                _taskWeight.setFill(Color.WHITE);
                break;
            case INDIGO:
                _circle.setFill(Color.INDIGO);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
                break;
            case VIOLET:
                _circle.setFill(Color.VIOLET);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
                break;
            case CYAN:
                _circle.setFill(Color.CYAN);
                _taskName.setFill(Color.WHITE);
                _taskWeight.setFill(Color.WHITE);
                break;
            case MAGENTA:
                _circle.setFill(Color.MAGENTA);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
                break;
            default:
            case WHITE:
                _circle.setFill(Color.WHITE);
                _taskName.setFill(Color.BLACK);
                _taskWeight.setFill(Color.BLACK);
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

    public static void main(String[] args){
        System.out.println(Color.ORANGE.toString());
        System.out.println(Color.YELLOW.toString());
        System.out.println(Color.VIOLET.toString());
        System.out.println(Color.CYAN.toString());
        System.out.println(Color.GREEN.toString());
    }
}
