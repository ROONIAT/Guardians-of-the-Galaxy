package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ProfileMenu extends Application {

    public TextField usernameField;
    public TextField passwordField;


    public void start(Stage stage) throws IOException {
        URL url = ProfileMenu.class.getResource("/FXML/profile.fxml");
        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(url));
        stage.setScene(new Scene(pane));
        stage.show();
    }


    public void handleDeleteAccount(MouseEvent mouseEvent) throws Exception {
        User.deleteAccount();
        new firstMenu().start(MainMenu.stage);
    }

    public void handleLogout(MouseEvent mouseEvent) throws Exception {
        User.logout();
        new firstMenu().start(MainMenu.stage);
    }

    public void handleAvatarMenu(MouseEvent mouseEvent) throws Exception {
        new AvatarMenu().start(MainMenu.stage);
    }

    public void handleChangeUsername(MouseEvent mouseEvent) {
        User.changeUsername(usernameField.getText());
    }

    public void handleChangePassword(MouseEvent mouseEvent) {
        User.changePassword(passwordField.getText());
    }

    public void backToinfoMenu(MouseEvent mouseEvent) throws Exception {
        new infoMenu().start(MainMenu.stage);
    }
}
