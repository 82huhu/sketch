package sketchy.main;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sketchy.shapes.*;

import java.util.ArrayList;

import static java.lang.Math.atan2;


public class Canvas {
    private Pane canvasPane;
    private VBox controlPane;
    private ShapesEnum selectedShapeEnum;
    private boolean isDrawLine;
    private CurvedLine curvedLine;
    private boolean isLasso;
    private ColorPicker colorPicker;
    private boolean isSelecting;
    private ArrayList<Selectable> shapes;
    private Selectable selectedShape;

    public Canvas(Pane canvasPane, VBox controlPane) {
        this.isDrawLine = false;
        this.isLasso = false;
        this.isSelecting = false;
        this.shapes = new ArrayList<>();
        this.canvasPane = canvasPane;
        this.controlPane = controlPane;

        this.setUpRadioButtons(controlPane);
        this.setUpControlPane(controlPane);
        this.setColorPicker(controlPane);
        this.setUpButtons(controlPane);

        this.canvasPane.setOnMousePressed((MouseEvent e) -> this.click(e));
        this.canvasPane.setOnMouseDragged((MouseEvent e) -> this.drag(e));
        //this.canvasPane.setOnKeyPressed((KeyEvent e) -> this.onKeyPress(e));
        this.canvasPane.setFocusTraversable(false);
    }

    private void setUpControlPane(VBox controlPane) {
        controlPane.setStyle("-fx-background-color: orange;");
        controlPane.setPrefWidth(Constants.CONTROL_WIDTH);
        controlPane.setAlignment(Pos.CENTER);
        controlPane.setSpacing(Constants.CONTROL_SPACING);
    }

    private void setUpRadioButtons(VBox controlPane) {
        Label drawingOptionsLabel = new Label("Drawing Options");
        controlPane.getChildren().add(drawingOptionsLabel);
        ToggleGroup drawingOptionsGroup = new ToggleGroup();

        RadioButton selectShape = new RadioButton("select shape");
        selectShape.setSelected(false);
        controlPane.getChildren().add(selectShape);
        selectShape.setToggleGroup(drawingOptionsGroup);
        selectShape.setOnAction((ActionEvent e)-> {this.isDrawLine = false; this.isSelecting = true;});

        RadioButton drawWithPen = new RadioButton("draw with pen");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawWithPen);
        drawWithPen.setToggleGroup(drawingOptionsGroup);
        drawWithPen.setOnAction(ActionEvent -> {this.isLasso = false; this.isDrawLine = true;});

        RadioButton lassoDraw = new RadioButton("draw with lasso");
        selectShape.setSelected(false);
        controlPane.getChildren().add(lassoDraw);
        lassoDraw.setToggleGroup(drawingOptionsGroup);
        lassoDraw.setOnAction(ActionEvent -> {this.isLasso = true; this.isDrawLine = true;});

        RadioButton drawRect = new RadioButton("draw rectangle");
        drawRect.setToggleGroup(drawingOptionsGroup);
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawRect);
        drawRect.setOnAction(ActionEvent -> {this.isSelecting = false;
            this.radioButtonClick(ShapesEnum.RECTANGLE);});

        RadioButton drawEllipse = new RadioButton("draw ellipse");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawEllipse);
        drawEllipse.setToggleGroup(drawingOptionsGroup);
        drawEllipse.setOnAction(ActionEvent -> {this.isSelecting = false;
            this.radioButtonClick(ShapesEnum.ELLIPSE);});

    }

    private void setColorPicker(VBox controlPane) {
        Label setColorLabel= new Label("Set Color");
        controlPane.getChildren().add(setColorLabel);

        this.colorPicker = new ColorPicker();
        controlPane.getChildren().add(this.colorPicker);
    }

    private void setUpButtons(VBox controlPane) {
        Label ShapeOptionsLabel = new Label("Shape Options");
        controlPane.getChildren().add(ShapeOptionsLabel);

        Button fill = new Button("fill");
        controlPane.getChildren().add(fill);
        fill.setOnAction((ActionEvent e) -> this.fillAction());

        Button delete = new Button("delete");
        controlPane.getChildren().add(delete);
        delete.setOnAction((ActionEvent e) -> this.deleteAction());

        Button raise = new Button("raise");
        controlPane.getChildren().add(raise);

        Button lower = new Button("lower");
        controlPane.getChildren().add(lower);

        Label operationsLabel = new Label("Operations");
        controlPane.getChildren().add(operationsLabel);

        Button undo = new Button("undo");
        controlPane.getChildren().add(undo);

        Button redo = new Button("redo");
        controlPane.getChildren().add(redo);

        Button save = new Button("save");
        controlPane.getChildren().add(save);

        Button load = new Button("load");
        controlPane.getChildren().add(load);

    }

    private void fillAction() {
        if(this.selectedShape != null) {
            Color selectedColor = this.colorPicker.getValue();
            this.selectedShape.changeColor(selectedColor);
        }
    }

    private void deleteAction() {
        if(this.selectedShape != null) {
            this.shapes.remove(this.selectedShape);
            this.selectedShape.delete(this.canvasPane);
        }
    }


    private void click(MouseEvent event) {
        if(this.isDrawLine){
            //deselects selected shape, if there is one
            if(this.selectedShape != null) {
                this.selectedShape.setStroke(Color.TRANSPARENT);
                this.selectedShape = null;
            }

            this.createLine(event.getX(), event.getY());
            return;
        }

        if(this.selectedShapeEnum != null && !this.isSelecting){
            this.drawShape(event);
        }
        
        if(this.isSelecting) {
            this.select(event);
        }
    }

    private void drag(MouseEvent event) {
        this.penDrawOnDrag(event.getX(),event.getY());

        Point2D currLoc = new Point2D(event.getX(), event.getY());
        if(this.selectedShape != null) {
            if(event.isControlDown()) {
                this.rotate(this.selectedShape.getCenter(), currLoc);
            } else if(event.isShiftDown()) {
                this.resize(this.selectedShape.getCenter(), currLoc);
            } else {
                this.translate(this.selectedShape.getCenter(), currLoc);
            }

        }
    }

    private void drawShape(MouseEvent event) {
        Selectable shape = this.selectedShapeEnum.getShape();
        shape.setFill(this.colorPicker.getValue());
        this.shapes.add(0, shape);
        shape.setLocation(event.getX(), event.getY());
        shape.draw(this.canvasPane);
    }

    private void select(MouseEvent event) {
        for (Selectable shape : this.shapes) {
            //deselects all shapes except the most recently clicked shape
            if(this.selectedShape != null) {
                this.selectedShape.setStroke(Color.TRANSPARENT);
                this.selectedShape = null;
            }

            if (shape.isSelected(event.getX(), event.getY())) {
                this.selectedShape = shape;
                this.selectedShape.setStroke(Color.LAWNGREEN);
                this.selectedShape.setStrokeWidth(2.5);
                return;
            }
        }
    }

    private void createLine(double x, double y){
        this.curvedLine = new CurvedLine(this.colorPicker.getValue());
        this.curvedLine.addPoint(x, y);
        this.curvedLine.draw(this.canvasPane);
    }

    private void penDrawOnDrag(double x, double y) {
        if(this.curvedLine !=null && this.isDrawLine){
            if(this.isLasso) {
                this.curvedLine.setFill(this.colorPicker.getValue());
            }
            this.curvedLine.addPoint(x, y);
        }
    }

    private void radioButtonClick(ShapesEnum shape) {
        this.isDrawLine = false;
        this.selectedShapeEnum = shape;
    }

    private void translate(Point2D curr, Point2D newCurr) {
        double dx = newCurr.getX() - curr.getX();
        double dy = newCurr.getY() - curr.getY();

        double newX = curr.getX() + dx;
        double newY = curr.getY() + dy;

        this.selectedShape.setLocation(newX, newY);
    }

    private void rotate(Point2D curr, Point2D newCurr) {
        double centerY = this.selectedShape.getCenter().getY();
        double centerX = this.selectedShape.getCenter().getX();

        double angle = atan2(newCurr.getY()-centerY, newCurr.getX()-centerX) -
                atan2(curr.getY()-centerY, curr.getX()-centerX);

        double angleDeg = Math.toDegrees(angle);
        this.selectedShape.setRotate(angleDeg);
    }

    private void resize(Point2D curr, Point2D newCurr) {
        double dx = newCurr.getX() - curr.getX();
        double dy = newCurr.getY() - curr.getY();

        this.selectedShape.setCenter(this.selectedShape.getCenter().getX(),
                this.selectedShape.getCenter().getY(), dx, dy);
        this.selectedShape.setHeight(dy);
        this.selectedShape.setWidth(dx);
    }

}