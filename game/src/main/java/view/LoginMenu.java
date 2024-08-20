package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;

public class LoginMenu extends Application {
    public TextField username;
    public PasswordField password;


    @Override
    public void start(Stage stage) throws Exception {


        try {
            URL url = getClass().getResource("/FXML/login.fxml");
            if (url == null) {
                throw new IOException("Cannot find FXML file at /FXML/firstMenu.fxml");
            }

            BorderPane borderPane = FXMLLoader.load(url);
            stage.setScene(new Scene(borderPane));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new firstMenu().start(MainMenu.stage);

    }

    public void login(MouseEvent mouseEvent) throws Exception {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't login");
            alert.setContentText("please fill all the fields.");
            alert.show();
        } else if (!doesUserExist(username.getText())) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't login");
            alert.setContentText("username doesn't exist.");
            alert.show();

        } else if (!User.getUserByUsername(username.getText()).getPassword().equals(password.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't login");
            alert.setContentText("Password is incorrect");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("login successfully");
            alert.setContentText("you can now play the game.");
            alert.showAndWait();
            User.setLoggedInUser(User.getUserByUsername(username.getText()));
            new infoMenu().start(MainMenu.stage);
        }
    }

    private boolean doesUserExist(String text) {

        for (User user : User.getAllUsers()) {
            if (user.getUsername().equals(text)) {
                return true;
            }
        }
        return false;
    }
}
