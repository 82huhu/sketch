package sketchy.shapes;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;

public class CurvedLine implements Saveable {
    private Polyline drawingLine;
    private double red;
    private double green;
    private double blue;

    public CurvedLine(Color color) {
        this.drawingLine = new Polyline();
        this.drawingLine.setStroke(color);
        this.red = color.getRed()*255;
        this.green = color.getGreen()*255;
        this.blue = color.getBlue()*255;
    }

    public void addPoint(double x, double y) {
        this.drawingLine.getPoints().addAll(x, y);
    }

    public int getLength() {
        return this.drawingLine.getPoints().size();
    }

    public String pointsToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.drawingLine.getPoints().size()-1; i++) {
            double x = this.drawingLine.getPoints().get(i);
            double y = this.drawingLine.getPoints().get(i + 1);
            sb.append(x).append(" ").append(y).append(" ");
        }
        return sb.toString();
    }


    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.drawingLine);
    }

    public void setFill(Color color) {
        this.drawingLine.setFill(color);
        this.red = color.getRed()*255;
        this.green = color.getGreen()*255;
        this.blue = color.getBlue()*255;
    }

    public Node getNode() {
        return this.drawingLine;
    }

    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.drawingLine);
    }

    @Override
    public String toString() {
        return "line " + (int)this.red + " " + (int)this.green + " "
                + (int)this.blue + " " + this.pointsToString();

    }
}
