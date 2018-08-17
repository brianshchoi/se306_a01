package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import scheduleModel.ISchedule;
import view.listeners.AlgorithmListener;

/**
 * The tile which shows the time elapsed
 */
public class TimerTile implements AlgorithmListener {

    private Label time;
    private Boolean finished = false;

    // add more fields if you want to show more information like how many nodes, processors, etc
    TimerTile() {
        makeTimer();
    }

    /**
     * Set up the custom tile
     * @return node
     */
    public Node makeTimer() {

        time = new Label();
        time.setTextFill(Color.WHITE);
        time.setFont(Font.font(28));
        time.setTextAlignment(TextAlignment.RIGHT);
        time.setText("0.000s");

        // comment this out when integrating with the actual algorithm
        doTime(System.currentTimeMillis());

        VBox box = new VBox(2);
        box.getChildren().add(time);

        return box;
    }

    public void doTime(double starTime) {
        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        // Update time every 1ms
        KeyFrame frame = new KeyFrame(Duration.millis(1), event -> {
            if (!finished) {
                // calculating time elapsed
                double currentTime  = System.currentTimeMillis();
                double timeElapsed = (currentTime - starTime)/1000;
                // update time on GUI
                this.time.setText( timeElapsed +"s");
                this.time.setTextAlignment(TextAlignment.RIGHT);
            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    // use this somewhere to end the time
    public void setTimeFinished(){
        finished = true;
    }


    @Override
    public void bestScheduleUpdated(ISchedule schedule) {
        // do nothing
    }

    @Override
    public void algorithmFinished() {
        finished = true;
    }

    @Override
    public void numberOfBranchesChanged() {
        // do nothing
    }
}
