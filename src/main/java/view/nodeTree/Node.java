package view.nodeTree;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import taskModel.Task;

public class Node {

    private Circle _circle;
    private Text _text;
    private StackPane _stack;

    public Node(Task task, String color){
        _circle = new Circle(0, 0, 40);
        _text = new Text(task.getName() + "\n\n" + task.getWeight());

        _stack = new StackPane();
        _stack.getChildren().addAll(_circle, _text);
        setColor(color);
    }

    // TODO: Change switch statement to enum, and add more colours
    private void setColor(String color){
        switch(color){
            case "black":
                _circle.setFill(Color.BLACK);
                _text.setFill(Color.WHITE);
                break;
            case "blue":
                _circle.setFill(Color.BLUE);
                _text.setFill(Color.WHITE);
                break;
            case "":
                _circle.setFill(Color.GOLD);
                _text.setFill(Color.BLACK);
                break;
            case "cyan":
                _circle.setFill(Color.CYAN);
                _text.setFill(Color.BLACK);
                break;
            default:
            case "white":
                _circle.setFill(Color.WHITE);
                _text.setFill(Color.BLACK);
                break;
        }
    }

    public StackPane getStackPane(){
        return _stack;
    }
}
