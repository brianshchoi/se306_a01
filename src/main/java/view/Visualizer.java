package view;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import scheduleModel.Schedule;
import taskModel.Task;
import taskModel.TaskModel;
import view.ganttChart.GanttChartScheduler;
import view.nodeTree.NodeTreeGenerator;

import java.util.List;
import java.util.Locale;

public class Visualizer extends Application {

    // Tiles
    private static ISchedule schedule;
    private static TaskModel taskModel;
    private Tile scheduler_tile;
    private Tile nodeTree_tile;
    private Tile branchPercentage_tile;


    @Override public void init(){

        NodeTreeGenerator nodeTreeGenerator = new NodeTreeGenerator(taskModel, 1000, 1000);

        nodeTree_tile = TileBuilder.create()
                .prefSize(1000, 1000)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Node Tree")
                .graphic(nodeTreeGenerator.getGraphicPane())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);

        scheduler_tile = TileBuilder.create().prefSize(1000, 600)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Parallel Scheduler")
                .text("Whatever text")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        branchPercentage_tile = TileBuilder.create()
                .prefSize(200, 400)
                .title("Branch Percentage")
                .unit("%")
                .build();
    }

    @Override
    public void start(Stage primaryStage) {
        FlowGridPane pane = new FlowGridPane(3,1,
                nodeTree_tile, scheduler_tile,  branchPercentage_tile);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

        primaryStage.setTitle("Optimal Scheduler GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launch(TaskModel tm, ISchedule sch) {
        taskModel = tm;
        schedule = sch;

        launch();
    }

}
