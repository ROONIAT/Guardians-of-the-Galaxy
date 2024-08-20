package model;

import java.util.Random;

public class Tree extends Obstacle{
    public Tree() {

        super("/Image/tree.png", 50, 0, 0.0, 0, 680);
        setFitWidth(100);
        setFitHeight(100);

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
