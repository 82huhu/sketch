package sketchy.commands;

import javafx.geometry.Point2D;
import sketchy.shapes.Selectable;

public class TranslateShape implements Commands {
    private Point2D curr;
    private Point2D newCurr;
    private Selectable selectedShape;
    private double ogX;
    private double ogY;
    private double newX;
    private double newY;

    public TranslateShape(Point2D curr, Point2D newCurr, Selectable selectedShape, double ogX, double ogY, double releasedX, double releasedY) {
        this.curr = curr;
        this.newCurr = newCurr;
        this.selectedShape = selectedShape;
        this.ogX = ogX;
        this.ogY = ogY;
        this.newX = releasedX;
        this.newY = releasedY;
    }

    @Override
    public void undo() {
        this.selectedShape.setCenter(this.ogX, this.ogY);
    }

    @Override
    public void redo() {
        this.selectedShape.setCenter(this.newX, this.newY);
    }

    @Override
    public void execute() {
        double dx = this.newCurr.getX() - this.curr.getX();
        double dy = this.newCurr.getY() - this.curr.getY();

        this.newX = this.curr.getX() + dx;
        this.newY = this.curr.getY() + dy;

        this.selectedShape.setCenter(this.newX, this.newY);
    }
}
