package controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.*;
import view.MainMenu;
import view.infoMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameMenuController {

    private static final long TANK_SPAWN_INTERVAL = 15_000_000_000L;
    public static double MigspawnTime = 20;
    public static int gameDifficulty = 1;


    
    
    private long nextTankSpawnTime = 0;

    private AnimationTimer gameTimer;

    private Timeline freezeTimer;
    private Timeline migTimer;
    private int freezeMarks = 0;

    private boolean frozenMode = false;
    private boolean planeRemovalInProgress = false;

    private static final int PLANE_SPEED = 30;
    private static final double PLANE_SIZE = 100;


    private ImageView plane;
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Arrow> arrows = new ArrayList<>();

    private List<MiG> migs = new ArrayList<>();
    private List<ClusterBomb> clusterBombs = new ArrayList<>();
    private List<ClusterBullet> clusterBullets = new ArrayList<>();
    private List<Tank> tanks = new ArrayList<>();
    private List<TankBullet> tankBullets = new ArrayList<>();


    private Pane root;
    private Random random = new Random();
    private long nextSpawnTime = 0;

    private Game currentGame;
    private Text killText;
    private Text scoreText;
    private Text accuracyText;
    private Text clusterText;
    private Text bombText;
    private Text migAlertText;
    private Text waveText;
    private Text migTimerText;
    private Text gameFreezeText;

    private int totalHits = 0;
    private int consecutiveHits = 0;
    private int totalMisses = 0;


    public GameMenuController(Pane root) {
        this.root = root;
        this.currentGame = new Game();
        initializePlane();
        displayStats();
        startMigTimer();
    }


    private void initializePlane() {
        Image planeImage = new Image(getClass().getResourceAsStream("/Image/rocket.png"));
        plane = new ImageView(planeImage);
        plane.setFitWidth(PLANE_SIZE);
        plane.setFitHeight(PLANE_SIZE);
        plane.setLayoutX(100);
        plane.setLayoutY(100);
        root.getChildren().add(plane);
    }

    private void movePlane(double dx, double dy) {
        double newX = plane.getLayoutX() + dx;
        double newY = plane.getLayoutY() + dy;

        newX = (newX + plane.getScene().getWidth()) % plane.getScene().getWidth();
        newY = Math.min(Math.max(newY, 0), plane.getScene().getHeight() - PLANE_SIZE);

        plane.setLayoutX(newX);
        plane.setLayoutY(newY);
    }

    private void handlePlaneRemoval() {
        if (planeRemovalInProgress) {
            return;
        }
        planeRemovalInProgress = true;
        displayGameOver();

        if (gameTimer != null) {
            gameTimer.stop();
        }

        Platform.runLater(() -> {
            try {
                new infoMenu().start(MainMenu.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void handleKeyPress(KeyCode code) {
        switch (code) {
            case UP:
            case W:
                movePlane(0, -PLANE_SPEED);
                break;
            case DOWN:
            case S:
                movePlane(0, PLANE_SPEED);
                break;
            case LEFT:
            case A:
                movePlane(-PLANE_SPEED, 0);
                break;
            case RIGHT:
            case D:
                movePlane(PLANE_SPEED, 0);
                break;
            case SPACE:
                shootArrow();
                break;
            case TAB:
                if (freezeMarks >= 5) {
                    toggleFrozenMode();
                    freezeMarks = 0;
                }
                break;
            case C:
                shootClusterBomb();
                break;
            case V:
                explodeClusterBombs();
                break;
            case P:
                if (currentGame.getWave() < 3) {
                    currentGame.incrementWave();
                } else {
                    showWinAlert();
                    frozenMode = true;
                }

                break;
            case T:
                spawnTank();
                break;
            case O:
                currentGame.incrementClusterBombCount();
                break;
        }
    }


    private void moveObstacles() {
        if (!frozenMode) {
            List<Obstacle> toRemove = new ArrayList<>();
            for (Obstacle obstacle : obstacles) {
                obstacle.move();
                if (obstacle.getLayoutX() > root.getWidth()) {
                    toRemove.add(obstacle);
                }
            }
            obstacles.removeAll(toRemove);
            root.getChildren().removeAll(toRemove);
        }
    }

    private void spawnObstacle() {

        if (!frozenMode) {
            Obstacle obstacle;
            int type = random.nextInt(5);
            switch (type) {
                case 0:
                    obstacle = new Tree();
                    break;
                case 1:
                    obstacle = new Building();
                    break;
                case 2:
                    obstacle = new Clouds();
                    break;
                case 3:
                    obstacle = new Minions();
                    break;
                case 4:
                    obstacle = new Moon();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }

            obstacles.add(obstacle);
            root.getChildren().add(obstacle);
        }
    }

    private long getRandomSpawnInterval() {
        int minSeconds = 3;
        int maxSeconds = 6;
        return (random.nextInt((maxSeconds - minSeconds) + 1) + minSeconds) * 1_000_000_000L;       }


    private void shootArrow() {
        Arrow arrow = new Arrow(plane.getLayoutX() + PLANE_SIZE / 2 - 10, plane.getLayoutY() + PLANE_SIZE);
        arrows.add(arrow);
        root.getChildren().add(arrow);
        currentGame.incrementShootCount();
    }

    private void moveArrows() {
        List<Arrow> toRemoveArrows = new ArrayList<>();
        List<Obstacle> toRemoveObstacles = new ArrayList<>();

        for (Arrow arrow : arrows) {
            arrow.move();
            for (Obstacle obstacle : obstacles) {
                if (arrow.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                                          toRemoveArrows.add(arrow);
                    toRemoveObstacles.add(obstacle);
                    addScore(obstacle.getMarks());


                    if (obstacle instanceof Tree || obstacle instanceof Building) {
                                                  freezeMarks++;
                    }

                    currentGame.incrementKillCount();


                    Explosion explosion = new Explosion(obstacle.getLayoutX(), obstacle.getLayoutY());
                    root.getChildren().add(explosion);

                    totalHits++;
                    consecutiveHits++;
                    if (consecutiveHits == 10) {
                        currentGame.incrementWave();
                        consecutiveHits = 0;
                                              }
                    break;   
                }
            }
            if (arrow.getLayoutY() > root.getHeight()) {
                toRemoveArrows.add(arrow);
                totalMisses++;
                consecutiveHits = 0;
            }
        }

        arrows.removeAll(toRemoveArrows);
        obstacles.removeAll(toRemoveObstacles);
        root.getChildren().removeAll(toRemoveArrows);
        root.getChildren().removeAll(toRemoveObstacles);

        if (totalHits >= 30 && totalMisses == 0) {
                          showWinAlert();
            frozenMode = true;
        }
    }


    private void startMigTimer() {
        if (!frozenMode) {
            migTimer = new Timeline(new KeyFrame(Duration.seconds(MigspawnTime), event -> spawnMiG()));
            migTimer.setCycleCount(Timeline.INDEFINITE);
            migTimer.play();
        }
    }

    private void spawnMiG() {
        if (!frozenMode && (currentGame.getWave() == 3)) {
            double spawnY = random.nextDouble() * root.getHeight();
            MiG mig = new MiG(root.getWidth(), spawnY);
            migs.add(mig);
            root.getChildren().add(mig.getRadiusCircle());
            root.getChildren().add(mig);
        }
    }

    private void moveMigs() {
        if (!frozenMode && (currentGame.getWave() == 3)) {
            List<MiG> toRemove = new ArrayList<>();
            List<Circle> radiusCirclesToRemove = new ArrayList<>();

            for (MiG mig : migs) {
                mig.move();
                if (mig.getLayoutX() < -mig.getFitWidth()) {
                    toRemove.add(mig);
                    radiusCirclesToRemove.add(mig.getRadiusCircle());
                }
            }

            migs.removeAll(toRemove);
            root.getChildren().removeAll(toRemove);
            root.getChildren().removeAll(radiusCirclesToRemove);
        }
    }

    private void checkPlaneProximityToMiGs() {
        if (!frozenMode && (currentGame.getWave() == 3)) {
            List<MiG> toRemoveMigs = new ArrayList<>();
            for (MiG mig : migs) {
                double distance = Math.sqrt(Math.pow(plane.getLayoutX() - mig.getLayoutX(), 2) + Math.pow(plane.getLayoutY() - mig.getLayoutY(), 2));
                if (distance <= mig.getRadius()) {
                    toRemoveMigs.add(mig);
                    handlePlaneRemoval();                   }
            }
            migs.removeAll(toRemoveMigs);
            root.getChildren().removeAll(toRemoveMigs);
        }
    }


    private void explodeClusterBombs() {
        List<ClusterBomb> bombsToExplode = new ArrayList<>(clusterBombs);
        for (ClusterBomb bomb : bombsToExplode) {
            for (ClusterBullet bullet : clusterBullets) {
                for (Obstacle obstacle : obstacles) {
                    if (bullet.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                        addScore(obstacle.getMarks());

                        if (obstacle instanceof Tree || obstacle instanceof Building) {

                            freezeMarks++;
                        }
                    }
                }
            }
            bomb.explode(clusterBullets);
            clusterBombs.remove(bomb);
            root.getChildren().remove(bomb);
        }
    }

    private void shootClusterBomb() {
        if (currentGame.getClusterBombCount() > 0) {
            ClusterBomb clusterBomb = new ClusterBomb(plane.getLayoutX() + PLANE_SIZE / 2 - 25, plane.getLayoutY() + PLANE_SIZE, root);
            clusterBombs.add(clusterBomb);
            root.getChildren().add(clusterBomb);
            currentGame.decrementClusterBombCount();
            clusterText.setText("Clusters: " + currentGame);

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> clusterBomb.move()));
            timeline.setCycleCount(100);
            timeline.play();

            timeline.setOnFinished(event -> {
                if (!clusterBomb.isExploded()) {
                    clusterBomb.explode(clusterBullets);
                    clusterBombs.remove(clusterBomb);
                    root.getChildren().remove(clusterBomb);
                }
            });
        }
    }

    private void moveClusterBullets() {
        List<ClusterBullet> toRemoveBullets = new ArrayList<>();
        for (ClusterBullet bullet : clusterBullets) {
            bullet.move();
            if (bullet.getLayoutX() > root.getWidth() || bullet.getLayoutY() > root.getHeight() || bullet.getLayoutX() < 0 || bullet.getLayoutY() < 0) {
                toRemoveBullets.add(bullet);
            }
        }
        clusterBullets.removeAll(toRemoveBullets);
        root.getChildren().removeAll(toRemoveBullets);
    }


    public void toggleFrozenMode() {
        frozenMode = !frozenMode;

        if (frozenMode) {
            startFreezeTimer();
        } else {
            stopFreezeTimer();
        }
    }

    private void startFreezeTimer() {
        freezeTimer = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
            toggleFrozenMode();
        }));
        freezeTimer.setCycleCount(1);
        freezeTimer.play();
    }

    private void stopFreezeTimer() {
        if (freezeTimer != null) {
            freezeTimer.stop();
        }
    }


    private void spawnTank() {
        if (!frozenMode && (currentGame.getWave() == 2)) {
            double x = 0;
            double y = 100 + (300 * random.nextDouble());

            Tank tank = new Tank(x, y, root);
            tanks.add(tank);
            root.getChildren().add(tank);

        }
    }

    private void moveTanks() {
        if (!frozenMode) {
            List<Tank> toRemoveTanks = new ArrayList<>();
            for (Tank tank : tanks) {
                tank.move();
                if (tank.getLayoutX() > root.getWidth()) {
                    toRemoveTanks.add(tank);
                }
            }
            tanks.removeAll(toRemoveTanks);
            root.getChildren().removeAll(toRemoveTanks);
        }
    }

    private void shootTankBullets() {
        if (!frozenMode && (currentGame.getWave() == 2)) {
            double planeX = plane.getLayoutX() + PLANE_SIZE / 2;
            double planeY = plane.getLayoutY() + PLANE_SIZE / 2;
            for (Tank tank : tanks) {
                tank.shootIfPlaneInRange(planeX, planeY, tankBullets, root);
            }
        }
    }


    private void moveTankBullets() {
        if (!frozenMode) {
            List<TankBullet> toRemoveBullets = new ArrayList<>();
            for (TankBullet bullet : tankBullets) {
                bullet.move();
                if (bullet.getLayoutX() > root.getWidth() || bullet.getLayoutY() > root.getHeight() || bullet.getLayoutX() < 0 || bullet.getLayoutY() < 0) {
                    toRemoveBullets.add(bullet);
                }
            }
            tankBullets.removeAll(toRemoveBullets);
            root.getChildren().removeAll(toRemoveBullets);
        }
    }


    public void startGame() {

        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now >= nextSpawnTime) {
                    spawnObstacle();
                    nextSpawnTime = now + getRandomSpawnInterval();
                }
                if (now >= nextTankSpawnTime) {
                    spawnTank();
                    nextTankSpawnTime = now + TANK_SPAWN_INTERVAL;
                }

                moveObstacles();
                moveArrows();


                moveTanks();

                shootTankBullets();
                moveTankBullets();

                moveClusterBullets();
                moveMigs();

                checkCollisions();
                checkPlaneProximityToMiGs();

                updateStats();


            }
        };
        gameTimer.start();
    }


    private void checkCollisions() {
        List<Obstacle> toRemoveObstacles = new ArrayList<>();
        for (Obstacle obstacle : obstacles) {
            if (plane.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {

                toRemoveObstacles.add(obstacle);


            }
        }

        List<ClusterBullet> toRemoveBullets = new ArrayList<>();
        for (ClusterBullet bullet : clusterBullets) {
            for (Obstacle obstacle : obstacles) {
                if (bullet.getBoundsInParent().intersects(obstacle.getBoundsInParent())) {
                    toRemoveBullets.add(bullet);
                    toRemoveObstacles.add(obstacle);
                    addScore(obstacle.getMarks());
                }
            }
        }

        List<MiG> toRemoveMigs = new ArrayList<>();
        for (MiG mig : migs) {
            if (plane.getBoundsInParent().intersects(mig.getBoundsInParent())) {
                toRemoveMigs.add(mig);
            }
        }


        for (TankBullet bullet : tankBullets) {
            if (plane.getBoundsInParent().intersects(bullet.getBoundsInParent())) {

                root.getChildren().removeAll(tankBullets);
                handlePlaneRemoval();

            }
        }

        obstacles.removeAll(toRemoveObstacles);
        root.getChildren().removeAll(toRemoveObstacles);

        migs.removeAll(toRemoveMigs);
        for (MiG mig : toRemoveMigs) {
            root.getChildren().remove(mig.getRadiusCircle());
        }
        root.getChildren().removeAll(toRemoveMigs);
        clusterBullets.removeAll(toRemoveBullets);
        root.getChildren().removeAll(toRemoveBullets);
    }

    private void displayStats() {
        killText = new Text("Kills: " + currentGame.getKillCount());
        killText.setLayoutX(10);
        killText.setLayoutY(20);
        killText.setFill(Color.WHITE);
        root.getChildren().add(killText);

        accuracyText = new Text("Accuracy: 0%");
        accuracyText.setLayoutX(10);
        accuracyText.setLayoutY(40);
        accuracyText.setFill(Color.WHITE);
        root.getChildren().add(accuracyText);


        clusterText = new Text("Clusters: " + currentGame.getClusterBombCount());
        clusterText.setLayoutX(10);
        clusterText.setLayoutY(60);
        clusterText.setFill(Color.WHITE);
        root.getChildren().add(clusterText);

        bombText = new Text("Bombs: " + currentGame.getRadioActiveBombCount());
        bombText.setLayoutX(10);
        bombText.setLayoutY(80);
        bombText.setFill(Color.WHITE);
        root.getChildren().add(bombText);

        migAlertText = new Text();
        migAlertText.setLayoutX(10);
        migAlertText.setLayoutY(100);
        migAlertText.setFill(Color.WHITE);
        root.getChildren().add(migAlertText);

        waveText = new Text("Wave: " + currentGame.getWave());
        waveText.setLayoutX(10);
        waveText.setLayoutY(120);
        waveText.setFill(Color.WHITE);
        root.getChildren().add(waveText);

        scoreText = new Text("score " + currentGame.getScore());
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(140);
        scoreText.setFill(Color.WHITE);
        root.getChildren().add(scoreText);


        migTimerText = new Text(" ");
        migTimerText.setFill(Color.WHITE);
        migTimerText.setLayoutX(10);
        migTimerText.setLayoutY(200);
        root.getChildren().add(migTimerText);

        gameFreezeText = new Text("Frozen Mode: " + freezeMarks + "/5");
        gameFreezeText.setFill(Color.WHITE);
        gameFreezeText.setLayoutX(10);
        gameFreezeText.setLayoutY(160);
        root.getChildren().add(gameFreezeText);
    }

    private void updateStats() {
        killText.setText("Kills: " + currentGame.getKillCount());
        accuracyText.setText("Accuracy: " + currentGame.getAccuracy());

        clusterText.setText("Clusters: " + currentGame.getClusterBombCount());
        bombText.setText("Bombs: " + currentGame.getRadioActiveBombCount());

        scoreText.setText("Scores: " + currentGame.getScore());
        waveText.setText("Wave: " + currentGame.getWave());
        gameFreezeText.setText("Frozen Mode: " + freezeMarks + "/5");
        long timeUntilMiG = (long) ((migTimer.getCycleDuration().toMillis() - migTimer.getCurrentTime().toMillis()) / 1000);
        if (timeUntilMiG < 4 && currentGame.getWave() == 3)
            migTimerText.setText("MiG Arrival in: " + timeUntilMiG + " seconds");

    }


    private void displayGameOver() {
        Text gameOverText = new Text("Game Over");
        gameOverText.setFill(Color.RED);
        gameOverText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");
        gameOverText.setLayoutX(root.getWidth() / 2 - 100);
        gameOverText.setLayoutY(root.getHeight() / 2);
        root.getChildren().add(gameOverText);
    }

    private void showWinAlert() {
        Text congratsText = new Text("Congratulations! You've completed the game.");
        congratsText.setFill(Color.GREEN);
        congratsText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        congratsText.setLayoutX(root.getWidth() / 2 - 250);
        congratsText.setLayoutY(root.getHeight() / 2 - 50);

        int killCount = currentGame.getKillCount();
        int shootCount = currentGame.getShootCount();
        double accuracy = shootCount == 0 ? 0 : (double) killCount / shootCount * 100;

        Text statsText = new Text(String.format("Statistics:\nKills: %d\nShots Fired: %d\nAccuracy: %.2f%%", killCount, shootCount, accuracy));
        statsText.setFill(Color.BLACK);
        statsText.setStyle("-fx-font-size: 24px;");
        statsText.setLayoutX(root.getWidth() / 2 - 250);
        statsText.setLayoutY(root.getHeight() / 2);

        root.getChildren().addAll(congratsText, statsText);
    }

    private void addScore(int marks) {
        currentGame.setScore(currentGame.getScore() + marks);
        updateStats();
    }

}

