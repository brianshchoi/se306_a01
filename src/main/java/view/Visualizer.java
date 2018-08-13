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

import java.util.List;
import java.util.Locale;

public class Visualizer extends Application {
    private static final double TILE_SIZE = 300;

    // Tiles
    private Tile scheduler_tile;
    private Tile nodeTree_tile;
    private Tile branchPercentage_tile;
    private GanntChartScheduler _ganttChart;


    @Override public void init(){

        _ganttChart = new GanntChartScheduler(mockSchedule());

        scheduler_tile = TileBuilder.create().prefSize(TILE_SIZE, TILE_SIZE)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Parallel Scheduler")
                .text("Whatever text")
                .graphic(_ganttChart.get_chart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        NodeTreeGenerator nodeTreeGenerator = new NodeTreeGenerator(mockModel());

        nodeTree_tile = TileBuilder.create()
                .prefSize(1000, 1000)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Node Tree")
                .graphic(
                        nodeTreeGenerator.getGraphicPane()
                )
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
        FlowGridPane pane = new FlowGridPane(3,2,
                scheduler_tile, nodeTree_tile, branchPercentage_tile);
        pane.setHgap(5);
        pane.setVgap(5);
        pane.setPadding(new Insets(5));
        pane.setBackground(new Background(new BackgroundFill(Tile.BACKGROUND.darker(), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);

        primaryStage.setTitle("Optimal Scheduler GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private TaskModel mockModel() {
        TaskModel model = new TaskModel("Test");

        Task tZero = new Task("0", 4);
        Task tOne = new Task("1", 2);
        Task tTwo = new Task("2", 2);
        Task tThree = new Task("3", 2);
        Task tFour = new Task("4", 5);
        Task tFive = new Task("5", 5);
        Task tSix = new Task("6", 10);

        model.addTask(tZero);
        model.addTask(tOne);
        model.addTask(tTwo);
        model.addTask(tThree);
        model.addTask(tFour);
        model.addTask(tFive);
        model.addTask(tSix);

        model.addDependency(tZero, tOne, 1);
        model.addDependency(tZero, tTwo, 1);
        model.addDependency(tZero, tThree, 3);
        model.addDependency(tOne, tFour, 1);
        model.addDependency(tTwo, tFour, 2);
        model.addDependency(tThree, tFive, 3);
        model.addDependency(tFive, tSix, 5);
        model.addDependency(tFour, tSix, 4);
        return model;
    }

    private ISchedule mockSchedule() {
        Task tZero = new Task("0", 4);
        Task tOne = new Task("1", 2);
        Task tTwo = new Task("2", 2);
        Task tThree = new Task("3", 2);
        Task tFour = new Task("4", 5);
        Task tFive = new Task("5", 5);
        Task tSix = new Task("6", 10);

        ISchedule s = new Schedule(2);

        // Needed here to get processor object
        List<IProcessor> processors = s.getProcessors();
        IProcessor p1 = processors.get(0);
        IProcessor p2 = processors.get(1);

        s.schedule(tZero, p1, 0);
        s.schedule(tOne, p1, 4);
        s.schedule(tTwo, p2, 5);
        s.schedule(tThree, p1, 6);
        s.schedule(tFour, p2, 7);
        s.schedule(tFive, p1, 8);
        s.schedule(tSix, p1, 16);

        return s;
    }
}
