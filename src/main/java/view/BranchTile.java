package view;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import scheduleModel.ISchedule;
import view.listeners.AlgorithmListener;

public class BranchTile implements AlgorithmListener {

    private int numBranches;

    /**
     * Every 100ms updates the number of branches explored on the GUI
     * @param branchTile
     */
    BranchTile(Tile branchTile){
        Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        // Every 100 ms execute the frame
        KeyFrame frame = new KeyFrame(Duration.millis(100), event -> {
            branchTile.setValue(numBranches);
        });
        time.getKeyFrames().add(frame);
        time.playFromStart();
    }

    @Override
    public void bestScheduleUpdated(ISchedule schedule) {
        //do nothing
    }

    @Override
    public void algorithmFinished() {
        // do nothing
    }

    /**
     * Whenever it is notified of new branch explored, increment
     * numBranches
     */
    @Override
    public void numberOfBranchesChanged() {
        numBranches++;
    }
}
