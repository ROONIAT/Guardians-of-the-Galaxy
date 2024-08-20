package model;

public class Moon extends Obstacle{
    public Moon() {
        super("/Image/moon.png", 150, 30, 0.6, 0, 680);
        setFitWidth(75);
        setFitHeight(75);
    }

    @Override
    public int giveMarks() {
        return marks;
    }

}
