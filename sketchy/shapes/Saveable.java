package sketchy.shapes;

import javafx.scene.layout.Pane;

/**
 * Saveable is an interface which encapsulates the shapes and line
 */
public interface Saveable {
    public void delete(Pane canvasPane);
    public String toString();
}
