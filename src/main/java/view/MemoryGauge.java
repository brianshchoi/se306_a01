package view;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class MemoryGauge {

    private Tile _memoryTile;
    private Boolean _finished = false;

    MemoryGauge(Tile memoryTile){
        this._memoryTile = memoryTile;
        showMemoryUsage();
    }

    private void showMemoryUsage(){
        Timeline time = new Timeline();
        if(time!=null){
            time.stop();
        }

        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Runtime runtime = Runtime.getRuntime();
                double currentMemory = runtime.maxMemory() - runtime.freeMemory();
                double toMegabytes = 1000000;
                _memoryTile.setValue(currentMemory/toMegabytes);
                if(_finished) {
                    _finished = false;
                    time.stop();
                }
            }
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    public void stopMemoryUsageDisplay(){
        _finished = true;
    }

}
