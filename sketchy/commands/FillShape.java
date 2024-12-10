package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import sketchy.shapes.Selectable;

public class FillShape implements Commands {
    private Selectable selectedShape;
    private ColorPicker colorPicker;
    private Color originalColor;
    private Color newColor;

    public FillShape(Selectable selectedShape, ColorPicker colorPicker) {
       this.selectedShape = selectedShape;
       this.colorPicker = colorPicker;
       this.originalColor = this.selectedShape.getColor();
       this.newColor = this.selectedShape.getColor();
    }

    @Override
    public void undo() {
        this.selectedShape.setFill(this.originalColor);
    }

    @Override
    public void redo() {
        this.selectedShape.setFill(this.newColor);
    }

    @Override
    public void execute() {
        Color selectedColor = this.colorPicker.getValue();
        this.selectedShape.changeColor(selectedColor);
        this.newColor = this.selectedShape.getColor();
    }
}
