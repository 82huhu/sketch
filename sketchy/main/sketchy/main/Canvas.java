package sketchy.main;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sketchy.commands.*;
import sketchy.shapes.*;

import java.util.ArrayList;
import java.util.Stack;


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
    private boolean isTranslating;
    private boolean isResizing;
    private boolean isRotating;
    private double angle;
    private double initialHeight;
    private double initialWidth;
    private double initialRotation;
    private double ogX;
    private double ogY;
    private Commands command;
    private Stack<Commands> undoStack;
    private Stack<Commands> redoStack;
    private Commands translateShape;
    private Commands resizeShape;
    private Commands rotateShape;

    public Canvas(Pane canvasPane, VBox controlPane) {
        this.isDrawLine = false;
        this.isLasso = false;
        this.isSelecting = false;
        this.creatingShape = false;
        this.angle = 0;
        this.shapes = new ArrayList<>();
        this.canvasPane = canvasPane;
        this.controlPane = controlPane;
        this.command = null;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.isTranslating = false;
        this.isResizing = false;
        this.isRotating = false;

        this.setUpRadioButtons(controlPane);
        this.setUpControlPane(controlPane);
        this.setColorPicker(controlPane);
        this.setUpButtons(controlPane);

        this.canvasPane.setOnMousePressed((MouseEvent e) -> this.click(e));
        this.canvasPane.setOnMouseDragged((MouseEvent e) -> this.drag(e));
        this.canvasPane.setOnMouseReleased((MouseEvent e) -> this.released());
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
        undo.setOnAction((ActionEvent e) -> this.undoAction());

        Button redo = new Button("redo");
        controlPane.getChildren().add(redo);
        redo.setOnAction((ActionEvent e) -> this.redoAction());

        Button save = new Button("save");
        controlPane.getChildren().add(save);

        Button load = new Button("load");
        controlPane.getChildren().add(load);

    }

    private void fillAction() {
        if(this.selectedShape != null) {
           Commands fill = new FillShape(this.selectedShape, this.colorPicker);
            fill.execute();
            this.command = fill;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    private void deleteAction() {
        Commands delete = new DeleteShape(this.selectedShape, this.shapes, this.canvasPane);
        delete.execute();
        this.command = delete;
        this.undoStack.push(this.command);
        this.redoStack.clear();
    }

    private void undoAction() {
        if(!this.undoStack.isEmpty()) {
            Commands command = this.undoStack.pop();
            this.redoStack.push(command);
            command.undo();
        }
    }

    private void redoAction() {
        if(!this.redoStack.isEmpty()) {
            Commands command = this.redoStack.pop();
            this.undoStack.push(command);
            command.redo();
        }
    }

    private void click(MouseEvent event) {
        if (this.selectedShape != null) {
            this.ogX = this.selectedShape.getCenter().getX();
            this.ogY = this.selectedShape.getCenter().getY();
            this.initialRotation = this.selectedShape.getRotate();
            if (this.selectedShapeEnum == null) {
                this.initialHeight = this.selectedShape.getHeight();
                this.initialWidth = this.selectedShape.getWidth();
            }
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
                this.isResizing = true;
            } else if(event.isControlDown()) {
                //rotates shape
                this.rotate(this.selectedShape.getCenter(), currLoc);
                this.isRotating = true;
            } else {
                //translates shape
                this.translate(this.selectedShape.getCenter(), currLoc);
                this.isTranslating = true;
            }

        }
    }

    private void released() {
        if(this.selectedShape != null) {
            if(this.translateShape != null && this.isTranslating) {
                this.command = this.translateShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isTranslating = false;
            }
            if(this.resizeShape != null && this.isResizing) {
                this.command = this.resizeShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isResizing = false;
            }
            if(this.rotateShape != null && this.isRotating) {
                this.command = this.rotateShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isResizing = false;
            }
        }
    }

    private void drawShape(MouseEvent event) {
        Commands drawShape = new CreateShape(this.selectedShapeEnum.getShape(), this.colorPicker,
                this.shapes, this.canvasPane, event);
        drawShape.execute();
        this.select(event);
        this.command = drawShape;
        this.undoStack.push(this.command);
        this.redoStack.clear();
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
        Commands drawLine = new DrawLine(x, y, this.curvedLine, this.colorPicker, this.canvasPane);
        drawLine.execute();
        this.command = drawLine;
        this.undoStack.push(this.command);
        this.redoStack.clear();
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
        this.translateShape = new TranslateShape(curr, newCurr, this.selectedShape, this.ogX, this.ogY);
        this.translateShape.execute();
    }

    private void rotate(Point2D curr, Point2D newCurr) {
        this.rotateShape = new RotateShape(this.selectedShape, this.angle, curr, newCurr, this.initialRotation);
        this.rotateShape.execute();
    }

    private void resize(Point2D curr) {
        this.resizeShape = new ResizeShape(this.initialWidth, this.initialHeight, this.selectedShape, curr);
        this.resizeShape.execute();
    }

    private void rotateAndResize(Point2D curr) {
        this.rotate(this.selectedShape.getCenter(), curr);
        this.resize(curr);
    }

    private void raise() {
        if (this.selectedShape != null) {
            Commands raise = new RaiseShape(this.shapes, this.selectedShape, this.canvasPane);
            raise.execute();
            this.command = raise;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    private void lower() {
        if(this.selectedShape != null) {
            Commands lower = new LowerShape(this.shapes, this.selectedShape, this.canvasPane);
            lower.execute();
            this.command = lower;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

}