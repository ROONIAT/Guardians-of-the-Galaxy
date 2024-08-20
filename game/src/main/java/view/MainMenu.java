package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainMenu extends Application {

    public static Stage stage;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainMenu.stage = stage;

        try {
            URL url = MainMenu.class.getResource("/FXML/main.fxml");
            if (url == null) {
                throw new IllegalStateException("Cannot find FXML file");
            }
            BorderPane borderpane = FXMLLoader.load(url);
            Scene scene = new Scene(borderpane);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}