package com.example.demo6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        StopwatchPage stopwatchPage = new StopwatchPage();
        RockPaperScissorsPage rpsPage = new RockPaperScissorsPage();
        CalculatorPage calculatorPage = new CalculatorPage();
        TaskPage taskPage = new TaskPage();
        WeatherPage weatherPage = new WeatherPage();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Narzędzia");

        MenuItem stopwatchItem = new MenuItem("Stoper");
        MenuItem rpsItem = new MenuItem("Papier Kamień Nożyce");
        MenuItem calculatorItem = new MenuItem("Kalkulator");
        MenuItem taskItem = new MenuItem("Zadania");
        MenuItem weatherItem = new MenuItem("Pogoda");

        menu.getItems().addAll(stopwatchItem, rpsItem, calculatorItem, taskItem, weatherItem);
        menuBar.getMenus().add(menu);

        root.setTop(menuBar);
        root.setCenter(stopwatchPage);

        stopwatchItem.setOnAction(e -> root.setCenter(stopwatchPage));
        rpsItem.setOnAction(e -> root.setCenter(rpsPage));
        calculatorItem.setOnAction(e -> root.setCenter(calculatorPage));
        taskItem.setOnAction(e -> root.setCenter(taskPage));
        weatherItem.setOnAction(e -> root.setCenter(weatherPage)); // <--- show weather page

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/com/example/demo6/styles.css").toExternalForm());
        stage.setTitle("JavaFX Multi-Tool");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}