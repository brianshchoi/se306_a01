package view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Node {
    private Rectangle _rectangle;
    private Text _text;
    private StackPane _stack;
    private String _color;

    public Node(String taskName, int weight, String color){
        _rectangle = new Rectangle(100, 10, 80, 80);
        _text = new Text(taskName + "\n\n" + weight);

        _stack = new StackPane();
        _stack.getChildren().addAll(_rectangle, _text);
        setColor(color);
    }

    // TODO: Change switch statement to enum, and add more colours
    private void setColor(String color){
        switch(color){
            case "black":
                _rectangle.setFill(Color.BLACK);
                _text.setFill(Color.WHITE);
                break;
            case "white":
                _rectangle.setFill(Color.WHITE);
                _text.setFill(Color.BLACK);
                break;
            case "blue":
                _rectangle.setFill(Color.BLUE);
                _text.setFill(Color.WHITE);
                break;
            case "":
                _rectangle.setFill(Color.GOLD);
                _text.setFill(Color.BLACK);
                break;
            case "cyan":
                _rectangle.setFill(Color.CYAN);
                _text.setFill(Color.BLACK);
                break;
        }
    }

    public StackPane getStackPane(){
        return _stack;
    }
}
