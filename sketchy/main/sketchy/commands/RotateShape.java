package sketchy.commands;

import javafx.geometry.Point2D;
import sketchy.shapes.Selectable;

import static java.lang.Math.atan2;

public class RotateShape implements Commands {
    private Selectable selectedShape;
    private double angle;
    private Point2D curr;
    private Point2D newCurr;
    private double initialRotation;
    private double finalRotation;

    public RotateShape(Selectable selectedShape, double angle, Point2D curr, Point2D newCurr, double initialRotation) {
        this.selectedShape = selectedShape;
        this.angle = angle;
        this.curr = curr;
        this.newCurr = newCurr;
        this.initialRotation = initialRotation;
    }

    @Override
    public void undo() {
        this.selectedShape.setRotate(this.initialRotation);
    }

    @Override
    public void redo() {
        this.selectedShape.setRotate(this.finalRotation);
    }

    @Override
    public void execute() {
        double centerY = this.selectedShape.getCenter().getY();
        double centerX = this.selectedShape.getCenter().getX();

        double angle = atan2(this.newCurr.getY()-centerY, this.newCurr.getX()-centerX) -
                atan2(this.curr.getY()-centerY, this.curr.getX()-centerX);

        this.angle = angle;

        double angleDeg = Math.toDegrees(angle);
        this.finalRotation = angleDeg;
        this.selectedShape.setRotate(this.finalRotation);
    }
}
