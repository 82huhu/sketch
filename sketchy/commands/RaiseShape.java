package sketchy.commands;

import javafx.scene.layout.Pane;
import sketchy.shapes.Saveable;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

public class RaiseShape implements Commands {
    private ArrayList<Selectable> shapes;
    private ArrayList<Saveable> saveables;
    private Selectable selectedShape;
    private Pane canvasPane;
    private int currShapeIndex;
    private int currItemInPane;
    private int newIndex;
    private int newPaneIndex;

    public RaiseShape(ArrayList<Selectable> shapes, ArrayList<Saveable> saveables, Selectable selectedShape, Pane canvasPane) {
        this.shapes = shapes;
        this.saveables = saveables;
        this.selectedShape = selectedShape;
        this.canvasPane = canvasPane;
        this.currShapeIndex = this.shapes.indexOf(selectedShape);
        this.currItemInPane = this.currShapeIndex;
    }

    @Override
    public void undo() {
        this.shapes.remove(this.newIndex);
        this.saveables.remove(this.newIndex);
        this.shapes.add(this.currShapeIndex, this.selectedShape);
        this.saveables.add(this.currShapeIndex, this.selectedShape);
        this.canvasPane.getChildren().remove(this.newPaneIndex);
        this.canvasPane.getChildren().add(this.currItemInPane, this.selectedShape.getShape());
    }

    @Override
    public void redo() {
        this.shapes.remove(this.currShapeIndex);
        this.saveables.remove(this.currShapeIndex);
        this.shapes.add(this.newIndex, this.selectedShape);
        this.saveables.add(this.newIndex, this.selectedShape);
        this.canvasPane.getChildren().remove(this.currItemInPane);
        this.canvasPane.getChildren().add(this.newPaneIndex, this.selectedShape.getShape());
    }

    @Override
    public void execute() {
        this.currShapeIndex = this.shapes.indexOf(this.selectedShape);
        this.currItemInPane = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());

        if (this.currItemInPane + 1 < this.canvasPane.getChildren().size() ) {
            if (this.currShapeIndex + 1 < this.shapes.size()) {
                //move shape up in ArrayList logically
                int nextShapeInArray = this.currShapeIndex + 1;
                Selectable nextShape = this.shapes.get(nextShapeInArray);
                int nextShapeInPane = this.canvasPane.getChildren().indexOf(nextShape.getShape());
                if (nextShapeInPane - this.currItemInPane == 1) {
                    this.shapes.remove(this.currShapeIndex);
                    this.saveables.remove(this.currShapeIndex);
                    this.shapes.add(this.currShapeIndex + 1, this.selectedShape);
                    this.saveables.add(this.currShapeIndex + 1, this.selectedShape);
                    this.newIndex = this.shapes.indexOf(this.selectedShape);
                }
            }
            //move shape up in Pane graphically
            this.canvasPane.getChildren().remove(this.currItemInPane);
            this.canvasPane.getChildren().add(this.currItemInPane + 1, this.selectedShape.getShape());
            this.newPaneIndex = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());
        }
    }
}
