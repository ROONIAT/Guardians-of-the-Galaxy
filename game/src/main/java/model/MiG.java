package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MiG extends ImageView {
    private static final int MIG_SPEED = 5;
    private static final int MIG_SIZE = 75;
    public static int MIG_RADIUS = 200;

    private Circle radiusCircle;

    public MiG(double x, double y) {
        Image migImage = new Image(getClass().getResourceAsStream("/Image/Dolphin.png"));
        setImage(migImage);
        setFitWidth(MIG_SIZE);
        setFitHeight(MIG_SIZE);
        setLayoutX(x);
        setLayoutY(y);

        radiusCircle = new Circle(MIG_RADIUS);
        radiusCircle.setStroke(Color.RED);
        radiusCircle.setFill(Color.TRANSPARENT);
        radiusCircle.setCenterX(x + MIG_SIZE / 2);
        radiusCircle.setCenterY(y + MIG_SIZE / 2);
    }

    public void move() {
        setLayoutX(getLayoutX() - MIG_SPEED);
        radiusCircle.setCenterX(getLayoutX() + MIG_SIZE / 2);
        radiusCircle.setCenterY(getLayoutY() + MIG_SIZE / 2);
    }

    public Circle getRadiusCircle() {
        return radiusCircle;
    }

    public double getRadius() {
        return MIG_RADIUS;
    }
}

