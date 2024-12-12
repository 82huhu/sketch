package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sketchy.main.Constants;

import static java.lang.Math.*;

/**
 * MyRectangle is a rectangle wrapper class. It implements Selectable (and by extension Saveable) and
 * defines the attributes and functionality of a MyRectangle.
 */
public class MyRectangle implements Selectable {
    private Rectangle rect;
    private double degrees;

    public MyRectangle() {
        this.rect = new Rectangle();

        this.rect.setHeight(1);
        this.rect.setWidth(1);
    }

    /**
     * setLocation, setCenter, getLocation, and getCenter set and get the center of the rectangle.
     */
    @Override
    public void setLocation(double x, double y) {
        this.rect.setX(x);
        this.rect.setY(y);
    }
    @Override
    public void setCenter(double x, double y) {
        this.rect.setX(x - this.rect.getWidth()/2);
        this.rect.setY(y- this.rect.getHeight()/2);
    }
    @Override
    public Point2D getLocation() {
        return new Point2D(this.rect.getX(), this.rect.getY());
    }
    @Override
    public Point2D getCenter() {
        return new Point2D((this.rect.getX() + this.rect.getWidth() / 2),
                (this.rect.getY() + this.rect.getHeight() / 2));
    }

    /**
     * getHeight, getWidth, setHeight, and setWidth get and set the width and height for rectangle.
     */
    @Override
    public double getHeight() {
        return this.rect.getHeight();
    }
    @Override
    public double getWidth() {
        return this.rect.getWidth();
    }
    @Override
    public void setHeight(double height) {
        this.rect.setHeight(height);
    }
    @Override
    public void setWidth(double width) {
        this.rect.setWidth(width);
    }

    /**
     * draw() adds the rectangle to the pane
     */
    @Override
    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.rect);
    }

    /**
     * delete() removes the rectangle from the game
     */
    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.rect);
    }

    /**
     * changeColor changes the fill of the rectangle to the inputted color
     */
    @Override
    public void changeColor(Color color) {
        this.rect.setFill(color);
    }

    /**
     * isSelected returns if the mouse is within the boundaries if the rectangle.
     */
    @Override
    public boolean isSelected(double x, double y) {
        Point2D point = this.rotatePoint(new Point2D(x, y), this.getCenter(), this.degrees);
        return this.rect.contains(point);
    }

    /**
     * setStroke() sets the outline of the rectangle to an inputted color
     */
    @Override
    public void setStroke(Color color) {
        this.rect.setStroke(color);
    }

    /**
     * setStrokeWidth() sets the outline of the rectangle to an inputted width
     */
    @Override
    public void setStrokeWidth(double width) {
        this.rect.setStrokeWidth(width);
    }

    /**
     * getColor() returns the fill of the rectangle
     */
    @Override
    public Color getColor() {
        return (Color) this.rect.getFill();
    }

    /**
     * getRotate() returns how rotated the rectangle is
     */
    @Override
    public double getRotate() {
        return this.rect.getRotate();
    }

    /**
     * setRotate sets the rotation of the rectangle to an inputted angle
     */
    @Override
    public void setRotate(double degrees) {
        this.degrees = degrees;
        this.rect.setRotate(degrees);
    }

    /**
     * getShape() returns an actual rectangle. This is needed for methods that require Shapes
     * as inputs
     */
    @Override
    public Node getShape() {
        return this.rect;
    }

    /**
     * rotatePoint() ensures the points in contains() match the rotation of the rectangle
     */
    @Override
    public Point2D rotatePoint(Point2D pointsToRotate, Point2D rotateAround, double degrees) {
        double sine = sin(toRadians(degrees));
        double cosine = cos(toRadians(degrees));

        Point2D point = new Point2D(pointsToRotate.getX() - rotateAround.getX(),
                pointsToRotate.getY() - rotateAround.getY());
        point = new Point2D(point.getX()*cosine + point.getY()*sine, -point.getX()*sine +
                point.getY()*cosine);
        point = new Point2D(point.getX() + rotateAround.getX(), point.getY() + rotateAround.getY());

        return point;
    }

    /**
     * toString() returns the attributes of the rectangle as a string to be used for saving
     */
    @Override
    public String toString() {
        return "rectangle " + (int)(this.getColor().getRed()*Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getGreen()*Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getBlue()*Constants.RGB_MULTIPLIER) + " " +
                this.getLocation().getX() + " " +
                this.getLocation().getY() + " " + this.getWidth() + " " + this.getHeight() + " " +
                this.getRotate();
    }

}
