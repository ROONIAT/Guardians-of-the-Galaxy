package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TankBullet extends Circle {
    private static final int BULLET_RADIUS = 5;
    private double dx;
    private double dy;

    public TankBullet(double x, double y, double dx, double dy) {
        super(x, y, BULLET_RADIUS);
        this.dx = dx;
        this.dy = dy;
        setFill(Color.RED);
    }

    public void move() {
        setLayoutX(getLayoutX() + dx);
        setLayoutY(getLayoutY() + dy);
    }
}
