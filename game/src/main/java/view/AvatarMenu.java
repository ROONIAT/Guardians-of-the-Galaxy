package view;
import javafx.scene.input.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.MouseEvent;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AvatarMenu extends Application {

    @FXML
    private ImageView avatarImageView;
    @FXML
    private FlowPane avatarFlowPane;

    private List<Image> avatars;

    public void start(Stage stage) throws Exception{

        URL url = ProfileMenu.class.getResource("/FXML/avatar.fxml");
        BorderPane pane = FXMLLoader.load(Objects.requireNonNull(url));
        stage.setScene(new Scene(pane));
        stage.setTitle("Avatar Menu");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void initialize() {

        avatars = new ArrayList<>();

        for (int i = 0; i <= 7; i++) {
            String imagePath = getClass().getResource("/Image/avatar" + i + ".png").toExternalForm();
            avatars.add(new Image(imagePath));
        }
        for (Image avatar : avatars) {
            ImageView imageView = new ImageView(avatar);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setOnMouseClicked(event -> handleAvatarSelection(avatar));
            avatarFlowPane.getChildren().add(imageView);
        }

        User loggedInUser = User.getLoggedInUser();
        if (loggedInUser != null && loggedInUser.getAvatar() != null) {
            avatarImageView.setImage(loggedInUser.getAvatar());
        }

        avatarImageView.setOnDragOver(this::handleDragOver);
        avatarImageView.setOnDragDropped(this::handleDragDropped);

    }

    private void handleDragOver(DragEvent event) {

        if (event.getGestureSource() != avatarImageView && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {

        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String filePath = db.getFiles().get(0).toURI().toString();
            Image customImage = new Image(filePath);
            avatarImageView.setImage(customImage);
            User.getLoggedInUser().setAvatar(customImage);
        }
        event.setDropCompleted(success);
        event.consume();

    }

    private void handleAvatarSelection(Image avatar) {
          avatarImageView.setImage(avatar);
        User.getLoggedInUser().setAvatar(avatar);
    }


    @FXML
    private void handleSelectCustomFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Avatar Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image customImage = new Image(file.toURI().toString());
            avatarImageView.setImage(customImage);
            User.getLoggedInUser().setAvatar(customImage);
        }
    }

    @FXML

    public void backtoprofileMenu(MouseEvent mouseEvent) throws IOException {
        new ProfileMenu().start(MainMenu.stage);
    }
}
