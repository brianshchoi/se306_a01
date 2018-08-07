package view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class VisualizerController {

    @FXML
    private Text text;

    @FXML
    private void initialize() {
        // Do data setup here
    }

    @FXML
    private void buttonHandler() {
        text.setText("You clicked button");
    }
}
