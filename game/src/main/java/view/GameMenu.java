package view;

import controller.GameMenuController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMenu extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        Scene scene = new Scene(root);


        Image backgroundImage = new Image(getClass().getResourceAsStream("/Image/gameBackground.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(scene.widthProperty());
        backgroundImageView.fitHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(backgroundImageView);


        GameMenuController controller = new GameMenuController(root);


        scene.setOnKeyPressed(event -> controller.handleKeyPress(event.getCode()));

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        controller.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
