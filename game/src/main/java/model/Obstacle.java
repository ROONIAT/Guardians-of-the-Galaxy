package model;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public abstract class Obstacle extends ImageView {


    protected int health;
    protected int marks;
    protected double speed;
    protected double x;
    protected double y;

    public Obstacle(String imagePath ,int health, int marks, double speed, double x, double y) {
        super(new Image(Obstacle.class.getResourceAsStream(imagePath)));
        this.health = health;
        this.marks = marks;
        this.speed = speed;
        this.x = x;
        this.y = y;
        setLayoutX(x);
        setLayoutY(y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            giveMarks();
        }
    }

    public void setXCoordinate(double newX) {
        this.x = newX;
        setLayoutX(newX);
    }
    public int getMarks() {
        return marks;
    }

    public abstract int giveMarks();

    public void move() {
        setLayoutX(getLayoutX() + speed);
    }

}
