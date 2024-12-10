package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

public class CreateShape implements Commands {
    private Selectable selectedShapeEnum;
    private ColorPicker colorPicker;
    private ArrayList<Selectable> shapes;
    private Pane canvasPane;
    private boolean addToPane;
    private MouseEvent event;

    public CreateShape(Selectable selectedShapeEnum, ColorPicker colorPicker,
                       ArrayList<Selectable> shapes, Pane canvasPane, MouseEvent event) {
        this.selectedShapeEnum = selectedShapeEnum;
        this.colorPicker = colorPicker;
        this.shapes = shapes;
        this.canvasPane = canvasPane;
        this.addToPane = true;
        this.event = event;
    }

    @Override
    public void undo() {
        this.addToPane = false;
        this.shapes.remove(this.shapes.size() - 1);
        this.canvasPane.getChildren().remove(this.selectedShapeEnum.getShape());
    }

    @Override
    public void redo() {
        this.execute();
    }


    @Override
    public void execute() {
        Selectable shape = this.selectedShapeEnum;
        shape.setFill(this.colorPicker.getValue());
        this.shapes.add(shape);
        shape.setLocation(this.event.getX(), this.event.getY());
        shape.draw(this.canvasPane);
    }
}
