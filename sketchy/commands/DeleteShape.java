package sketchy.commands;

import javafx.scene.layout.Pane;
import sketchy.shapes.Saveable;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

public class DeleteShape implements Commands {
    private Selectable selectedShape;
    private ArrayList<Selectable> shapes;
    private ArrayList<Saveable> saveables;
    private Pane canvasPane;

    public DeleteShape(Selectable selectedShape, ArrayList<Selectable> shapes, ArrayList<Saveable> saveables, Pane canvasPane) {
        this.selectedShape = selectedShape;
        this.shapes = shapes;
        this.saveables = saveables;
        this.canvasPane = canvasPane;
    }

    @Override
    public void undo() {
        this.shapes.add(this.selectedShape);
        this.saveables.add(this.selectedShape);
        this.canvasPane.getChildren().add(this.selectedShape.getShape());
    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void execute() {
        if(this.selectedShape != null) {
            this.shapes.remove(this.selectedShape);
            this.saveables.remove(this.selectedShape);
            this.selectedShape.delete(this.canvasPane);
        }
    }
}
