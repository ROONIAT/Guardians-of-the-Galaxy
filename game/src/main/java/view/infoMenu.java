package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import model.Game;
import model.User;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;

public class infoMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        

        try {
            URL url = infoMenu.class.getResource("/FXML/info.fxml");
            BorderPane root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            initializeUserInfo(root);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void initializeUserInfo(BorderPane root) {

        User loggedInUser = User.getLoggedInUser();
        if (loggedInUser != null) {
            Text usernameText = (Text) root.lookup("#usernameText");
            ImageView avatarImageView = (ImageView) root.lookup("#avatarImageView");
            usernameText.setText(loggedInUser.getUsername());
            Image avatarImage = loggedInUser.getAvatar();
            if (avatarImage != null) {
                avatarImageView.setImage(avatarImage);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void continueGame(MouseEvent mouseEvent) {
        Game game = User.getLoggedInUser().getLastGame();
        if (game != null) {
            try {
                new GameMenu().start(MainMenu.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void startGame(MouseEvent mouseEvent) throws Exception {
        Game game = new Game();
        User.getLoggedInUser().addGame(game);
        new GameMenu().start(MainMenu.stage);
    }

    public void goToProfileMenu(MouseEvent mouseEvent) throws IOException {
        new ProfileMenu().start(MainMenu.stage);

    }

    public void goToScoresChart(MouseEvent mouseEvent) throws Exception {
        new ScoresChart().start(MainMenu.stage);
    }

    public void goToSetting(MouseEvent mouseEvent) {
        new SettingMenu().start(MainMenu.stage);
    }

    public void logout(MouseEvent mouseEvent) throws Exception {
        User.logout();
        new MainMenu().start(MainMenu.stage);
    }
}
