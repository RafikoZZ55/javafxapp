package com.example.demo6;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.animation.AnimationTimer;

public class StopwatchPage extends VBox {
    private long startTime = 0;
    private boolean running = false;
    private final Label timeLabel = new Label("00:00:00");
    private final AnimationTimer timer;

    public StopwatchPage() {
        this.getStyleClass().add("vbox");

        timeLabel.getStyleClass().add("title-label");

        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button resetBtn = new Button("Reset");
        startBtn.getStyleClass().add("button");
        stopBtn.getStyleClass().add("button");
        resetBtn.getStyleClass().add("button");

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    long elapsed = (System.currentTimeMillis() - startTime);
                    long seconds = (elapsed / 1000) % 60;
                    long minutes = (elapsed / (1000 * 60)) % 60;
                    long hours = (elapsed / (1000 * 60 * 60));
                    timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }
            }
        };

        startBtn.setOnAction(e -> {
            if (!running) {
                startTime = System.currentTimeMillis() - getElapsedMillis();
                running = true;
                timer.start();
            }
        });

        stopBtn.setOnAction(e -> running = false);

        resetBtn.setOnAction(e -> {
            running = false;
            startTime = System.currentTimeMillis();
            timeLabel.setText("00:00:00");
        });

        HBox buttonBox = new HBox(10, startBtn, stopBtn, resetBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("hbox");

        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(timeLabel, buttonBox);
    }

    private long getElapsedMillis() {
        String[] parts = timeLabel.getText().split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000L;
    }
}