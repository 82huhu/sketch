package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sketchy.shapes.Saveable;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

/**
 * DrawShapeCommand is a command which draws a shape on the screen.
 */
public class DrawShapeCommand implements Commands {
    private Selectable selectedShapeEnum;
    private ColorPicker colorPicker;
    private ArrayList<Selectable> shapes;
    private ArrayList<Saveable> saveables;
    private Pane canvasPane;
    private MouseEvent event;

    public DrawShapeCommand(Selectable selectedShapeEnum, ColorPicker colorPicker,
                            ArrayList<Selectable> shapes, ArrayList<Saveable> saveables,
                            Pane canvasPane, MouseEvent event) {
        this.selectedShapeEnum = selectedShapeEnum;
        this.colorPicker = colorPicker;
        this.shapes = shapes;
        this.canvasPane = canvasPane;
        this.event = event;
        this.saveables = saveables;
    }

    /**
     * undo() removes the shape from the pane and shapes and saveables arraylists.
     */
    @Override
    public void undo() {
        this.shapes.remove(this.shapes.size() - 1);
        this.saveables.remove(this.saveables.size() - 1);
        this.canvasPane.getChildren().remove(this.selectedShapeEnum.getShape());
    }

    /**
     * redo() adds the shape to the pane and shapes and saveables arraylists.
     */
    @Override
    public void redo() {
        this.shapes.add(this.selectedShapeEnum);
        this.saveables.add(this.selectedShapeEnum);
        this.canvasPane.getChildren().add(this.selectedShapeEnum.getShape());
    }

    /**
     * execute() performs the action of drawing a shape, determined by the selectedShapeEnum.
     */
    @Override
    public void execute() {
        Selectable shape = this.selectedShapeEnum;
        shape.changeColor(this.colorPicker.getValue());

        this.shapes.add(shape);
        this.saveables.add(shape);

        shape.setLocation(this.event.getX(), this.event.getY());
        shape.draw(this.canvasPane);
    }
}
