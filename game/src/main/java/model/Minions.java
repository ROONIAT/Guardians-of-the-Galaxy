package model;

public class Minions extends Obstacle{
    public Minions() {
        super("/Image/minion.png", 120, 25, 0.8, 0, 680);
        setFitWidth(50);
        setFitHeight(50);
    }

    @Override
    public int giveMarks() {
        return marks;
    }

}
