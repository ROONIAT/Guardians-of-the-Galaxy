package model;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Tank extends ImageView {
    private static final int TANK_SIZE = 50;
    public static int TANK_SPEED = 1;
    public static int DETECTION_RADIUS = 200; // Radius of detection circle
    private Pane root;
    private boolean movingRight;
    private List<TankBullet> bullets;
    private Circle detectionCircle;

    public Tank(double x, double y, Pane root) {
        Image tankImage = new Image(getClass().getResourceAsStream("/Image/Satellite.png"));
        setImage(tankImage);
        setFitWidth(TANK_SIZE);
        setFitHeight(TANK_SIZE);
        setLayoutX(x);
        setLayoutY(y);
        this.root = root;
        this.movingRight = true;
        this.bullets = new ArrayList<>();


        this.detectionCircle = new Circle();
        detectionCircle.setRadius(DETECTION_RADIUS);
        detectionCircle.setFill(null);
        detectionCircle.setStroke(null);
        root.getChildren().add(detectionCircle);
    }

    public void setSpeed(int speed) {
        this.TANK_SPEED = speed;
    }

    public void move() {
        setLayoutX(getLayoutX() + 1);
        detectionCircle.setCenterX(getLayoutX() + TANK_SIZE / 2);
        detectionCircle.setCenterY(getLayoutY() + TANK_SIZE / 2);
    }

    public void shootIfPlaneInRange(double planeX, double planeY, List<TankBullet> tankBullets, Pane root) {
        double distance = Math.sqrt(Math.pow((planeX - (getLayoutX() + TANK_SIZE / 2)), 2) +
                Math.pow((planeY - (getLayoutY() + TANK_SIZE / 2)), 2));
        if (distance <= DETECTION_RADIUS) {
            shoot(tankBullets, root);
        }
    }

    private void shoot(List<TankBullet> tankBullets, Pane root) {
        double[] angles = {0, 30, 60, 90};
        for (double angle : angles) {
            double radian = Math.toRadians(angle);
            double dx = 5 * Math.cos(radian);
            double dy = 5 * Math.sin(radian);
            TankBullet bullet = new TankBullet(getLayoutX() + TANK_SIZE / 2, getLayoutY() + TANK_SIZE / 2, dx, dy);
            tankBullets.add(bullet);
            root.getChildren().add(bullet);
        }
    }

    public List<TankBullet> getBullets() {
        return bullets;
    }


}
