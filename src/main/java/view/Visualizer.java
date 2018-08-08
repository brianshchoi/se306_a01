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
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.util.Locale;

public class Visualizer extends Application {
    private static final double TILE_SIZE = 150;

    // Tiles
    private Tile clock_tile;
    private Tile colorRegulator;
    private Tile sparkLineTile;


    @Override public void init(){

        clock_tile = TileBuilder.create().prefSize(TILE_SIZE, TILE_SIZE)
                .skinType(Tile.SkinType.CLOCK)
                .title("Clock Tile")
                .text("Whatever text")
                .dateVisible(true)
                .locale(Locale.US)
                .running(true)
                .build();

        sparkLineTile = TileBuilder.create()
                .prefSize(TILE_SIZE, TILE_SIZE)
                .skinType(Tile.SkinType.SPARK_LINE)
                .title("SparkLine Tile")
                .unit("mb")
                .gradientStops(new Stop(0, Tile.GREEN),
                        new Stop(0.5, Tile.YELLOW),
                        new Stop(1.0, Tile.RED))
                .strokeWithGradient(true)
                .build();
    }

    @Override
    public void start(Stage primaryStage) {
        FlowGridPane pane = new FlowGridPane(5,5,
                clock_tile,sparkLineTile);
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
