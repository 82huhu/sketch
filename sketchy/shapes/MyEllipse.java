package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class MyEllipse implements Selectable {
    private Ellipse ellipse;
    private double angle;

    public MyEllipse() {
        this.ellipse = new Ellipse();

        this.ellipse.setRadiusX(1);
        this.ellipse.setRadiusY(1);
    }

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

    @Override
    public double getHeight() {
        return this.ellipse.getRadiusY();
    }

    @Override
    public double getWidth() {
        return this.ellipse.getRadiusX();
    }

    @Override
    public void setHeight(double height) {
        this.ellipse.setRadiusY(height/2);
    }

    @Override
    public void setWidth(double width) {
        this.ellipse.setRadiusX(width/2);
    }

    @Override
    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.ellipse);
    }

    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.ellipse);
    }

    @Override
    public void changeColor(Color color) {
        this.ellipse.setFill(color);
    }

    @Override
    public boolean isSelected(double x, double y) {
        Point2D point = this.rotatePoint(new Point2D(x, y),this.getCenter(), this.angle);
        return this.ellipse.contains(point);
    }

    @Override
    public void setStroke(Color color) {
        this.ellipse.setStroke(color);
    }

    @Override
    public void setStrokeWidth(double width) {
        this.ellipse.setStrokeWidth(width);
    }

    @Override
    public void setFill(Color color) {
        this.ellipse.setFill(color);
    }

    @Override
    public Color getColor() {
        return (Color) this.ellipse.getFill();
    }

    @Override
    public double getRotate() {
        return this.ellipse.getRotate();
    }

    @Override
    public void setRotate(double degrees) {
        this.angle = degrees;
        this.ellipse.setRotate(degrees);
    }

    @Override
    public Node getShape() {
        return this.ellipse;
    }

    @Override
    public Point2D rotatePoint(Point2D pointsToRotate, Point2D rotateAround, double degrees) {
        double sine = sin(toRadians(degrees));
        double cosine = cos(toRadians(degrees));

        Point2D point = new Point2D(pointsToRotate.getX() - rotateAround.getX(),
                pointsToRotate.getY() - rotateAround.getY());
        point = new Point2D(point.getX()*cosine + point.getY()*sine, -point.getX()*sine + point.getY()*cosine);
        point = new Point2D(point.getX() + rotateAround.getX(), point.getY() + rotateAround.getY());

        return point;
    }

    @Override
    public String toString() {
        return "ellipse " + (int)(this.getColor().getRed()*255) + " " + (int)(this.getColor().getGreen()*255) + " " +
                (int)(this.getColor().getBlue()*255) + " " + this.getCenter().getX() + " " + this.getCenter().getY()
                + " " + this.getWidth() + " " + this.getHeight() + " " + this.getRotate();
    }
}
