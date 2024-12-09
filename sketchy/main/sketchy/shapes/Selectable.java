package sketchy.shapes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public interface Selectable extends Saveable {
    public void setLocation(double x, double y);
    public void setCenter(double x, double y);
//    public void setCenterX(double x);
//    public void setCenterY(double y);
    public Point2D getCenter();
    public Point2D getLocation();
    public double getHeight();
    public double getWidth();
    public void setHeight(double height);
    public void setWidth(double width);
    public void draw(Pane canvasPane);
    public void delete(Pane canvasPane);
    public void changeColor(Color color);
    public boolean isSelected(double x, double y);
    public void setStroke(Color color);
    public void setStrokeWidth(double width);
    public void setFill(Color color);
    public Color getColor();
    public double getRotate();
    public void setRotate(double degrees);

//    public void resize(double ogWidth, double ogHeight, Point2D curr);
    public Node getShape();


    public Point2D rotatePoint(Point2D pointsToRotate, Point2D rotateAround, double degrees);
}
