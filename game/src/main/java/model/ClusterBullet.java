package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClusterBullet extends ImageView {
    private static final int BULLET_SIZE = 10;
    private double dx;
    private double dy;

    public ClusterBullet(double x, double y, double dx, double dy) {
        Image bulletImage = new Image(getClass().getResourceAsStream("/Image/arrow.png"));
        setImage(bulletImage);
        setFitWidth(BULLET_SIZE);
        setFitHeight(BULLET_SIZE);
        setLayoutX(x);
        setLayoutY(y);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        setLayoutX(getLayoutX() + dx);
        setLayoutY(getLayoutY() + dy);
    }
}
