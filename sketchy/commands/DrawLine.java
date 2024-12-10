package sketchy.commands;

import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sketchy.shapes.CurvedLine;
import sketchy.shapes.Saveable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DrawLine implements Commands {
    private double x;
    private double y;
    private CurvedLine curvedLine;
    private Pane canvasPane;
    private ArrayList<Saveable> saveable;


    public DrawLine(double x, double y, ArrayList<Saveable> saveables, CurvedLine curvedLine, Pane canvasPane) {
        this.x = x;
        this.y = y;
        this.curvedLine = curvedLine;
        this.canvasPane = canvasPane;
        this.saveable = saveables;
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
        this.saveable.add(this.curvedLine);
    }
}
