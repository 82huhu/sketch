package sketchy.shapes;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class CurvedLine implements Saveable {
    Polyline drawingLine;

    public CurvedLine(Color color) {
        this.drawingLine = new Polyline();
        this.drawingLine.setStroke(color);
    }

    public void addPoint(double x, double y) {
        this.drawingLine.getPoints().addAll(x, y);
    }

    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.drawingLine);
    }

    public void setFill(Color color) {
        this.drawingLine.setFill(color);
    }
}
