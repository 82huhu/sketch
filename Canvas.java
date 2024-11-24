package sketchy.main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ColorPicker;

public class Canvas {
    private Pane canvasPane;

    public Canvas(Pane canvasPane, VBox controlPane) {
        this.canvasPane = canvasPane;
        this.setUpRadioButtons(controlPane);
        this.setUpControlPane(controlPane);
        this.setColorPicker(controlPane);
        this.setUpButtons(controlPane);
    }

    private void setUpControlPane(VBox controlPane) {
        controlPane.setStyle("-fx-background-color: orange;");
        controlPane.setPrefWidth(Constants.CONTROL_WIDTH);
        controlPane.setAlignment(Pos.CENTER);
        controlPane.setSpacing(Constants.CONTROL_SPACING);
    }

    private void setUpRadioButtons(VBox controlPane) {
        Label drawingOptionsLabel = new Label("Drawing Options");
        controlPane.getChildren().add(drawingOptionsLabel);

        RadioButton selectShape = new RadioButton("select shape");
        selectShape.setSelected(false);
        controlPane.getChildren().add(selectShape);

        RadioButton drawWithPen = new RadioButton("draw with pen");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawWithPen);

        RadioButton drawRect = new RadioButton("draw rectangle");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawRect);

        RadioButton drawEllipse = new RadioButton("draw ellipse");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawEllipse);
    }

    private void setColorPicker(VBox controlPane) {
        Label setColorLabel= new Label("Set Color");
        controlPane.getChildren().add(setColorLabel);

        ColorPicker colorPicker = new ColorPicker();
        controlPane.getChildren().add(colorPicker);
    }

    private void setUpButtons(VBox controlPane) {
        Label ShapeOptionsLabel = new Label("Shape Options");
        controlPane.getChildren().add(ShapeOptionsLabel);

        Button fill = new Button("fill");
        controlPane.getChildren().add(fill);

        Button delete = new Button("delete");
        controlPane.getChildren().add(delete);

        Button raise = new Button("raise");
        controlPane.getChildren().add(raise);

        Button lower = new Button("lower");
        controlPane.getChildren().add(lower);

        Label operationsLabel = new Label("Operations");
        controlPane.getChildren().add(operationsLabel);

        Button undo = new Button("undo");
        controlPane.getChildren().add(undo);

        Button redo = new Button("redo");
        controlPane.getChildren().add(redo);

        Button save = new Button("save");
        controlPane.getChildren().add(save);

        Button load = new Button("load");
        controlPane.getChildren().add(load);

    }

    private void selectShape() {

    }






}
