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
import scheduleModel.ISchedule;
import view.listeners.AlgorithmListener;

public class TimerTile implements AlgorithmListener {

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
        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(1), event -> {
            if (!_finished) {
                double currentTime  = System.currentTimeMillis();
                double timeElapsed = (currentTime - starTime)/1000;
                _time.setText( timeElapsed +"s");
                _time.setTextAlignment(TextAlignment.RIGHT);
            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    // use this somewhere to end the time
    public void setTimeFinished(){
        _finished = true;
    }


    @Override
    public void bestScheduleUpdated(ISchedule schedule) {
        // do nothing
    }

    @Override
    public void algorithmFinished() {
        System.out.println("event fired");
        _finished = true;
    }

    @Override
    public void numberOfBranchesChanged(int numBranches) {
        // do nothing
    }
}
