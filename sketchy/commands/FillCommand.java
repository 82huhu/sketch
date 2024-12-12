package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import sketchy.shapes.Selectable;

/**
 * FillCommand is a command which changes the color of the shape to the color picker's color
 */
public class FillCommand implements Commands {
    private Selectable selectedShape;
    private ColorPicker colorPicker;
    private Color originalColor;
    private Color newColor;

    public FillCommand(Selectable selectedShape, ColorPicker colorPicker) {
       this.selectedShape = selectedShape;
       this.colorPicker = colorPicker;
       this.originalColor = this.selectedShape.getColor();
       this.newColor = this.selectedShape.getColor();
    }

    /**
     * undo() changes the color back to the previous color of the shape
     */
    @Override
    public void undo() {
        this.selectedShape.changeColor(this.originalColor);
    }

    /**
     * redo() changes the color back to the color it was changed into
     */
    @Override
    public void redo() {
        this.selectedShape.changeColor(this.newColor);
    }

    /**
     * execute() changes the color of the selected shape to the color of the color picker
     */
    @Override
    public void execute() {
        Color selectedColor = this.colorPicker.getValue();
        this.selectedShape.changeColor(selectedColor);
        this.newColor = this.selectedShape.getColor();
    }
}
