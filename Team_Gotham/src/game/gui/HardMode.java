package game.gui;
import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HardMode extends Application {
    public static int titanSpawnDistance = 75;
    public BorderPane root;
    public int initialNumOfLanes = 5, initialResourcesPerLane = 125;
    public GridPane titanSpawner;

    public static Circle titanCircle(Titan titan) {
        //switch based on titan type for radius and color
        switch (titan.getDangerLevel()) {
            case AbnormalTitan.TITAN_CODE:
                return new Circle(15, Color.BLUE);
            case ArmoredTitan.TITAN_CODE:
                return new Circle(20, Color.ORANGE);
            case ColossalTitan.TITAN_CODE:
                return new Circle(25, Color.BLACK);
            default:
                return new Circle(10, Color.GREEN);
        }
    }

    public static void addTitanInfoOnClick(Circle circle, Titan titan, BorderPane root) {
        circle.setOnMouseClicked(event -> {


            BorderPane bp = new BorderPane();


            ProgressBar healthBar = new ProgressBar((double) titan.getCurrentHealth() / titan.getBaseHealth());
            healthBar.setPrefWidth(200);


            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
            dataSeries.getData().add(new XYChart.Data<>("Damage", titan.getDamage()));
            dataSeries.getData().add(new XYChart.Data<>("Danger Level", titan.getDangerLevel()));
            dataSeries.getData().add(new XYChart.Data<>("Height", titan.getHeightInMeters()));
            dataSeries.getData().add(new XYChart.Data<>("Distance from Base", titan.getDistance()));
            barChart.getData().add(dataSeries);


            Label nameLabel = new Label("Titan: " + titan.getClass().getSimpleName());
            nameLabel.setFont(new Font("Arial", 30));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(event1 -> {
                root.setRight(null);
            });
            closeButton.getStylesheets().add("/styles2.css");
            VBox v=new VBox();
            v.getChildren().addAll(nameLabel, healthBar,barChart,closeButton);
            bp.setCenter(v);
            root.setRight(bp);


            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue(barChart.animatedProperty(), true)));
            timeline.play();
        });
    }
    public static void AddToolTipToTitan(Titan titan, Circle circle, Battle battle, Stage stage) {
        Tooltip.install(circle, new Tooltip("Titan Name: " + titan.getClass().getSimpleName() +
                "\nDistanceFromWall: " + titan.getDistance() +
                "\nHP: " + titan.getCurrentHealth() + "/" + titan.getBaseHealth() +
                "\nDamage: " + titan.getDamage() +
                "\nHeight: " + titan.getHeightInMeters() +
                "\nSpeed: " + titan.getSpeed() +
                "\nResource Value: " + titan.getResourcesValue()+
                "\nDanger Level: " + titan.getDangerLevel()));
    }
    public static int distancer(Titan titan){
        int ans = titanSpawnDistance%titan.getSpeed();
        return (ans==0?titan.getSpeed():ans)+(titan.getDangerLevel()==4?2:0);
    }



    public HardMode() {
        this.titanSpawner = new GridPane();
        titanSpawner.setPrefSize(1200, 800);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setHeight(900);
        primaryStage.setWidth(1200);
        primaryStage.setTitle("Easy Mode Game");
        beginGame(primaryStage);
        primaryStage.show();
    }

    public void beginGame(Stage stage) {
        Battle battle = null;
        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red;");

        try {
            int numberOfTurns = 1;
            int score = 0;
            battle = new Battle(numberOfTurns, score, titanSpawnDistance, initialNumOfLanes, initialResourcesPerLane);

            root = new BorderPane();

            Label scoreLabel = new Label("Score: " + battle.getScore());
            Label turnLabel = new Label("Turn: " + battle.getNumberOfTurns());
            Label phaseLabel = new Label("Phase: " + battle.getBattlePhase());
            Label resourcesLabel = new Label("Resources: " + battle.getResourcesGathered());
            Label lanesLabel = new Label("Lanes: " + battle.getLanes().size());
            Label weaponShopLabel = new Label("Weapon Shop: ");

            HBox statusBox = new HBox(scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, weaponShopLabel, errorMessageLabel);
            statusBox.setSpacing(10);
            statusBox.setStyle("-fx-border-color: red; -fx-background-color: lightgray");
            root.setTop(statusBox);

            HBox weapons = new HBox();
            Button piercingCannon = new Button("Piercing Cannon");
            Button sniperCannon = new Button("Sniper Cannon");
            Button volleySpreadCannon = new Button("Volley Spread Cannon");
            Button wallTrap = new Button("Wall Trap");
            weapons.getChildren().addAll(piercingCannon, sniperCannon, volleySpreadCannon, wallTrap);
            statusBox.getChildren().add(weapons);

            VBox lanes = new VBox();
            lanes.setAlignment(Pos.BOTTOM_LEFT);
            lanes.setPadding(new Insets(0, 0, 0, 0));
            lanes.setSpacing(0);
            lanes.setStyle("-fx-border-color: blue");

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(30, 10, 10, 10));
            gridPane.setVgap(10);
            gridPane.setHgap(10);
            gridPane.setStyle("-fx-border-color: green");
            root.setCenter(gridPane);

            for (int i = 0; i < initialNumOfLanes; i++) {
                Button laneButton = new Button("L" + (i + 1));
                laneButton.setAlignment(Pos.TOP_LEFT);
                laneButton.setPrefWidth(50);
                laneButton.setPrefHeight(stage.getHeight() / initialNumOfLanes);
                lanes.getChildren().add(laneButton);
            }
            root.setLeft(lanes);

            Button passTurnButton = new Button("Pass Turn");
            Battle finalBattle = battle;
            passTurnButton.setOnAction(event -> {
                try {
                    finalBattle.passTurn();
                    animateTitanAttacks(gridPane, finalBattle);
                    updateAllLanes(gridPane, finalBattle, stage, root);
                    updateStatus(finalBattle, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);



                    if (finalBattle.isGameOver()) {

                        Stage gameOverStage = new Stage();
                        VBox vbox = new VBox(new Label("Game Over! Your final score is: " + finalBattle.getScore()));
                        vbox.setAlignment(Pos.CENTER);
                        vbox.setSpacing(10);
                        String musicFile = "gameOver.mp3";


                        Media sound = new Media(new File(musicFile).toURI().toString());


                        MediaPlayer mediaPlayer = new MediaPlayer(sound);


                        mediaPlayer.play();


                        Button returnButton = new Button("Return to Game");
                        returnButton.setOnAction(e -> {
                            stage.close();
                            game gameInstance = new game();
                            Stage newStage = new Stage();
                            try {
                                gameInstance.start(newStage);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        });

                        vbox.getChildren().add(returnButton);
                        Scene gameOverScene = new Scene(vbox, 300, 200);
                        gameOverStage.setScene(gameOverScene);
                        gameOverStage.show();
                    }
                } catch (NullPointerException e) {
                    displayErrorMessage(errorMessageLabel, "An error occurred while passing the turn.");
                }
            });
            statusBox.getChildren().add(passTurnButton);

            Button returnButton = new Button("Return");
            returnButton.setOnAction(event -> beginGame(stage));
            statusBox.getChildren().add(returnButton);

            setWeaponHandlers(weapons, battle, gridPane, stage, root, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);

            Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            displayErrorMessage(errorMessageLabel, "Failed to start the game.");
        } catch (NullPointerException e) {
            displayErrorMessage(errorMessageLabel, "An unexpected error occurred. Please try again.");
        } catch (Exception e) {
            displayErrorMessage(errorMessageLabel, "An unexpected error occurred: " + e.getMessage());
        }

        if (battle == null) {
            displayErrorMessage(errorMessageLabel, "Failed to initialize the battle. Please check your configurations.");
        }
    }

    private void displayErrorMessage(Label errorMessageLabel, String message) {
        errorMessageLabel.setText(message);
        errorMessageLabel.setVisible(true);
    }




    private void setWeaponHandlers(HBox weapons, Battle battle, GridPane gridPane, Stage stage, BorderPane root, Label scoreLabel, Label turnLabel, Label phaseLabel, Label resourcesLabel, Label lanesLabel, VBox lanes) {
        Button piercingCannon = (Button) weapons.getChildren().get(0);
        Button sniperCannon = (Button) weapons.getChildren().get(1);
        Button volleySpreadCannon = (Button) weapons.getChildren().get(2);
        Button wallTrap = (Button) weapons.getChildren().get(3);

        setWeaponHandler(piercingCannon, 1, battle, weapons, gridPane, stage, root, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);
        setWeaponHandler(sniperCannon, 2, battle, weapons, gridPane, stage, root, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);
        setWeaponHandler(volleySpreadCannon, 3, battle, weapons, gridPane, stage, root, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);
        setWeaponHandler(wallTrap, 4, battle, weapons, gridPane, stage, root, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);
    }

    private void setWeaponHandler(Button weaponButton, int weaponType, Battle battle, HBox weapons, GridPane gridPane, Stage stage, BorderPane root, Label scoreLabel, Label turnLabel, Label phaseLabel, Label resourcesLabel, Label lanesLabel, VBox lanes) {
        weaponButton.setOnAction(event -> {
            VBox laneButtons = new VBox();
            for (int i = 0; i < initialNumOfLanes; i++) {
                Button laneButton = new Button("Lane " + (i + 1));
                int laneIndex = i;
                laneButton.setOnAction(e -> {
                    Label errorMessageLabel = new Label();
                    try {
                        battle.purchaseWeapon(weaponType, battle.getOriginalLanes().get(laneIndex));
                        updateStatus(battle, scoreLabel, turnLabel, phaseLabel, resourcesLabel, lanesLabel, lanes);
                        updateAllLanes(gridPane, battle, stage, root);
                        showPopupNotification("Weapon successfully added to Lane " + (laneIndex + 1));
                    } catch (InvalidLaneException ex) {
                        displayErrorMessage(errorMessageLabel, "This lane is no longer available.");
                    } catch (InsufficientResourcesException ex) {
                        displayErrorMessage(errorMessageLabel, "You have not enough resources.");
                    }
                    weapons.getChildren().set(weapons.getChildren().indexOf(laneButtons), weaponButton);
                });
                laneButtons.getChildren().add(laneButton);
            }

            weapons.getChildren().set(weapons.getChildren().indexOf(weaponButton), laneButtons);

            root.addEventFilter(MouseEvent.MOUSE_CLICKED, event1 -> {
                if (!weapons.getChildren().contains(event1.getTarget())) {
                    weapons.getChildren().set(weapons.getChildren().indexOf(laneButtons), weaponButton);
                }
            });
        });
    }

    private void showPopupNotification(String message) {
        Label popupLabel = new Label(message);
        popupLabel.setStyle("-fx-background-color: yellow; -fx-padding: 10px;");
        Popup popup;
        popup = new Popup();
        popup.getContent().add(popupLabel);
        popup.show(root.getScene().getWindow());

        new Timeline(new KeyFrame(Duration.seconds(2), e -> popup.hide())).play();
    }

    private void updateStatus(Battle battle, Label scoreLabel, Label turnLabel, Label phaseLabel, Label resourcesLabel, Label lanesLabel, VBox lanes) {
        scoreLabel.setText("Score: " + battle.getScore());
        turnLabel.setText("Turn: " + battle.getNumberOfTurns());
        phaseLabel.setText("Phase: " + battle.getBattlePhase());
        resourcesLabel.setText("Resources: " + battle.getResourcesGathered());
        lanesLabel.setText("Lanes: " + battle.getLanes().size());
    }


    private void animateTitanAttacks(GridPane gridPane, Battle battle) {
        for (int i = 0; i < battle.getOriginalLanes().size(); i++) {
            Lane lane = battle.getOriginalLanes().get(i);
            for (Titan titan : lane.getTitans()) {
                Circle attack = new Circle(10, Color.BLUE);
                gridPane.add(attack, 0, i);
                Circle attack2 = new Circle(10, Color.GREEN);
                gridPane.add(attack2, 3, i);
                Circle attack3 = new Circle(10, Color.BLACK);
                gridPane.add(attack3, 6, i);

                TranslateTransition transition = new TranslateTransition();
                transition.setNode(attack);
                transition.setDuration(Duration.seconds(1));
                transition.setFromX(gridPane.getWidth());
                transition.setToX(0);
                transition.setCycleCount(1);
                transition.setOnFinished(event -> gridPane.getChildren().remove(attack));
                transition.play();

                TranslateTransition transition2 = new TranslateTransition();
                transition2.setNode(attack2);
                transition2.setDuration(Duration.seconds(1));
                transition2.setFromX(gridPane.getWidth());
                transition2.setToX(0);
                transition2.setCycleCount(1);
                transition2.setOnFinished(event -> gridPane.getChildren().remove(attack2));
                transition2.play();

                TranslateTransition transition3 = new TranslateTransition();
                transition3.setNode(attack3);
                transition3.setDuration(Duration.seconds(1));
                transition3.setFromX(gridPane.getWidth());
                transition3.setToX(0);
                transition3.setCycleCount(1);
                transition3.setOnFinished(event -> gridPane.getChildren().remove(attack3));
                transition3.play();
            }
        }
    }


    public static void updateAllLanes(GridPane gridPane, Battle battle, Stage stage, BorderPane root) {

        Map<Titan, Pair<Circle, Double>> titanCircleMap = new HashMap<>();
        System.out.println("Updating all lanes...");

        for (int i = 0; i < battle.getOriginalLanes().size(); i++) {
            Lane lane = battle.getOriginalLanes().get(i);
            if (lane.isLaneLost()) {
                continue;
            }

            Pane pane = null;
            if (i < gridPane.getChildren().size() && gridPane.getChildren().get(i) instanceof Pane) {
                pane = (Pane) gridPane.getChildren().get(i);
            } else {

                pane = new Pane();
                gridPane.add(pane, 0, i);
            }

            for (Titan titan : lane.getTitans()) {
                Pair<Circle, Double> pair = titanCircleMap.get(titan);
                Circle circle;
                double yPosition;
                if (pair == null) {

                    circle = titanCircle(titan);
                    yPosition = Math.random() * (pane.getHeight());
                    titanCircleMap.put(titan, new Pair<>(circle, yPosition));
                    circle.setCenterX(titan.getDistance() * 15);
                    circle.setCenterY(yPosition);

                    addTitanInfoOnClick(circle, titan, root);
                    AddToolTipToTitan(titan, circle, battle, stage);

                    pane.getChildren().add(circle);
                    System.out.println("Added new titan to lane " + i + ": " + titan.getClass().getSimpleName());
                } else {
                    circle = pair.getKey();
                    yPosition = pair.getValue();

                    addTitanInfoOnClick(circle, titan, root);
                    AddToolTipToTitan(titan, circle, battle, stage);

                    if (!titan.hasReachedTarget()) {

                        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), circle);
                        tt.setFromX(15 * (titan.getDistance() + titan.getSpeed() - (titan.getDangerLevel() == 4 ? 1 : 0) - titanSpawnDistance));
                        tt.setToX(15 * (titan.getDistance() - titanSpawnDistance));
                        tt.play();
                    } else if (titan.FirstTime) {

                        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5), circle);
                        tt.setFromX(15 * (distancer(titan) - titanSpawnDistance));
                        tt.setToX(15 * (-titanSpawnDistance));
                        tt.play();
                        titan.FirstTime = false;
                    }
                    circle.setCenterY(yPosition);
                    System.out.println("Updated existing titan in lane " + i + ": " + titan.getClass().getSimpleName());
                }
            }


            int finalI = i;
            pane.getChildren().removeIf(node -> {
                if (node instanceof Circle) {
                    Circle circle = (Circle) node;
                    Optional<Titan> optionalTitan = titanCircleMap.entrySet().stream()
                            .filter(entry -> entry.getValue().getKey().equals(circle))
                            .map(Map.Entry::getKey)
                            .findFirst();
                    if (optionalTitan.isPresent() && optionalTitan.get().isDefeated()) {
                        System.out.println("Removed defeated titan from lane " + finalI + ": " + optionalTitan.get().getClass().getSimpleName());
                        return true;
                    }
                }
                return false;
            });
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
