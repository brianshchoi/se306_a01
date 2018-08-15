package view;

import app.CLI;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;
import scheduleModel.ISchedule;
import taskModel.TaskModel;
import view.ganttChart.GanttChartScheduler;
import view.listeners.AlgorithmListener;
import view.nodeTree.NodeTreeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Visualizer extends Application implements AlgorithmListener {
    private static final double firstLayerHeight = 325;
    private static final double secondLayerHeight = 325;

    // Tiles
    private static ISchedule schedule;
    private static TaskModel taskModel;
    private Tile scheduler_tile;
    private Tile nodeTree_tile;
    private Tile time_tile;
    private Tile memory_tile;
    private Tile branches_tile;
    private TimerTile timerTile;
    private FlowGridPane wholePane, topRowPane, bottomRowPane;


    @Override public void init(){
        List<AlgorithmListener> listeners = new ArrayList<>();
        NodeTreeGenerator nodeTreeGenerator = new NodeTreeGenerator(taskModel, 500, firstLayerHeight);
        timerTile = new TimerTile();

        nodeTree_tile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Node Tree")
                .graphic(nodeTreeGenerator.getGraphicPane())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        time_tile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(400, secondLayerHeight)
                .title("Time Taken")
                .textSize(Tile.TextSize.BIGGER)
                .graphic(timerTile.makeTimer())
                /*.description("0.000s")
                .descriptionAlignment(Pos.TOP_RIGHT)*/
                .build();
        listeners.add(timerTile);
        Runtime runtime = Runtime.getRuntime();
        memory_tile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(400,secondLayerHeight)
                .title("Memory")
                .maxValue(runtime.maxMemory()/1000000)
                .unit("MB")
                .textSize(Tile.TextSize.BIGGER)
                .build();

        MemoryGauge memoryGauge = new MemoryGauge(memory_tile);

        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);
        listeners.add(this);

        scheduler_tile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Currently Known Best Schedule")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        branches_tile = TileBuilder.create()
                .prefSize(400, secondLayerHeight)
                .skinType(Tile.SkinType.BAR_CHART)
                .title("Branches Explored")
                .running(true)
                .build();

        topRowPane = new FlowGridPane(2, 1, scheduler_tile, nodeTree_tile);
        topRowPane.setVgap(5);
        topRowPane.setHgap(5);

        bottomRowPane = new FlowGridPane(3, 1, time_tile, branches_tile, memory_tile);
        bottomRowPane.setVgap(5);
        bottomRowPane.setHgap(5);
        wholePane = new FlowGridPane(1,2,
                topRowPane, bottomRowPane);

        // Give listeners to CLI
        new Thread(() -> CLI.visualizerReady(listeners)).start();
    }

    @Override
    public void start(Stage primaryStage) {

        wholePane.setHgap(5);
        wholePane.setVgap(5);
        wholePane.setPadding(new Insets(5));
        wholePane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(wholePane);

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

    private void remakeChart(ISchedule schedule){
        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);

        scheduler_tile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Parallel Scheduler")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

    }

    @Override
    public void bestScheduleUpdated(ISchedule schedule) {
        Platform.runLater(() -> {
            schedule.debug();
            topRowPane.getChildren().remove(scheduler_tile);
            remakeChart(schedule);
            topRowPane.getChildren().add(0, scheduler_tile);
        });
    }

    @Override
    public void algorithmFinished() {

    }
}
