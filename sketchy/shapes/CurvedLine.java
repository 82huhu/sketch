package sketchy.shapes;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import sketchy.main.Constants;

/**
 * CurvedLine defines the attributes and functionalities of curved lines.
 */
public class CurvedLine implements Saveable {
    private Polyline drawingLine;
    private double red;
    private double green;
    private double blue;

    /**
     * the CurvedLine constructor creates a new Polyline() and sets the color of the line to
     * an inputted color.
     */
    public CurvedLine(Color color) {
        this.drawingLine = new Polyline();
        this.drawingLine.setStroke(color);

        //separates color's rgb values for saving
        this.red = color.getRed()* Constants.RGB_MULTIPLIER;
        this.green = color.getGreen()*Constants.RGB_MULTIPLIER;
        this.blue = color.getBlue()*Constants.RGB_MULTIPLIER;
    }

    /**
     * addPoint() adds the inputted point to the drawingLine.
     */
    public void addPoint(double x, double y) {
        this.drawingLine.getPoints().addAll(x, y);
    }

    /**
     * getLength() returns the length of the line.
     */
    public int getLength() {
        return this.drawingLine.getPoints().size();
    }

    /**
     * pointsToString() returns the points in the drawingLine as "x1 y1 x2 y2", etc.
     */
    public String pointsToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.drawingLine.getPoints().size(); i+=2) {
            double x = this.drawingLine.getPoints().get(i);
            double y = this.drawingLine.getPoints().get(i + 1);
            sb.append(" ").append(x).append(" ").append(y);
        }
        return sb.toString();
    }

    /**
     * draw() adds the drawingLine to the pane
     */
    public void draw(Pane canvasPane) {
        canvasPane.getChildren().add(this.drawingLine);
    }

    /**
     * setFill() fills in the lines drawn during drawingLine
     * I used this to make the lasso draw.
     */
    public void setFill(Color color) {
        this.drawingLine.setFill(color);
        this.red = color.getRed()*Constants.RGB_MULTIPLIER;
        this.green = color.getGreen()*Constants.RGB_MULTIPLIER;
        this.blue = color.getBlue()*Constants.RGB_MULTIPLIER;
    }

    /**
     * delete() removes the drawingLine from the pane.
     */
    @Override
    public void delete(Pane canvasPane) {
        canvasPane.getChildren().remove(this.drawingLine);
    }

    /**
     * toString() is used to save the files. It returns the attributes of
     * the line in text form.
     */
    @Override
    public String toString() {
        return "line " + (int)this.red + " " + (int)this.green + " "
                + (int)this.blue + " " + this.pointsToString();

    }
}
