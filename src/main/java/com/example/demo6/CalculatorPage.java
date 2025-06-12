package com.example.demo6;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorPage extends VBox {
    private final GridPane grid = new GridPane();
    private final TextField display = new TextField();

    private double num1 = 0;
    private String operator = "";
    private boolean start = true;

    public CalculatorPage() {
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("vbox");

        grid.getStyleClass().add("grid-pane");

        display.setEditable(false);
        display.setPrefHeight(40);
        display.getStyleClass().add("text-field");
        grid.add(display, 0, 0, 4, 1);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        int row = 1, col = 0;
        for (String text : buttons) {
            Button btn = new Button(text);
            btn.setPrefSize(50, 50);
            btn.getStyleClass().add("button");
            grid.add(btn, col, row);
            btn.setOnAction(e -> onButtonClick(text));
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        grid.setVgap(5);
        grid.setHgap(5);

        this.getChildren().add(grid);
    }

    private void onButtonClick(String text) {
        if (text.matches("[0-9]")) {
            if (start) {
                display.setText("");
                start = false;
            }
            display.setText(display.getText() + text);
        } else if (text.matches("[+\\-*/]")) {
            num1 = Double.parseDouble(display.getText());
            operator = text;
            start = true;
        } else if (text.equals("=")) {
            double num2 = Double.parseDouble(display.getText());
            double result = 0;
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num2 != 0 ? num1 / num2 : 0;
                    break;
            }
            display.setText("" + result);
            start = true;
        } else if (text.equals("C")) {
            display.setText("");
            start = true;
        }
    }
}