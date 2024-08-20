package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class RegisterMenu extends Application {
    public TextField username;
    public TextField password;
    public TextField passConfirm;


    @Override
    public void start(Stage stage) throws Exception {

        URL url = RegisterMenu.class.getResource("/FXML/Register.fxml");
        BorderPane borderPane = FXMLLoader.load(url);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }


    public void back() throws Exception {
        new firstMenu().start(MainMenu.stage);
    }

    public void register() throws Exception {

        String usernamePattern = "^[a-zA-Z0-9.]{3,}$";
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,}$";

        if (username.getText().isEmpty() || password.getText().isEmpty() || passConfirm.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't register");
            alert.setHeaderText("please fill all the fields.");
            alert.show();
        } else if (!password.getText().equals(passConfirm.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't register");
            alert.setContentText("passwords don't match.");
            alert.show();
        } else if (!username.getText().matches(usernamePattern)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't register");
            alert.setContentText("username at least 3 characters long and can only contain letters, numbers and dots.");
            alert.show();
        } else if (!password.getText().matches(passwordPattern)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't register");
            alert.setContentText("password must be at least 6 characters long and contain at least one letter and one digit.");
            alert.show();
        } else if (doesUserExist(username.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("can't register");
            alert.setContentText("username already exists.");
            alert.show();
        } else {

            User user = new User(username.getText(), password.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("registered successfully");
            alert.setContentText("you can login now.");
            alert.showAndWait();
            new firstMenu().start(MainMenu.stage);
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