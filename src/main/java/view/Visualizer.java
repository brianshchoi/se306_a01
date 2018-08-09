package view;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.util.Locale;

public class Visualizer extends Application {
    private static final double TILE_SIZE = 300;

    // Tiles
    private Tile scheduler_tile;
    private Tile nodeTree_tile;
    private Tile branchPercentage_tile;
    private GanttChartSample _ganttChart;


    @Override public void init(){

        _ganttChart = new GanttChartSample();

        scheduler_tile = TileBuilder.create().prefSize(TILE_SIZE, TILE_SIZE)
                .skinType(Tile.SkinType.CUSTOM)
                .title("Parallel Scheduler")
                .text("Whatever text")
                .graphic(_ganttChart.get_chart())
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        nodeTree_tile = TileBuilder.create()
                .prefSize(500, 600)
                .skinType(Tile.SkinType.SPARK_LINE)
                .title("Node Tree")
                .unit("mb")
                .gradientStops(new Stop(0, Tile.GREEN),
                        new Stop(0.5, Tile.YELLOW),
                        new Stop(1.0, Tile.RED))
                .strokeWithGradient(true)
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
}
