package sketchy.main;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PaneOrganizer {
    private BorderPane root;

    public PaneOrganizer() {
        this.root = new BorderPane();

        Pane canvasPane = new Pane();
        this.root.setCenter(canvasPane);
        VBox controlPane = new VBox();
        this.root.setLeft(controlPane);

        new Canvas(canvasPane, controlPane);
    }

    public BorderPane getRoot() {
        return this.root;
    }
}
