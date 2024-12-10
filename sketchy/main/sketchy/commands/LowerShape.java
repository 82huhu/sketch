package sketchy.commands;

import javafx.scene.layout.Pane;
import sketchy.shapes.Selectable;

import java.util.ArrayList;

public class LowerShape implements Commands {
    private ArrayList<Selectable> shapes;
    private Selectable selectedShape;
    private Pane canvasPane;
    private int currShapeIndex;
    private int currItemInPane;
    private int newIndex;
    private int newPaneIndex;

    public LowerShape(ArrayList<Selectable> shapes, Selectable selectedShape, Pane canvasPane) {
        this.shapes = shapes;
        this.selectedShape = selectedShape;
        this.canvasPane = canvasPane;
        this.currShapeIndex = this.shapes.indexOf(this.selectedShape);
        this.currItemInPane = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());

    }
    @Override
    public void undo() {
        this.shapes.remove(this.newIndex);
        this.shapes.add(this.currShapeIndex, this.selectedShape);
        this.canvasPane.getChildren().remove(this.newPaneIndex);
        this.canvasPane.getChildren().add(this.currItemInPane, this.selectedShape.getShape());
    }

    @Override
    public void redo() {
        this.shapes.remove(this.currShapeIndex);
        this.shapes.add(this.newIndex, this.selectedShape);
        this.canvasPane.getChildren().remove(this.currItemInPane);
        this.canvasPane.getChildren().add(this.newPaneIndex, this.selectedShape.getShape());
    }

    @Override
    public void execute() {
        this.currShapeIndex = this.shapes.indexOf(this.selectedShape);
        this.currItemInPane = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());

        if(this.currItemInPane - 1 >= 0) {
            if(this.currShapeIndex - 1 >= 0) {
                //move shape up in ArrayList logically
                int prevShapeInArray = this.currShapeIndex - 1;
                Selectable prevShape = this.shapes.get(prevShapeInArray);
                int prevShapeInPane = this.canvasPane.getChildren().indexOf(prevShape.getShape());
                if(this.currItemInPane - prevShapeInPane == 1) {
                    this.shapes.remove(this.currShapeIndex);
                    this.shapes.add(this.currShapeIndex - 1, this.selectedShape);
                    this.newIndex = this.shapes.indexOf(this.selectedShape);
                }
            }
            //move shape up in Pane graphically
            this.canvasPane.getChildren().remove(this.currItemInPane);
            this.canvasPane.getChildren().add(this.currItemInPane - 1, this.selectedShape.getShape());
            this.newPaneIndex = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());
        }
    }
}
