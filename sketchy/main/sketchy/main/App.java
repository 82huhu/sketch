package sketchy.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * It's time for Sketchy! This is the App class to get things started.
 *
 * Your job is to fill in the start method!
 *
 * Class comments here...
 */


public class App extends Application {

  @Override
  public void start(Stage stage) {
    PaneOrganizer organizer = new PaneOrganizer();
    Scene scene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    stage.setScene(scene);
    stage.setTitle("Sketchy");
    stage.show();
  }

  public static void main(String[] argv) {
    // launch is a method inherited from Application
    launch(argv);
  }
}
