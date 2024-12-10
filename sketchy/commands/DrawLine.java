package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sketchy.shapes.CurvedLine;

public class DrawLine implements Commands {
    private double x;
    private double y;
    private CurvedLine curvedLine;
    private ColorPicker colorPicker;
    private Pane canvasPane;


    public DrawLine(double x, double y, CurvedLine curvedLine, ColorPicker colorPicker, Pane canvasPane) {
        this.x = x;
        this.y = y;
        this.colorPicker = colorPicker;
        this.curvedLine = curvedLine;
        this.canvasPane = canvasPane;
    }

    @Override
    public void undo() {
        this.canvasPane.getChildren().remove(this.curvedLine.getNode());
    }

    @Override
    public void redo() {
        this.canvasPane.getChildren().add(this.curvedLine.getNode());
    }

    @Override
    public void execute() {
        this.curvedLine.addPoint(this.x, this.y);
        this.curvedLine.draw(this.canvasPane);
    }
}
