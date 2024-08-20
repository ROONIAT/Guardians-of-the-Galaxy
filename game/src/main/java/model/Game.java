package model;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Game {

    private int ClusterBombCount;
    private int RadioActiveBombCount;
    private int killCount;
    private int shootCount;
    private double accuracy;
    private int score;
    private User user;
    private int wave;
    private int difficulty;


    private LocalDateTime timestamp;
    private static ArrayList<Game> games = new ArrayList<>();

    public Game() {
        this.ClusterBombCount = 3;
        this.RadioActiveBombCount = 3;
        this.killCount = 0;
        this.shootCount = 0;
        this.wave = 1;
        this.difficulty = 1;
        this.accuracy = 00.0;
        this.score = 0;
        this.timestamp = LocalDateTime.now();
        this.user = User.getLoggedInUser();
        if (this.user != null) {
            this.user.addGame(this);
        }
        games.add(this);
    }

    public int getClusterBombCount() {
        return ClusterBombCount;
    }

    public int setClusterBombCount(int ClusterBombCount) {
        this.ClusterBombCount = ClusterBombCount;
        return ClusterBombCount;
    }

    public void incrementClusterBombCount() {
        ClusterBombCount++;
    }

    public void incrementRadioActiveBombCount() {
        RadioActiveBombCount++;
    }

    public void decrementClusterBombCount() {
        ClusterBombCount--;
    }

    public void decrementRadioActiveBombCount() {
        RadioActiveBombCount--;
    }

    public int getRadioActiveBombCount() {
        return RadioActiveBombCount;
    }

    public int setRadioActiveBombCount(int RadioActiveBombCount) {
        this.RadioActiveBombCount = RadioActiveBombCount;
        return RadioActiveBombCount;
    }



    public int getWave() {
        return wave;
    }

    public void incrementWave() {
        wave++;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void incrementDifficulty() {
        difficulty++;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void incrementKillCount() {
        killCount++;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementShootCount() {
        shootCount++;
    }

    public int getShootCount() {
        return shootCount;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        if(shootCount==0)
            return 0.00;
        double v = ((double) killCount / shootCount);
        return v;
    }

    public int getScore() {
        return score;
    }

    public void setScore( int mark){
        score= mark;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }


    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public void incrementScore(int v) {
        score = v + score;
    }

    public void setWave(int i) {
        wave = i;
    }
}
