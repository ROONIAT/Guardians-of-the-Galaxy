package view;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;

import model.*;

public class SettingMenu extends Application {

    public void start(Stage stage) {

        try {
            URL url = infoMenu.class.getResource("/FXML/setting.fxml");
            BorderPane root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleMute() {

    }

    @FXML
    public void handleBlackWhite() {

    }

    @FXML
    public void handleCustomControls() {
    }

    @FXML
    public void handleBackToinfoMenu(MouseEvent mouseEvent) throws Exception {
        new infoMenu().start(MainMenu.stage);
    }


    @FXML
    public void GameDifficulty1(MouseEvent mouseEvent) {
        GameMenuController.gameDifficulty = 1;
        Tank.TANK_SPEED = 1;
        Tank.DETECTION_RADIUS = 100;
        MiG.MIG_RADIUS = 50;
        GameMenuController.MigspawnTime = 24;
    }

    @FXML
    public void GameDifficulty2(MouseEvent mouseEvent) {
        GameMenuController.gameDifficulty = 2;
        Tank.TANK_SPEED = 2;
        Tank.DETECTION_RADIUS = 200;
        MiG.MIG_RADIUS = 100;
        GameMenuController.MigspawnTime = 18;
    }

    @FXML
    public void GameDifficulty3(MouseEvent mouseEvent) {
        GameMenuController.gameDifficulty = 3;
        Tank.TANK_SPEED = 3;
        Tank.DETECTION_RADIUS = 300;
        MiG.MIG_RADIUS = 150;
        GameMenuController.MigspawnTime = 12;
    }
}
