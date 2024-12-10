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

import static java.lang.Math.*;


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
    private boolean creatingShape;
    private double angle;
    private double initialHeight;
    private double initialWidth;

    public Canvas(Pane canvasPane, VBox controlPane) {
        this.isDrawLine = false;
        this.isLasso = false;
        this.isSelecting = false;
        this.creatingShape = false;
        this.angle = 0;
        this.shapes = new ArrayList<>();
        this.canvasPane = canvasPane;
        this.controlPane = controlPane;

        this.setUpRadioButtons(controlPane);
        this.setUpControlPane(controlPane);
        this.setColorPicker(controlPane);
        this.setUpButtons(controlPane);

        this.canvasPane.setOnMousePressed((MouseEvent e) -> this.click(e));
        this.canvasPane.setOnMouseDragged((MouseEvent e) -> this.drag(e));
        this.canvasPane.setFocusTraversable(false);
    }

    private void setUpControlPane(VBox controlPane) {
        controlPane.setStyle("-fx-background-color: orange;");
        this.canvasPane.setStyle("-fx-background-color: skyblue;");
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
        selectShape.setOnAction((ActionEvent e)-> {this.isDrawLine = false; this.creatingShape = false; this.isSelecting = true;});

        RadioButton drawWithPen = new RadioButton("draw with pen");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawWithPen);
        drawWithPen.setToggleGroup(drawingOptionsGroup);
        drawWithPen.setOnAction(ActionEvent -> {this.isLasso = false; this.creatingShape = false; this.isDrawLine = true;});

        RadioButton lassoDraw = new RadioButton("draw with lasso");
        selectShape.setSelected(false);
        controlPane.getChildren().add(lassoDraw);
        lassoDraw.setToggleGroup(drawingOptionsGroup);
        lassoDraw.setOnAction(ActionEvent -> {this.isLasso = true; this.creatingShape = false; this.isDrawLine = true;});

        RadioButton drawRect = new RadioButton("draw rectangle");
        drawRect.setToggleGroup(drawingOptionsGroup);
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawRect);
        drawRect.setOnAction(ActionEvent -> {this.isSelecting = false; this.creatingShape = true;
            this.radioButtonClick(ShapesEnum.RECTANGLE);});

        RadioButton drawEllipse = new RadioButton("draw ellipse");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawEllipse);
        drawEllipse.setToggleGroup(drawingOptionsGroup);
        drawEllipse.setOnAction(ActionEvent -> {this.isSelecting = false; this.creatingShape = true;
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
        raise.setOnAction((ActionEvent e) -> this.raise());

        Button lower = new Button("lower");
        controlPane.getChildren().add(lower);
        lower.setOnAction((ActionEvent e) -> this.lower());

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
        if(this.selectedShape != null && this.selectedShapeEnum == null) {
            this.initialHeight = this.selectedShape.getHeight();
            this.initialWidth = this.selectedShape.getWidth();
        }

        //drawing line
        if(this.isDrawLine){
            //deselects selected shape, if there is one
            if(this.selectedShape != null) {
                this.selectedShape.setStroke(Color.TRANSPARENT);
                this.selectedShape = null;
            }

            this.createLine(event.getX(), event.getY());
            return;
        }

        //drawing shapes
        if(this.selectedShapeEnum != null && !this.isSelecting){
            this.drawShape(event);
        }

        //selecting
        if(this.isSelecting) {
            this.select(event);
        }
    }

    private void drag(MouseEvent event) {
        this.penDrawOnDrag(event.getX(),event.getY());

        Point2D currLoc = new Point2D(event.getX(), event.getY());

        if(this.selectedShape != null) {
            if(event.isShiftDown() && event.isControlDown()) {
                this.rotateAndResize(currLoc);
            } else if(event.isShiftDown() || this.creatingShape) {
                //resizes shape
                this.resize(currLoc);
            } else if(event.isControlDown()) {
                //rotates shape
                this.rotate(this.selectedShape.getCenter(), currLoc);
            } else {
                //translates shape
                this.translate(this.selectedShape.getCenter(), currLoc);
            }

        }
    }

    private void drawShape(MouseEvent event) {
        Selectable shape = this.selectedShapeEnum.getShape();
        shape.setFill(this.colorPicker.getValue());
        this.shapes.add(shape);
        shape.setLocation(event.getX(), event.getY());
        shape.draw(this.canvasPane);

        //selects the newly drawn shape
        this.select(event);

        Point2D currLoc = new Point2D(event.getX(), event.getY());
        this.resize(currLoc);
    }

    private void select(MouseEvent event) {
        for (int i = this.shapes.size()-1; i >=0; i--) {
            Selectable shape = this.shapes.get(i);

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

        this.selectedShape.setCenter(newX, newY);
    }

    private void rotate(Point2D curr, Point2D newCurr) {
        double centerY = this.selectedShape.getCenter().getY();
        double centerX = this.selectedShape.getCenter().getX();

        double angle = atan2(newCurr.getY()-centerY, newCurr.getX()-centerX) -
                atan2(curr.getY()-centerY, curr.getX()-centerX);

        this.angle = angle;

        double angleDeg = Math.toDegrees(angle);
        this.selectedShape.setRotate(angleDeg);
    }

    private void resize(Point2D curr) {
        Point2D center = this.selectedShape.getCenter();
        Point2D rotatedCurr = this.selectedShape.rotatePoint(curr, center, -this.selectedShape.getRotate());
        double dx = Math.abs(rotatedCurr.getX() - center.getX());
        double dy = Math.abs(rotatedCurr.getY() - center.getY());
        this.selectedShape.setWidth(this.initialWidth + dx*2);
        this.selectedShape.setHeight(this.initialHeight + dy*2);
        this.selectedShape.setCenter(center.getX(), center.getY());
    }

    private void rotateAndResize(Point2D curr) {
        this.rotate(this.selectedShape.getCenter(), curr);
        this.resize(curr);
    }

    private void raise() {
        if (this.selectedShape != null) {
            int currShapeIndex = this.shapes.indexOf(this.selectedShape);
            int currItemInPane = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());

            if (currItemInPane + 1 < this.canvasPane.getChildren().size() ) {
                if (currShapeIndex + 1 < this.shapes.size()) {
                    //move shape up in ArrayList logically
                    int nextShapeInArray = currShapeIndex + 1;
                    Selectable nextShape = this.shapes.get(nextShapeInArray);
                    int nextShapeInPane = this.canvasPane.getChildren().indexOf(nextShape.getShape());
                    if (nextShapeInPane - currItemInPane == 1) {
                        this.shapes.remove(currShapeIndex);
                        this.shapes.add(currShapeIndex + 1, this.selectedShape);
                    }
                }
                //move shape up in Pane graphically
                this.canvasPane.getChildren().remove(currItemInPane);
                this.canvasPane.getChildren().add(currItemInPane + 1, this.selectedShape.getShape());
            }
        }
    }

    private void lower() {
        if(this.selectedShape != null) {
            int currShapeIndex = this.shapes.indexOf(this.selectedShape);
            int currItemInPane = this.canvasPane.getChildren().indexOf(this.selectedShape.getShape());

            if(currItemInPane - 1 >= 0) {
                if(currShapeIndex - 1 >= 0) {
                    //move shape up in ArrayList logically
                    int prevShapeInArray = currShapeIndex - 1;
                    Selectable prevShape = this.shapes.get(prevShapeInArray);
                    int prevShapeInPane = this.canvasPane.getChildren().indexOf(prevShape.getShape());
                    if(currItemInPane - prevShapeInPane == 1) {
                        this.shapes.remove(currShapeIndex);
                        this.shapes.add(currShapeIndex - 1, this.selectedShape);
                    }
                }
                //move shape up in Pane graphically
                this.canvasPane.getChildren().remove(currItemInPane);
                this.canvasPane.getChildren().add(currItemInPane - 1, this.selectedShape.getShape());
            }
        }
    }

}