package model;

import java.util.Random;

public class Building extends Obstacle {

    public Building() {
        super("/Image/building.jpg", 80, 15, 0, 0, 680);
        setFitWidth(75);
        setFitHeight(75);
        changeXCoordinate(generateRandomX());
    }

    private void changeXCoordinate(double newX) {

        x = newX;
        setLayoutX(x);
    }

    private double generateRandomX() {

        Random random = new Random();
        return 100 + (1100 - 100) * random.nextDouble();
    }

    @Override
    public int giveMarks() {
        return marks;
    }

}
