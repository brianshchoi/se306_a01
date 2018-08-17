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
    // General GUI size
    private static final double firstLayerHeight = 325;
    private static final double secondLayerHeight = 325;

    // Constructor fields
    private static ISchedule schedule;
    private static TaskModel taskModel;

    // Tiles
    private Tile schedulerTile;
    private Tile nodeTreeTile;
    private Tile timeTile;
    private Tile memoryTile;
    private Tile branchesTile;

    // GUI frame
    private FlowGridPane wholePane, topRowPane, bottomRowPane;
    private Stage primaryStage;
    private Scene scene;

    // Other Fields
    private TimerTile timerTileMaker;
    private boolean zoomActive;

    @Override public void init(){
        List<AlgorithmListener> listeners = new ArrayList<>();
        NodeTreeGenerator nodeTreeGenerator = new NodeTreeGenerator(taskModel, 500, firstLayerHeight);
        timerTileMaker = new TimerTile();

        // Creates Node Tile with input graph rendered
        nodeTreeTile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Node Tree")
                .graphic(nodeTreeGenerator.getGraphicPane())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        // Creates time the measures running time
        timeTile = TileBuilder.create()
                .skinType(Tile.SkinType.CUSTOM)
                .prefSize(400, secondLayerHeight)
                .title("Time Elapsed")
                .textSize(Tile.TextSize.BIGGER)
                .graphic(timerTileMaker.makeTimer())
                /*.description("0.000s")
                .descriptionAlignment(Pos.TOP_RIGHT)*/
                .build();
        listeners.add(timerTileMaker);

        // Creates Memory tile
        Runtime runtime = Runtime.getRuntime();
        memoryTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(400,secondLayerHeight)
                .title("Memory")
                .maxValue(runtime.maxMemory()/1000000)
                .unit("MB")
                .textSize(Tile.TextSize.BIGGER)
                .build();

        MemoryGauge memoryGauge = new MemoryGauge(memoryTile);

        // Making task schedule chart
        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);
        listeners.add(this);
        schedulerTile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Current Best Known Schedule")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        // make branch exploration tile
        branchesTile = TileBuilder.create()
                .skinType(Tile.SkinType.NUMBER)
                .prefSize(400, secondLayerHeight)
                .title("Branches Explored")
                .value(0)
                .maxValue(Double.MAX_VALUE)
                .textVisible(true)
                .build();


        BranchTile branchTile = new BranchTile(branchesTile);
        listeners.add(branchTile);

        // Positioning top row of the GUI
        topRowPane = new FlowGridPane(2, 1, schedulerTile, nodeTreeTile);
        topRowPane.setVgap(5);
        topRowPane.setHgap(5);

        // Positioning bottom row of the GUI
        bottomRowPane = new FlowGridPane(3, 1, timeTile, branchesTile, memoryTile);
        bottomRowPane.setVgap(5);
        bottomRowPane.setHgap(5);
        wholePane = new FlowGridPane(1,2,
                topRowPane, bottomRowPane);

        // Give listeners to CLI
        new Thread(() -> CLI.visualizerReady(listeners)).start();
    }

    /**
     * After init() is done the GUI is launched for users to see
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Setting up make pane setting
        wholePane.setHgap(5);
        wholePane.setVgap(5);
        wholePane.setPadding(new Insets(5));
        wholePane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(wholePane);

        this.primaryStage.setTitle("Optimal Scheduler GUI");
        this.primaryStage.setResizable(false);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public static void launch(TaskModel tm, ISchedule sch) {
        taskModel = tm;
        schedule = sch;

        launch();
    }

    /**
     * Update the current best known schedule chart
     * @param schedule
     */
    private void remakeChart(ISchedule schedule) {
        GanttChartScheduler ganttChart = new GanttChartScheduler(schedule);

        // Remakes the task schedule tile
        schedulerTile = TileBuilder.create()
                .prefSize(600, firstLayerHeight)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Current Best Known Schedule")
                .graphic(ganttChart.getChart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        // Zooms When clicked
        schedulerTile.setOnMouseClicked(e -> {
                    zoomActive = true;
                    remakeChart(schedule);
                    doZoom();
        });

    }

    @Override
    public void bestScheduleUpdated(ISchedule schedule) {
        Visualizer.schedule = schedule;
        Platform.runLater(() -> {
            // remove the old chart
            topRowPane.getChildren().remove(schedulerTile);
            // make a new one
            remakeChart(schedule);
            // add back the chart updated
            topRowPane.getChildren().add(0, schedulerTile);

            if (zoomActive) {
                doZoom();
            }
        });
    }

    private void doZoom() {
        // Setting up zoomed up GUI tile
        FlowGridPane schedulerFlowPane = new FlowGridPane(1, 1, schedulerTile);
        schedulerTile.setPrefSize(1200, 650);

        // When re clicked remake GUI into initial format
        schedulerTile.setOnMouseClicked(e2 -> {
            zoomActive = false;
            remakeChart(schedule);
            schedulerTile.setPrefSize(600, 325);
            topRowPane = new FlowGridPane(2, 1, schedulerTile, nodeTreeTile);
            topRowPane.setVgap(5);
            topRowPane.setHgap(5);

            bottomRowPane = new FlowGridPane(3, 1, timeTile, branchesTile, memoryTile);
            bottomRowPane.setVgap(5);
            bottomRowPane.setHgap(5);
            wholePane = new FlowGridPane(1,2,
                    topRowPane, bottomRowPane);


            wholePane.setHgap(5);
            wholePane.setVgap(5);
            wholePane.setPadding(new Insets(5));
            wholePane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

            scene = new Scene(wholePane);

            primaryStage.setTitle("Optimal Scheduler GUI");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();

        });
        Scene schedulerScene = new Scene(schedulerFlowPane);
        primaryStage.setScene(schedulerScene);
        primaryStage.show();
    }

    /**
     * Notify when optimal schedule has been found
     */
    @Override
    public void algorithmFinished() {
        timeTile.setTitle("Finished");
    }

    @Override
    public void numberOfBranchesChanged() {
        // do nothing
    }
}
