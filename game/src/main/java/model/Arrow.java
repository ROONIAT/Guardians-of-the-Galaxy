package model;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Arrow extends ImageView {

    private static final double ARROW_SPEED = 5;

    public Arrow(double x, double y) {
        Image arrowImage = new Image(getClass().getResourceAsStream("/Image/arrow.png"));
        this.setImage(arrowImage);
        this.setFitWidth(10);
        this.setFitHeight(20);
        this.setLayoutX(x);
        this.setLayoutY(y);
    }

    public void move() {
        this.setLayoutY(this.getLayoutY() + ARROW_SPEED);
    }
}
