package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sketchy.main.Constants;

import static java.lang.Math.*;

public class MyRectangle implements Selectable {

    private Rectangle rect;
    private double degrees;

    public MyRectangle() {
        this.rect = new Rectangle();

        this.rect.setHeight(30);
        this.rect.setWidth(30);
    }

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
    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.rect);
    }

    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.rect);
    }

    @Override
    public void changeColor(Color color) {
        this.rect.setFill(color);
    }

    @Override
    public boolean isSelected(double x, double y) {
        Point2D point = this.rotatePoint(new Point2D(x, y), this.getCenter(), this.degrees);
        return this.rect.contains(point);
    }

    @Override
    public void setStroke(Color color) {
        this.rect.setStroke(color);
    }

    @Override
    public void setStrokeWidth(double width) {
        this.rect.setStrokeWidth(width);
    }

    @Override
    public Color getColor() {
        return (Color) this.rect.getFill();
    }

    @Override
    public double getRotate() {
        return this.rect.getRotate();
    }

    @Override
    public void setRotate(double degrees) {
        this.degrees = degrees;
        this.rect.setRotate(degrees);
    }


    @Override
    public Node getShape() {
        return this.rect;
    }

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

    @Override
    public Point2D getCenter() {
        return new Point2D((this.rect.getX() + this.rect.getWidth() / 2),
                (this.rect.getY() + this.rect.getHeight() / 2));
    }

    @Override
    public Point2D getLocation() {
        return new Point2D(this.rect.getX(), this.rect.getY());
    }

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

    @Override
    public String toString() {
        return "rectangle " + (int)(this.getColor().getRed()* Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getGreen()*Constants.RGB_MULTIPLIER) + " " +
                (int)(this.getColor().getBlue()*Constants.RGB_MULTIPLIER) + " " +
                this.getLocation().getX() + " " +
                this.getLocation().getY() + " " + this.getWidth() + " " + this.getHeight() + " " +
                this.getRotate();
    }

}
