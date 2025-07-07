package app;

import com.technoearth.database.DBInitializer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloFX extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("🎉 JavaFX is working!");
        Scene scene = new Scene(label, 300, 200);
        stage.setTitle("Test JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DBInitializer.createTables(); // run only once
        Application.launch(args);     // launches JavaFX
    }
}
