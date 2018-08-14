package view;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
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
    private static final double firstLayerHeight = 300;
    private static final double secondLayerHeight = 300;

    // Tiles
    private static ISchedule schedule;
    private static TaskModel taskModel;
    private Tile scheduler_tile;
    private Tile nodeTree_tile;
    private Tile time_tile;
    private Tile memory_tile;
    private TimerTile _timeTile;



    @Override public void init(){

        NodeTreeGenerator nodeTreeGenerator = new NodeTreeGenerator(taskModel, 500, firstLayerHeight);
        _timeTile = new TimerTile();

        nodeTree_tile = TileBuilder.create()
                .prefSize(500, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Node Tree")
                .graphic(nodeTreeGenerator.getGraphicPane())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);

        scheduler_tile = TileBuilder.create()
                .prefSize(800, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Parallel Scheduler")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        time_tile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(800, secondLayerHeight)
                .title("Time Taken")
                .textSize(Tile.TextSize.BIGGER)
                .graphic(_timeTile.makeTimer())
                /*.description("0.000s")
                .descriptionAlignment(Pos.TOP_RIGHT)*/
                .build();

        Runtime runtime = Runtime.getRuntime();
        memory_tile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(500,secondLayerHeight)
                .title("Memory")
                .maxValue(runtime.maxMemory()/1000000)
                .unit("MB")
                .textSize(Tile.TextSize.BIGGER)
                .build();

        MemoryGauge memoryGauge = new MemoryGauge(memory_tile);

    }

    @Override
    public void start(Stage primaryStage) {
        FlowGridPane pane = new FlowGridPane(2,2,
               scheduler_tile, nodeTree_tile, time_tile, memory_tile);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

        primaryStage.setTitle("Optimal Scheduler GUI");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launch(TaskModel tm, ISchedule sch) {
        taskModel = tm;
        schedule = sch;

        launch();
    }

}
