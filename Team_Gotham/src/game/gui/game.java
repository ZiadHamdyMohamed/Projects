package game.gui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

public class game extends Application {

    MediaPlayer mediaPlayer, selectPlayer;
    Scene mainMenuScene, settingsScene, titansScene, startScene, playScene;
    boolean isMuted = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Attack on Titans");

        // Main Menu
        BorderPane mainMenuLayout = createLayoutWithBackground("main.jpeg");
        Button startButton = new Button("Start");
        Button settingsButton = new Button("Settings");
        VBox menuButtons = new VBox(10, startButton, settingsButton);
        menuButtons.setAlignment(Pos.CENTER);
        mainMenuLayout.setCenter(menuButtons);
        mainMenuScene = new Scene(mainMenuLayout, mainMenuLayout.getPrefWidth(), mainMenuLayout.getPrefHeight());
        mainMenuScene.getStylesheets().add("file:src/game/gui/styles.css");
        // Create the "Game Instructions" button
        Button instructionsButton = new Button("Game Instructions");

// Set the button's onAction event handler
        instructionsButton.setOnAction(e -> {
            // Create a TextArea to display the game rules
            TextArea rulesArea = new TextArea();
            rulesArea.setText("Game Rules\nGame Rules\n" +
                    "Winning and losing Conditions: The game will have no winning\n" +
                    "condition and the player will keep playing and defeat as many enemies as\n" +
                    "possible. The player loses when all the starting lanes become lost lanes\n" +
                    "(All their Wall Parts are destroyed) and the accumulated score by then is\n" +
                    "the player’s final score.\n" +
                    "Titan Movement: Each turn, every titan inside all of the active lanes that\n" +
                    "has not reached the walls yet will move closer to the wall (The distance\n" +
                    "from the wall will decrease) a distance equal to their speed stat. Note that\n" +
                    "Colossal titans gain an extra speed of 1 “Distance Unit” per movement\n" +
                    "action.\n" +
                    "Attack Actions: Both Titans and Weapons are able to perform attack\n" +
                    "actions:\n" +
                    "1- Titans: Each turn, only titans inside active lanes that have already\n" +
                    "reached the wall (distance from wall is 0) will perform their attack action\n" +
                    "on the wall part of their lane (reducing the Wall Part’s HP by the amount of\n" +
                    "that Titan’s damage). Note that Abnormal Titans perform their attack\n" +
                    "actions twice per turn.\n" +
                    "2- Weapons: Each turn, only weapons that are deployed into active lanes\n" +
                    "will perform their attack action on the titans their lane (reducing the\n" +
                    "Titans’ HP by the amount of that Weapon’s damage). Each weapon will\n" +
                    "follow the above weapons table on which titans to attack.\n" +
                    "Defeated Attack Targets: Since Titans and Wall Parts can be attacked,\n" +
                    "they can be defeated/destroyed. This happens when the attack target’s HP\n" +
                    "is dropped to 0 or below as a result of an attack. Defeated Titans are\n" +
                    "removed from the lanes they were in (and the game) and their resources\n" +
                    "9\n" +
                    "value is added to the player’s gathered resources as well as the player’s\n" +
                    "score (score increases with the same value as the gathered resources). If a\n" +
                    "Wall Part is destroyed, the lane with this Wall Part is then marked as a Lost\n" +
                    "Lane and not an active Lane. Lost Lanes can not have weapons deployed\n" +
                    "to them nor will have any more Titans spawning in them.\n" +
                    "Approaching Titans: This is a queue of titans that are not yet added to\n" +
                    "any lane. However, it is used to decide which Titan types will be added to\n" +
                    "the active lanes each turn. Whenever it is time to add a new Titan to an\n" +
                    "active lane, the titan at the front of this approaching titans queue is\n" +
                    "removed and then added to the intended lane. If the queue is empty while\n" +
                    "attempting to remove a titan, then the queue is refilled with multiple\n" +
                    "titans according to the below table.\n" +
                    "Titans Spawning & Battle Phase change: Each turn, A specific\n" +
                    "number of titans (Initially 1) is removed from the Approaching Titans\n" +
                    "and added to the lane with the least danger level. A Lane’s danger level is\n" +
                    "the sum of all the titans’ danger levels inside this lane. If, while attempting\n" +
                    "to get an approaching titan, all of the approaching titans have been added\n" +
                    "into lanes (Empty queue), the approaching titans will refill according to the\n" +
                    "following table (If the battle phases has changed while some approaching\n" +
                    "titans still remain, the approaching titans will not refill till all of them are\n" +
                    "added to lanes and then refilled according to the battle phase at the\n" +
                    "moment of refill). The table also shows how the battle phase and the\n" +
                    "number of titans to be added into the lanes change based on the elapsed\n" +
                    "number of turns.\n" +
                    "10\n" +
                    "turns Battle Phase\n" +
                    "#Titans Per\n" +
                    "Turn\n" +
                    "Approaching Titans Refill\n" +
                    "(Front —> Back)\n" +
                    "initial EARLY 1\n" +
                    "Pure, Pure, Pure, Abnormal, Pure,\n" +
                    "Armored, Colossal\n" +
                    "15 INTENSE 1\n" +
                    "Abnormal, Abnormal, Abnormal, Pure,\n" +
                    "Armored, Armored, Colossal\n" +
                    "30\n" +
                    "GRUMBLING\n" +
                    "1\n" +
                    "Colossal, Colossal, Colossal, Colossal,\n" +
                    "Colossal, Colossal, Colossal, 35\n" +
                    "2 (then\n" +
                    "doubles\n" +
                    "every 5\n" +
                    "turns)\n" +
                    "Weapon Purchase: The player will have the option to see all the available\n" +
                    "types of weapons and can choose to buy and deploy them into their\n" +
                    "choice of an active lane. To purchase a weapon, the player should have\n" +
                    "enough resources (higher than the weapon’s price) and then the weapon’s\n" +
                    "price is deducted from the gathered resources.\n" +
                    "Turn Actions: Each turn the player can choose to either Purchase and\n" +
                    "Deploy a Weapon or pass their turn without any actions. Either way the\n" +
                    "turn will proceed as follows. After the player’s action, The titans will do\n" +
                    "their move action. Then the weapons will do their attack action followed by\n" +
                    "the titans’ attack actions. After that, Titans will be added to the lanes\n" +
                    "according to the logic mentioned above. Finally, finalizing the turn by\n" +
                    "updating the battle phase and the relevant info if needed based on the\n" +
                    "number of elapsed turns, also according to the logic mentioned above.");
            rulesArea.setEditable(false);


            Button backButton = createBackButton(primaryStage, mainMenuScene);


            VBox rulesLayout = new VBox(10, backButton, rulesArea);
            rulesLayout.setAlignment(Pos.CENTER);


            Scene rulesScene = new Scene(rulesLayout, 800, 600);
            primaryStage.setScene(rulesScene);
        });


        menuButtons.getChildren().add(instructionsButton);


        BorderPane settingsLayout = createLayoutWithBackground(null);
        Button muteButton = new Button("Mute");
        muteButton.setOnAction(e -> {
            isMuted = !isMuted;
            mediaPlayer.setMute(isMuted);
        });
        Button volumeUpButton = new Button("Volume Up");
        volumeUpButton.setOnAction(e -> {
            if (isMuted) {
                isMuted = false;
                mediaPlayer.setMute(false);
            }
            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.1);
        });
        Button volumeDownButton = new Button("Volume Down");
        volumeDownButton.setOnAction(e -> {
            if (!isMuted) {
                mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.1);
            }
        });
        Button backButton = createBackButton(primaryStage, mainMenuScene);
        settingsLayout.setTop(backButton);
        VBox settingsButtons = new VBox(10, muteButton, volumeUpButton, volumeDownButton);
        settingsButtons.setAlignment(Pos.CENTER);
        settingsLayout.setCenter(settingsButtons);
        settingsScene = new Scene(settingsLayout, settingsLayout.getPrefWidth(), settingsLayout.getPrefHeight());
        settingsScene.getStylesheets().add("file:src/game/gui/styles.css");




        BorderPane playLayout = createLayoutWithBackground("play.jpeg");
        Button easyButton = new Button("Easy");
        Button hardButton = new Button("Hard");
        VBox playButtons = new VBox(10, easyButton, hardButton);
        playButtons.setAlignment(Pos.CENTER);
        playLayout.setCenter(playButtons);
        backButton = createBackButton(primaryStage, mainMenuScene);
        playLayout.setTop(backButton);
        playScene = new Scene(playLayout, playLayout.getPrefWidth(), playLayout.getPrefHeight());
        playScene.getStylesheets().add("file:src/game/gui/styles.css");


        FadeTransition ft = new FadeTransition(Duration.millis(1000));


        startButton.setOnAction(e -> {
            changeScene(primaryStage, playScene, ft);
        });
        settingsButton.setOnAction(e -> {
            changeScene(primaryStage, settingsScene, ft);
        });


        easyButton.setOnAction(e -> {
            try {

                primaryStage.close();


                EasyMode easyMode = new EasyMode();
                Stage easyModeStage = new Stage();
                easyMode.start(easyModeStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        hardButton.setOnAction(e -> {
            try {

                primaryStage.close();


                HardMode hardMode = new HardMode();
                Stage hardModeStage = new Stage();
                hardMode.start(hardModeStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        Media selectSound = new Media(new File("select.mp3").toURI().toString());
        selectPlayer = new MediaPlayer(selectSound);
        startButton.setOnMouseEntered(e -> selectPlayer.play());
        settingsButton.setOnMouseEntered(e -> selectPlayer.play());



        Media media = new Media(new File("music 1.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();


        primaryStage.setScene(mainMenuScene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);


        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                primaryStage.setMaximized(true);
            }
        });

        primaryStage.show();
    }

    private BorderPane createLayoutWithBackground(String imagePath) {
        BorderPane layout = new BorderPane();
        if (imagePath != null) {
            Image image = new Image(new File(imagePath).toURI().toString());
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
            Background background = new Background(backgroundImage);
            layout.setBackground(background);
        }
        return layout;
    }

    private void changeScene(Stage primaryStage, Scene scene, FadeTransition ft) {
        primaryStage.setScene(scene);

        
        ft.setNode(scene.getRoot());
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.play();
    }

    private Button createBackButton(Stage primaryStage, Scene sceneToReturnTo) {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(sceneToReturnTo));
        return backButton;
    }
}