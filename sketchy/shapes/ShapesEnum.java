package sketchy.shapes;

/**
 * ShapesEnum defines my Ellipse and Rectangle Enums
 */
public enum ShapesEnum {
    ELLIPSE,
    RECTANGLE;

    public Selectable getShape() {
        switch (this) {
            case ELLIPSE:
                return new MyEllipse();
            case RECTANGLE:
                return new MyRectangle();
            default:
                return null;
        }
    }
}


