package model;

import javafx.scene.image.Image;
import java.util.Comparator;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class User {
    private String username;
    private String password;
    private static ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Game> games;
    protected static User loggedInUser = null;
    private Image avatar;
    private int points;
    private double accuracy;
    private int killCount;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.games = new ArrayList<>();
        Random random = new Random();
        int randomNum = random.nextInt(8);
        this.avatar = new Image(User.class.getResource("/Image/avatar" + randomNum + ".png").toString());
        this.points = 0;
        this.accuracy = 0;
        this.killCount = 0;
        Users.add(this);
    }


    public int getPoints() {
        for (Game game : games) {
            points += game.getScore();
        }
        return points;
    }

    public double getAccuracy() {
        double totalAccuracy = 0;
        for (Game game : games) {
            totalAccuracy += game.getAccuracy();
        }
        return totalAccuracy / games.size(); // Average accuracy
    }

    public int getKillCount() {
        for (Game game : games) {
            killCount += game.getKillCount();
        }
        return killCount;
    }




    public Game getLastGame() {
        Optional<Game> lastGame = games.stream().max(Comparator.comparing(Game::getTimestamp));
        return lastGame.orElse(null);
    }


    public static ArrayList<User> getAllUsers() {
        return Users;
    }

    public static User getUserByUsername(String text) {
        for (User user : Users) {
            if (user.getUsername().equals(text)) {
                return user;
            }
        }
        return null;
    }

    public static void setLoggedInUser(User userByUsername) {
        loggedInUser = userByUsername;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static void changeUsername(String text) {
        loggedInUser.setUsername(text);
    }

    public static void changePassword(String text) {
        loggedInUser.setPassword(text);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }


    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void login(User user) {
        loggedInUser = user;
    }


    public static void deleteAccount() {
        Users.remove(loggedInUser);
        loggedInUser = null;
    }


    public ArrayList<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        games.add(game);
    }
}

