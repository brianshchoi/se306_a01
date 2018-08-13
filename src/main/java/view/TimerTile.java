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
import javafx.util.Duration;

public class TimerTile {

    private StackPane _pane = new StackPane();
    private Label _time;

    private final Integer starttime=15;
    private Integer seconds= starttime;

    TimerTile() {

      //  makeTimer();
    }

    public Node makeTimer() {

        _time = new Label();
        _time.setTextFill(Color.WHITE);
        _time.setFont(Font.font(28));
        _time.setText("0.00s");

        doTIme();

        VBox box = new VBox(2);
        box.getChildren().add(_time);

        return box;
    }

    private void doTIme() {

        Timeline time = new Timeline();

        if(time!=null){
            time.stop();
        }

        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                seconds--;
                _time.setText( seconds.toString() +"s");
                if(seconds <= 0) {
                    time.stop();
                }

            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();

    }


}
