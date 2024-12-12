package sketchy.main;

import cs15.fnl.sketchySupport.CS15FileIO;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import sketchy.commands.*;
import sketchy.shapes.*;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Canvas is the top level logic class. In this class, I create and give function to the
 * radio buttons (select shape, draw with pen, draw with lasso, draw rectangle, and draw
 * ellipse), the color picker, and buttons (fill, delete, raise, lower, undo, redo, save,
 * and reload).
 */
public class Canvas {
    private Pane canvasPane;
    private ShapesEnum selectedShapeEnum;
    private boolean isDrawLine;
    private CurvedLine curvedLine;
    private boolean isLasso;
    private ColorPicker colorPicker;
    private boolean isSelecting;
    private ArrayList<Selectable> shapes;
    private ArrayList<Saveable> saveables;
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

    /**
     * The Canvas() constructor takes in my canvasPane and controlPane. It calls helper methods
     * to set up my control pane's characteristics, radio buttons, color picker, and buttons.
     * It also calls methods to set up actions when mouse is pressed, dragged, and released.
     */
    public Canvas(Pane canvasPane, VBox controlPane) {
        this.isDrawLine = false;
        this.isLasso = false;
        this.isSelecting = false;
        this.creatingShape = false;
        this.angle = 0;
        this.shapes = new ArrayList<>();
        this.saveables = new ArrayList<>();
        this.canvasPane = canvasPane;
        this.command = null;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.isTranslating = false;
        this.isResizing = false;
        this.isRotating = false;

        this.setUpControlPane(controlPane);
        this.setUpRadioButtons(controlPane);
        this.setColorPicker(controlPane);
        this.setUpButtons(controlPane);

        this.canvasPane.setOnMousePressed((MouseEvent e) -> this.click(e));
        this.canvasPane.setOnMouseDragged((MouseEvent e) -> this.drag(e));
        this.canvasPane.setOnMouseReleased((MouseEvent e) -> this.released());
        this.canvasPane.setFocusTraversable(false);
    }

    /**
     * setUpControlPane() sets up controlPane's characteristics (color, alignment, spacing, etc.)
     */
    private void setUpControlPane(VBox controlPane) {
        controlPane.setStyle("-fx-background-color: orange;");
        this.canvasPane.setStyle("-fx-background-color: skyblue;");
        controlPane.setPrefWidth(Constants.CONTROL_WIDTH);
        controlPane.setAlignment(Pos.CENTER);
        controlPane.setSpacing(Constants.CONTROL_SPACING);
    }

    /**
     * setUpRadioButtons() creates the five radio buttons.
     */
    private void setUpRadioButtons(VBox controlPane) {
        Label drawingOptionsLabel = new Label("Drawing Options");
        controlPane.getChildren().add(drawingOptionsLabel);
        ToggleGroup drawingOptionsGroup = new ToggleGroup();

        RadioButton selectShape = new RadioButton("select shape");
        selectShape.setSelected(false);
        controlPane.getChildren().add(selectShape);
        selectShape.setToggleGroup(drawingOptionsGroup);
        selectShape.setOnAction((ActionEvent e)-> {this.isDrawLine = false;
            this.creatingShape = false; this.isSelecting = true;});
        //by toggling the applicable booleans when a radiobutton is clicked,
        //drag() and click() know case to play.

        RadioButton drawWithPen = new RadioButton("draw with pen");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawWithPen);
        drawWithPen.setToggleGroup(drawingOptionsGroup);
        drawWithPen.setOnAction(ActionEvent -> {
            this.isSelecting = false; this.selectedShapeEnum = null;
            this.isLasso = false; this.creatingShape = false; this.isDrawLine = true;
            this.isTranslating = false;});

        RadioButton lassoDraw = new RadioButton("draw with lasso");
        selectShape.setSelected(false);
        controlPane.getChildren().add(lassoDraw);
        lassoDraw.setToggleGroup(drawingOptionsGroup);
        lassoDraw.setOnAction(ActionEvent -> {this.isSelecting = false;
            this.selectedShapeEnum = null; this.isLasso = true;
            this.creatingShape = false; this.isDrawLine = true;
            this.isTranslating = false;});

        RadioButton drawRect = new RadioButton("draw rectangle");
        drawRect.setToggleGroup(drawingOptionsGroup);
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawRect);
        drawRect.setOnAction(ActionEvent -> {this.isSelecting = false; this.creatingShape = true;
            this.chooseShapeDraw(ShapesEnum.RECTANGLE);});
        //the shapesEnum indicates which shape to draw

        RadioButton drawEllipse = new RadioButton("draw ellipse");
        selectShape.setSelected(false);
        controlPane.getChildren().add(drawEllipse);
        drawEllipse.setToggleGroup(drawingOptionsGroup);
        drawEllipse.setOnAction(ActionEvent -> {this.isSelecting = false;
            this.creatingShape = true;
            this.chooseShapeDraw(ShapesEnum.ELLIPSE);});

    }

    /**
     * setColorPicker() creates a color picker and adds it to the controlPane.
     */
    private void setColorPicker(VBox controlPane) {
        Label setColorLabel= new Label("Set Color");
        controlPane.getChildren().add(setColorLabel);

        this.colorPicker = new ColorPicker();
        controlPane.getChildren().add(this.colorPicker);
    }

    /**
     * setUpButtons() creates and calls helper methods to add functionality to
     * all the buttons.
     */
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
        save.setOnAction((ActionEvent e) -> this.save());

        Button load = new Button("load");
        controlPane.getChildren().add(load);
        load.setOnAction((ActionEvent e) -> this.load());

    }

    /**
     * fillAction() is a helper method for the Fill button. If there is a shape selected, a new
     * fill command is created and executed. That command is added to the undoStack and the
     * redoStack is cleared.
     */
    private void fillAction() {
        if(this.selectedShape != null) {
            Commands fill = new FillCommand(this.selectedShape, this.colorPicker);
            fill.execute();
            this.command = fill;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    /**
     * deleteAction() is a helper method for the Delete button. If a shape is selected a new
     * DeleteShapeCommand command is created and executed. That command is added to the undoStack and
     * the redoStack is cleared.
     */
    private void deleteAction() {
        if(this.selectedShape != null) {
            Commands delete = new DeleteShapeCommand(this.selectedShape, this.shapes, this.saveables,
                    this.canvasPane);
            delete.execute();
            this.command = delete;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    /**
     * undoAction() is a helper method for the undo button. If undoStack is not empty, the
     * most recent command is popped off the undo stack onto the redo stack. That command's
     * undo method is called.
     */
    private void undoAction() {
        if(!this.undoStack.isEmpty()) {
            Commands command = this.undoStack.pop();
            this.redoStack.push(command);
            command.undo();
        }
    }

    /**
     * redoAction() is a helper method for the redo button. If redoStack is not empty, the
     * most recent command is popped off the redo stack onto the undo stack. That command's
     * redo method is called.
     */
    private void redoAction() {
        if(!this.redoStack.isEmpty()) {
            Commands command = this.redoStack.pop();
            this.undoStack.push(command);
            command.redo();
        }
    }

    /**
     * click() is a helper called every time the mouse is clicked. It contains functionality for
     * drawing lines, drawing shapes, and selecting.
     */
    private void click(MouseEvent event) {
        //selecting
        if (this.isSelecting) {
            this.select(event);
        }

        //instantiate initial x, y, rotation, height, and width for resize, rotate, and translate
        //commands' use.
        if (this.selectedShape != null) {
            this.ogX = this.selectedShape.getCenter().getX();
            this.ogY = this.selectedShape.getCenter().getY();
            this.initialRotation = this.selectedShape.getRotate();

            if (this.selectedShapeEnum == null && this.isResizing) {
                this.initialHeight = this.selectedShape.getHeight();
                this.initialWidth = this.selectedShape.getWidth();
            }
        }

            //drawing line
        if (this.isDrawLine) { //booleans defined by radiobuttons
            this.deselect();
            this.createLine(event.getX(), event.getY());
        }

        //drawing shapes
        if (this.selectedShapeEnum != null && !this.isSelecting) {
            this.drawShape(event);
        }
    }

    /**
     * drag() is a helper method which is called when the mouse is dragged. It contains
     * functionality for rotate, resize, simultaneous rotate and resize, and translate.
     */
    private void drag(MouseEvent event) {
        this.penDrawOnDrag(event.getX(),event.getY());

        Point2D currLoc = new Point2D(event.getX(), event.getY());
        if(this.selectedShape != null) {
            if(event.isShiftDown() && event.isControlDown()) {
                //rotate and resize at the same time
                this.rotateAndResize(currLoc);
                this.isResizing = true;
                this.isRotating = true;

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

    /**
     * released() is as helper method which is called when mouse click is released. It contains
     * functionality for pushing translate, resize, and rotate commands into the undoStack.
     */
    private void released() {
        if(this.selectedShape != null) {
            if(this.translateShape != null && this.isTranslating) {
                this.command = this.translateShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isTranslating = false;
            }

            if(this.resizeShape != null && this.isResizing && !this.creatingShape) {
                this.command = this.resizeShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isResizing = false;
            }

            if(this.rotateShape != null && this.isRotating) {
                this.command = this.rotateShape;
                this.undoStack.push(this.command);
                this.redoStack.clear();
                this.isRotating = false;
            }
        }
    }

    /**
     * drawShape() is a helper method called when the mouse is clicked that draws a shape.
     * It creates and executes a DrawShapeCommand command and pushes that command onto the undoStack.
     * It clears the redoStack.
     */
    private void drawShape(MouseEvent event) {
        this.deselect();
        Commands drawShape = new DrawShapeCommand(this.selectedShapeEnum.getShape(), this.colorPicker,
                this.shapes, this.saveables, this.canvasPane, event);
        drawShape.execute();
        this.select(event); //automatically select newly created shape
        this.command = drawShape;
        this.undoStack.push(this.command);
        this.redoStack.clear();
    }

    /**
     * select() is as helper method called on mouse click. It goes through
     * the shapes array and deselects shapes starting from the end. Then, it sets
     * the shape clicked by the mouse to the selected shape + highlights it
     * graphically. This makes it so only one shape is selected, and if several are
     * stacked the top one is selected.
     */
    private void select(MouseEvent event) {
        for (int i = this.shapes.size()-1; i >=0; i--) {
            Selectable shape = this.shapes.get(i);
            //deselects all shapes except the most recently clicked shape
            this.deselect();
            if (shape.isSelected(event.getX(), event.getY())) {
                this.selectedShape = shape;
                this.selectedShape.setStroke(Color.LAWNGREEN);
                this.selectedShape.setStrokeWidth(2.5);
                return;
            }

        }
    }

    /**
     * deselect() is a helper which deselects the selected shape both logically and
     * graphically
     */
    private void deselect() {
        if(this.selectedShape != null) {
            this.selectedShape.setStroke(Color.TRANSPARENT);
            this.selectedShape = null;
        }
    }

    /**
     * createLine() is a helper method called on mouse click which creates the first point
     * in drawing a line. It creates and executes a DrawLineCommand command, pushes that command
     * onto the undoStack, and clears the redoStack.
     */
    private void createLine(double x, double y){
        this.curvedLine = new CurvedLine(this.colorPicker.getValue());
        Commands drawLine = new DrawLineCommand(x, y, this.saveables, this.curvedLine, this.canvasPane);
        drawLine.execute();
        this.command = drawLine;
        this.undoStack.push(this.command);
        this.redoStack.clear();
    }

    /**
     * penDrawOnDrag() is a helper method called on mouse drag which adds points to the original
     * DrawLineCommand.
     */
    private void penDrawOnDrag(double x, double y) {
        if(this.curvedLine !=null && this.isDrawLine){
            if(this.isLasso) {
                this.curvedLine.setFill(this.colorPicker.getValue());
            }
            this.curvedLine.addPoint(x, y);
        }
    }

    /**
     * chooseShapeDraw() is a helper method called when the radio button for ellipse
     * or square is clicked. It makes it so the next click will draw the inputted shape.
     */
    private void chooseShapeDraw(ShapesEnum shape) {
        this.isDrawLine = false;
        this.selectedShapeEnum = shape;
    }

    /**
     * translate is a helper method called on mouse drag which moves the selected shape with the mouse.
     * It creates and executes a new TranslateCommand command.
     */
    private void translate(Point2D curr, Point2D newCurr) {
        this.translateShape = new TranslateCommand(curr, newCurr, this.selectedShape, this.ogX, this.ogY);
        this.translateShape.execute();
    }

    /**
     * rotate is a helper method called on mouse drag which rotates the selected shape with the mouse.
     * It creates and executes a new RotateCommand command.
     */
    private void rotate(Point2D curr, Point2D newCurr) {
        this.rotateShape = new RotateCommand(this.selectedShape, this.angle, curr, newCurr, this.initialRotation);
        this.rotateShape.execute();
    }

    /**
     * resize is a helper method called on mouse drag which resizes the selected shape with the mouse.
     * It creates and executes a new ResizeCommand command.
     */
    private void resize(Point2D curr) {
        this.resizeShape = new ResizeCommand(this.initialWidth, this.initialHeight,
                this.selectedShape, curr);
        this.resizeShape.execute();
    }

    /**
     * rotateAndResize is a helper method called when both ctrl and shift are held down. This makes
     * makes it so users can rotate and resize at the same time.
     */
    private void rotateAndResize(Point2D curr) {
        this.rotate(this.selectedShape.getCenter(), curr);
        this.resize(curr);
    }

    /**
     * raise() is a helper method called on Raise button click which raises a selected shape
     * up a layer. It creates and executes a RaiseCommand command, pushes that command onto
     * the undoStack, and clears the redoStack.
     */
    private void raise() {
        if (this.selectedShape != null) {
            Commands raise = new RaiseCommand(this.shapes, this.saveables, this.selectedShape, this.canvasPane);
            raise.execute();
            this.command = raise;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    /**
     * lower() is a helper method called on Lower button click which raises a selected shape
     * down a layer. It creates and executes a RaiseCommand command, pushes that command onto
     * the undoStack, and clears the redoStack.
     */
    private void lower() {
        if(this.selectedShape != null) {
            Commands lower = new LowerCommand(this.shapes, this.saveables, this.selectedShape, this.canvasPane);
            lower.execute();
            this.command = lower;
            this.undoStack.push(this.command);
            this.redoStack.clear();
        }
    }

    /**
     * save() writes a text file with each line and shape's attributes, which is saved
     * on the computer.
     */
    public void save() {
        CS15FileIO file = new CS15FileIO();
        Window stage = this.canvasPane.getScene().getWindow();
        String fileName = file.getFileName(true, stage);

        if(fileName == null) {
            return;
        }

        file.openWrite(fileName);
        for(Saveable saveable: this.saveables) {
            file.writeString(saveable.toString());
        }
        file.closeWrite();
    }

    /**
     * load() loads saved files so the text files saved in save() are translated back
     * into the images.
     */
    public void load() {
        CS15FileIO file = new CS15FileIO();
        Window stage = this.canvasPane.getScene().getWindow();
        String fileName = file.getFileName(false, stage);

        if(fileName == null) {
            return;
        }

        //clears the saveable + shape ArrayLists, the canvasPane, and the undo/redo stacks.
        if(this.saveables != null) {
            this.saveables.clear();
        }
        if(this.shapes != null) {
            this.shapes.clear();
        }
        this.canvasPane.getChildren().clear();
        if(this.undoStack != null) {
            this.undoStack.clear();
        }
        if(this.redoStack != null) {
            this.redoStack.clear();
        }

        file.openRead(fileName);

        while(file.hasMoreData()) {
            String saveableType = file.readString();

            switch(saveableType) {
                case "line":
                    int red = file.readInt();
                    int green = file.readInt();
                    int blue = file.readInt();

                    CurvedLine line = new CurvedLine(Color.rgb(red, green, blue));
                    this.saveables.add(line);
                    line.draw(this.canvasPane);
                    break;
                case "ellipse":
                    red = file.readInt();
                    green = file.readInt();
                    blue = file.readInt();
                    double centerX = file.readDouble();
                    double centerY = file.readDouble();
                    double width = file.readDouble();
                    double height = file.readDouble();
                    double angle = file.readDouble();
                    MyEllipse ellipse = new MyEllipse();
                    ellipse.setCenter(centerX, centerY);
                    ellipse.setWidth(width);
                    ellipse.setHeight(height);
                    ellipse.changeColor(Color.rgb(red, green, blue));
                    ellipse.setRotate(angle);
                    this.saveables.add(ellipse);
                    this.shapes.add(ellipse);
                    ellipse.draw(this.canvasPane);
                    break;
                case "rectangle":
                    red = file.readInt();
                    green = file.readInt();
                    blue = file.readInt();
                    double rectX = file.readDouble();
                    double rectY = file.readDouble();
                    double rectWidth = file.readDouble();
                    double rectHeight = file.readDouble();
                    double rectAngle = file.readDouble();
                    MyRectangle rect = new MyRectangle();
                    rect.setLocation(rectX, rectY);
                    rect.setWidth(rectWidth);
                    rect.setHeight(rectHeight);
                    rect.changeColor(Color.rgb(red, green, blue));
                    rect.setRotate(rectAngle);
                    this.saveables.add(rect);
                    this.shapes.add(rect);
                    rect.draw(this.canvasPane);
                    break;
                default:
                    file.closeRead();
                    break;
            }
        }
    }
}