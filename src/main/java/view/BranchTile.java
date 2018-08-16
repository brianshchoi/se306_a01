package view;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import scheduleModel.ISchedule;
import view.listeners.AlgorithmListener;

public class BranchTile implements AlgorithmListener {

    private int numBranches;

    BranchTile(Tile branchTile){
        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(100), event -> {
            branchTile.setValue(numBranches);
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    @Override
    public void bestScheduleUpdated(ISchedule schedule) {

    }

    @Override
    public void algorithmFinished() {

    }

    @Override
    public void numberOfBranchesChanged() {
        numBranches++;
    }
}
