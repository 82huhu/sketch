package sketchy.commands;

import javafx.scene.layout.Pane;
import sketchy.shapes.CurvedLine;
import sketchy.shapes.Saveable;
import java.util.ArrayList;

/**
 * DrawLineCommand is a command which creates a line
 */
public class DrawLineCommand implements Commands {
    private double x;
    private double y;
    private CurvedLine curvedLine;
    private Pane canvasPane;
    private ArrayList<Saveable> saveable;

    public DrawLineCommand(double x, double y, ArrayList<Saveable> saveables, CurvedLine curvedLine, Pane canvasPane) {
        this.x = x;
        this.y = y;
        this.curvedLine = curvedLine;
        this.canvasPane = canvasPane;
        this.saveable = saveables;
    }

    /**
     * undo() removes the line from the pane and the saveable arraylist
     */
    @Override
    public void undo() {
        this.saveable.remove(this.curvedLine);
        this.curvedLine.delete(this.canvasPane);
    }

    /**
     * redo() adds the line back to the pane and saveable arraylist
     */
    @Override
    public void redo() {
        this.saveable.add(this.curvedLine);
        this.curvedLine.draw(this.canvasPane);
    }

    /**
     * execute() adds the line to the
     */
    @Override
    public void execute() {
        this.curvedLine.addPoint(this.x, this.y);
        this.curvedLine.draw(this.canvasPane);
        this.saveable.add(this.curvedLine);
    }
}
