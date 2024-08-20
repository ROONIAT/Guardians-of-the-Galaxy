package model;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Explosion extends ImageView {
    public Explosion(double x, double y) {
        Image explosionImage = new Image(getClass().getResourceAsStream("/Image/14.png"));
        this.setImage(explosionImage);
        this.setX(x);
        this.setY(y);

        this.setFitWidth(100);
        this.setFitHeight(100);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> ((Pane)this.getParent()).getChildren().remove(this));
        fadeOut.play();
    }
}
