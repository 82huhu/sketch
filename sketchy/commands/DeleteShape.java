package sketchy.commands;

import javafx.scene.layout.Pane;
import sketchy.shapes.Saveable;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

/**
 * DeleteShape is a command which erases a shape off the screen
 */
public class DeleteShape implements Commands {
    private Selectable selectedShape;
    private ArrayList<Selectable> shapes;
    private ArrayList<Saveable> saveables;
    private Pane canvasPane;

    public DeleteShape(Selectable selectedShape, ArrayList<Selectable> shapes,
                       ArrayList<Saveable> saveables, Pane canvasPane) {
        this.selectedShape = selectedShape;
        this.shapes = shapes;
        this.saveables = saveables;
        this.canvasPane = canvasPane;
    }

    /**
     * undo() adds the shape back to the pane and the shape and saveables arraylists.
     */
    @Override
    public void undo() {
        this.shapes.add(this.selectedShape);
        this.saveables.add(this.selectedShape);
        this.canvasPane.getChildren().add(this.selectedShape.getShape());
    }

    /**
     * redo() calls execute() again to remove the shape from the arraylists and pane
     */
    @Override
    public void redo() {
        this.execute();
    }

    /**
     * execute() removes the shape from the arraylists and the pane
     */
    @Override
    public void execute() {
        this.shapes.remove(this.selectedShape);
        this.saveables.remove(this.selectedShape);
        this.selectedShape.delete(this.canvasPane);
    }
}
