package com.example.demo6;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

import java.util.Random;

public class RockPaperScissorsPage extends VBox {
    private final Label resultLabel = new Label("Choose: Rock, Paper, or Scissors");

    public RockPaperScissorsPage() {
        this.getStyleClass().add("vbox");

        resultLabel.getStyleClass().add("title-label");

        Button rockBtn = new Button("Rock");
        Button paperBtn = new Button("Paper");
        Button scissorsBtn = new Button("Scissors");

        rockBtn.setMinWidth(100);
        paperBtn.setMinWidth(100);
        scissorsBtn.setMinWidth(100);

        // Dodaj style do przycisków
        rockBtn.getStyleClass().add("button");
        paperBtn.getStyleClass().add("button");
        scissorsBtn.getStyleClass().add("button");

        rockBtn.setOnAction(e -> play("Rock"));
        paperBtn.setOnAction(e -> play("Paper"));
        scissorsBtn.setOnAction(e -> play("Scissors"));

        HBox buttonBox = new HBox(15, rockBtn, paperBtn, scissorsBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("hbox");

        this.setSpacing(30);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(resultLabel, buttonBox);
    }

    private void play(String userChoice) {
        String[] choices = { "Rock", "Paper", "Scissors" };
        String computerChoice = choices[new Random().nextInt(3)];
        String result;
        if (userChoice.equals(computerChoice)) {
            result = "Draw!";
        } else if ((userChoice.equals("Rock") && computerChoice.equals("Scissors")) ||
                (userChoice.equals("Paper") && computerChoice.equals("Rock")) ||
                (userChoice.equals("Scissors") && computerChoice.equals("Paper"))) {
            result = "You win!";
        } else {
            result = "You lose!";
        }
        resultLabel.setText("You: " + userChoice + " | Computer: " + computerChoice + " → " + result);
    }
}