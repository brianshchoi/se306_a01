package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class TimerTile {

    private Label _time;
    private Boolean _finished = false;

    // add more fields if you want to show more information like how many nodes, processors, etc
    TimerTile() {
        makeTimer();
    }

    public Node makeTimer() {

        _time = new Label();
        _time.setTextFill(Color.WHITE);
        _time.setFont(Font.font(28));
        _time.setTextAlignment(TextAlignment.RIGHT);
        _time.setText("0.000s");

        // comment this out when integrating with the actual algorithm
        doTime(System.currentTimeMillis());

        VBox box = new VBox(2);
        box.getChildren().add(_time);

        return box;
    }

    public void doTime(double starTime) {
        Timeline time = new Timeline();
        if(time!=null){
            time.stop();
        }

        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double currentTime  = System.currentTimeMillis();
                double timeElapsed = (currentTime - starTime)/1000;
                _time.setText( timeElapsed +"s");
                _time.setTextAlignment(TextAlignment.RIGHT);

                // Check if algorithm has finished
                if(_finished) {
                    _finished = false;
                    time.stop();
                }
            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    // use this somewhere to end the time
    public void setTimeFinished(){
        _finished = true;
    }


}
