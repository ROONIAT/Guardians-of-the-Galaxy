package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class ClusterBomb extends ImageView {
    private static final int BOMB_SIZE = 20;
    private static final int BOMB_SPEED = 3;
    private Pane root;
    private boolean exploded;

    public ClusterBomb(double x, double y, Pane root) {
        Image bombImage = new Image(getClass().getResourceAsStream("/Image/arrow.png"));
        setImage(bombImage);
        setFitWidth(BOMB_SIZE);
        setFitHeight(BOMB_SIZE);
        setLayoutX(x);
        setLayoutY(y);
        this.root = root;
        this.exploded = false;
    }

    public void move() {
        setLayoutY(getLayoutY() + BOMB_SPEED);
    }

    public void explode(List<ClusterBullet> clusterBullets) {
        if (!exploded) {
            exploded = true;
            double[] angles = {135, 112.5, 90, 67.5, 45};
            for (double angle : angles) {
                double radian = Math.toRadians(angle);
                double dx = 5 * Math.cos(radian);
                double dy = 5 * Math.sin(radian);
                ClusterBullet bullet = new ClusterBullet(getLayoutX(), getLayoutY(), dx, dy);
                clusterBullets.add(bullet);
                root.getChildren().add(bullet);
            }
        }
    }

    public boolean isExploded() {
        return exploded;
    }
}
