package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import sketchy.main.Constants;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

/**
 * MyEllipse is an ellipse wrapper class. It implements Selectable (and by extension Saveable) and
 * defines the attributes and functionality of MyEllipse.
 */
public class MyEllipse implements Selectable {
    private Ellipse ellipse;
    private double angle;

    public MyEllipse() {
        this.ellipse = new Ellipse();

        this.ellipse.setRadiusX(1);
        this.ellipse.setRadiusY(1);
    }

    /**
     * setLocation, setCenter, getLocation, and getCenter set and get the center of the ellipse.
     * Since rectangle needs both, ellipse needs to have both so they can both be called on
     * Selectable.
     */
    @Override
    public void setLocation(double x, double y) {
        this.ellipse.setCenterX(x);
        this.ellipse.setCenterY(y);
    }
    @Override
    public void setCenter(double x, double y) {
        this.ellipse.setCenterX(x);
        this.ellipse.setCenterY(y);
    }

    @Override
    public Point2D getCenter() {
        return new Point2D(this.ellipse.getCenterX(), this.ellipse.getCenterY());
    }

    @Override
    public Point2D getLocation() {
        return new Point2D(this.ellipse.getCenterX(), this.ellipse.getCenterY());
    }

    /**
     * getHeight, getWidth, setHeight, and setWidth get and set the width and height for ellipse.
     * they are multiplied/divided by two because the height and width are the diameter of
     * the ellipse, not radius.
     */
    @Override
    public double getHeight() {
        return this.ellipse.getRadiusY()*2;
    }

    @Override
    public double getWidth() {
        return this.ellipse.getRadiusX()*2;
    }

    @Override
    public void setHeight(double height) {
        this.ellipse.setRadiusY(height/2);
    }

    @Override
    public void setWidth(double width) {
        this.ellipse.setRadiusX(width/2);
    }

    /**
     * draw() adds the ellipse to the pane
     */
    @Override
    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.ellipse);
    }

    /**
     * delete removes the ellipse from the pane
     */
    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.ellipse);
    }

    /**
     * changeColor changes the fill of the ellipse to the inputted color
     */
    @Override
    public void changeColor(Color color) {
        this.ellipse.setFill(color);
    }

    /**
     * isSelected returns if the mouse is within the boundaries if the ellipse.
     */
    @Override
    public boolean isSelected(double x, double y) {
        Point2D point = this.rotatePoint(new Point2D(x, y),this.getCenter(), this.angle);
        //rotatePoint ensures isSelected() remains accurate for rotated shapes
        return this.ellipse.contains(point);
    }

    /**
     * setStroke() sets the outline of the ellipse to an inputted color
     */
    @Override
    public void setStroke(Color color) {
        this.ellipse.setStroke(color);
    }

    /**
     * setStrokeWidth() sets the outline of the ellipse to an inputted width
     */
    @Override
    public void setStrokeWidth(double width) {
        this.ellipse.setStrokeWidth(width);
    }

    /**
     * getColor() returns the fill of the ellipse
     */
    @Override
    public Color getColor() {
        return (Color) this.ellipse.getFill();
    }

    /**
     * getRotate() returns how rotated the ellipse is
     */
    @Override
    public double getRotate() {
        return this.ellipse.getRotate();
    }

    /**
     * setRotate sets the rotation of the ellipse to an inputted angle
     */
    @Override
    public void setRotate(double degrees) {
        this.angle = degrees;
        this.ellipse.setRotate(degrees);
    }

    /**
     * getShape() returns an actual ellipse. This is needed for methods that require Shapes
     * as inputs
     */
    @Override
    public Node getShape() {
        return this.ellipse;
    }

    /**
     * rotatePoint() ensures the points in contains() match the rotation of the ellipse
     */
    @Override
    public Point2D rotatePoint(Point2D pointsToRotate, Point2D rotateAround, double degrees) {
        double sine = sin(toRadians(degrees));
        double cosine = cos(toRadians(degrees));

        Point2D point = new Point2D(pointsToRotate.getX() - rotateAround.getX(),
                pointsToRotate.getY() - rotateAround.getY());
        point = new Point2D(point.getX()*cosine + point.getY()*sine, -point.getX()*sine +
                point.getY()*cosine);
        point = new Point2D(point.getX() + rotateAround.getX(), point.getY() +
                rotateAround.getY());

        return point;
    }

    /**
     * toString() returns the attributes of the ellipse as a string to be used for saving
     */
    @Override
    public String toString() {
        return "ellipse " + (int)(this.getColor().getRed()*Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getGreen()*Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getBlue()*Constants.RGB_MULTIPLIER) + " " +
                this.getCenter().getX() + " " + this.getCenter().getY()
                + " " + this.getWidth() + " " + this.getHeight() + " " + this.getRotate();
    }
}
