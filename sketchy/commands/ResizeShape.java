package sketchy.commands;

import javafx.geometry.Point2D;
import sketchy.shapes.Selectable;

/**
 * Resize is a command which changes the size of a shape depending on the mouse drag
 */
public class ResizeShape implements Commands {
    private double initialWidth;
    private double initialHeight;
    private double finalWidth;
    private double finalHeight;
    private Selectable selectedShape;
    private Point2D curr;

    public ResizeShape(double initialWidth, double initialHeight, Selectable selectedShape,
                       Point2D curr) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.selectedShape = selectedShape;
        this.curr = curr;
    }

    /**
     * undo() returns the shape to its original size
     */
    @Override
    public void undo() {
        this.selectedShape.setWidth(this.initialWidth);
        this.selectedShape.setHeight(this.initialHeight);
    }

    /**
     * redo() returns the shape to its size after resizing
     */
    @Override
    public void redo() {
        this.selectedShape.setWidth(this.finalWidth);
        this.selectedShape.setHeight(this.finalHeight);
    }

    /**
     * execute() resizes the shape based on the mouse's distance from its original x location.
     */
    @Override
    public void execute() {
        Point2D center = this.selectedShape.getCenter();
        Point2D rotatedCurr = this.selectedShape.rotatePoint(this.curr, center,
                -this.selectedShape.getRotate());

        double dx = Math.abs(rotatedCurr.getX() - center.getX());
        double dy = Math.abs(rotatedCurr.getY() - center.getY());
        this.finalWidth = this.initialWidth + dx*2;
        this.finalHeight = this.initialHeight + dy*2;

        this.selectedShape.setWidth(this.finalWidth);
        this.selectedShape.setHeight(this.finalHeight);
        this.selectedShape.setCenter(center.getX(), center.getY());

        //resetting initialWidth and initialHeight
        this.initialWidth = this.finalWidth;
        this.initialHeight = this.finalHeight;
    }
}
