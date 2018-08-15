package view;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.chart.ChartData;
import scheduleModel.ISchedule;
import view.listeners.AlgorithmListener;

public class BranchTile implements AlgorithmListener {

    private Tile _branchTile;
    private ChartData _smoothChartData;

    BranchTile(Tile branchTile){
        this._branchTile = branchTile;
    }

    @Override
    public void bestScheduleUpdated(ISchedule schedule) {

    }

    @Override
    public void algorithmFinished() {

    }

    @Override
    public void numberOfBranchesChanged(int numBranches) {
        _smoothChartData = new ChartData("Branches" ,numBranches, Tile.BLUE);
        _branchTile.addChartData(_smoothChartData);

    }
}
