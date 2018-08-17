package view;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * The tile which shows memory usage.
 */
public class MemoryGauge {

    private Tile memoryTile;
    private Boolean finished = false;

    MemoryGauge(Tile memoryTile){
        this.memoryTile = memoryTile;
        showMemoryUsage();
    }

    /**
     * Every 100ms it recalculates the current Java VM memory used
     * and updates it of the GUI
     */
    private void showMemoryUsage(){
        Timeline time = new Timeline();
        if(time!=null){
            time.stop();
        }

        // every 100ms execute the frame
        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // calculate the memory usage
                Runtime runtime = Runtime.getRuntime();
                double currentMemory = runtime.maxMemory() - runtime.freeMemory();
                double toMegabytes = 1000000;
                memoryTile.setValue(currentMemory/toMegabytes);
                if(finished) {
                    finished = false;
                    time.stop();
                }
            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    public void stopMemoryUsageDisplay(){
        finished = true;
    }

}
