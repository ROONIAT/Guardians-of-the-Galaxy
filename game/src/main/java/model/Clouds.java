package model;

public class Clouds extends Obstacle{

    public Clouds() {
        super("/Image/cloud.png", 100, 20, 1.0, 0, 680);
        setFitWidth(75);
        setFitHeight(75);
    }

    @Override
    public int giveMarks() {
        return marks;
    }

}
