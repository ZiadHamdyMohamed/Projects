package game.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class quiz extends Application {
    Scene mainMenuScene;
   private static int Healthy = 1000;
    private static int Damage = 10;
private int attack;

    public int getHealthy() {
        return Healthy;
    }

    public int getDamage() {
        return Damage;
    }

    public void setDamage(int damage) {
        Damage += damage;
    }
    public void increaseHealth(int health) {
        Healthy += health;
    }

    public void increaseDamage(int damage) {
        Damage += damage;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setHeight(900);
        primaryStage.setWidth(1200);
        primaryStage.setTitle("Titan Game");
        BorderPane hello = new BorderPane();
        Label Health = new Label("Health: " + getHealthy());
        Label Hit = new Label("Damage: " + Damage);
        Label AttacksNeeded = new Label("Attacks Need: ");
        HBox lab = new HBox(Health,Hit,AttacksNeeded);
        lab.setSpacing(10);
        lab.setAlignment(Pos.BASELINE_CENTER);
        hello.setTop(lab);
        Button H = new Button("Increase Health");
        H.setOnAction(event -> {
            increaseHealth(100);
            Health.setText("Health: " + getHealthy());
        });

        Button D = new Button("Increase Damage");
        D.setOnAction(event -> {
            increaseDamage(10);
            Hit.setText("Damage: " + getDamage());
        });
        Button A = new Button("Show attacks Needed");
        A.setOnAction(event -> {
            attack = getHealthy() / getDamage();
            AttacksNeeded.setText("Attacks Need: " + attack);
        });

        HBox labels = new HBox(H,D,A);
        labels.setAlignment(Pos.CENTER);
        hello.setCenter(labels);
        labels.setSpacing(10);
        mainMenuScene = new Scene(hello);
        primaryStage.setScene(mainMenuScene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}