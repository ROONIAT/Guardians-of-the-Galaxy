package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;

public class firstMenu extends Application {



    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        MainMenu.stage = stage;

        try {
            URL url = getClass().getResource("/FXML/main.fxml");
            if (url == null) {
                throw new IOException("Cannot find FXML file at /FXML/firstMenu.fxml");
            }

            BorderPane borderPane = FXMLLoader.load(url);
            stage.setScene(new Scene(borderPane));
            stage.setTitle("Guardians of the Galaxy");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void login() throws Exception {
        new LoginMenu().start(MainMenu.stage);
    }

    public void register() throws Exception {
        new RegisterMenu().start(MainMenu.stage);
    }

    public void Guest() throws Exception {
        new GameMenu().start(MainMenu.stage);
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
