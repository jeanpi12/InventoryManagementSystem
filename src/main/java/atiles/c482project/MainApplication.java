package atiles.c482project;

import Controller.MainFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The MainApplication class serves as the entry point for the application.
 * It initializes the JavaFX framework, loads the main form, and sets up the scene and stage.
 * Javadoc documentation can be found in the 'docs' folder.
 */
public class MainApplication extends Application {

    /**
     * The start() method is called when the JavaFX application is launched.
     * It loads the main form, sets up the scene and stage, and displays the application window.
     *
     * @param primaryStage The primary stage for the application.
     * @throws IOException If an I/O error occurs during the loading of the main form.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("MainForm.fxml"));
        Parent root = loader.load();

        // Get the controller instance
        MainFormController controller = loader.getController();

        // Create the scene and set it on the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Customize the stage properties if needed
        primaryStage.show();
    }

    /**
     * The main() method is the entry point of the Java application.
     * It launches the JavaFX application by calling the start() method.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
