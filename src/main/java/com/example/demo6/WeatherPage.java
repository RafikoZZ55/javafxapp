package com.example.demo6;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URI;
import java.net.http.*;
import java.util.*;
import org.json.*;

public class WeatherPage extends VBox {
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final ListView<Place> resultsList = new ListView<>();
    private final VBox weatherBox = new VBox(10);

    public WeatherPage() {
        this.getStyleClass().add("vbox");
        searchField.setPromptText("Enter city or place name...");
        searchField.getStyleClass().add("text-field");
        searchButton.getStyleClass().add("button");
        resultsList.getStyleClass().add("list-view");
        weatherBox.setPadding(new Insets(10));
        weatherBox.setAlignment(Pos.CENTER_LEFT);

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.getStyleClass().add("hbox");

        this.setSpacing(18);
        this.setPadding(new Insets(25));
        this.getChildren().addAll(new Label("Weather App"), searchBox, resultsList, weatherBox);

        searchButton.setOnAction(e -> searchPlaces());
        resultsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fetchWeather(newVal);
            }
        });
    }

    private void searchPlaces() {
        String query = searchField.getText().trim();
        if (query.isEmpty())
            return;
        resultsList.getItems().clear();
        weatherBox.getChildren().clear();

        new Thread(() -> {
            try {
                String url = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                        java.net.URLEncoder.encode(query, "UTF-8") + "&count=10&language=en&format=json";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());
                List<Place> places = new ArrayList<>();
                if (json.has("results")) {
                    JSONArray arr = json.getJSONArray("results");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        places.add(new Place(
                                obj.optString("name"),
                                obj.optString("country"),
                                obj.optDouble("latitude"),
                                obj.optDouble("longitude")));
                    }
                }
                Platform.runLater(() -> resultsList.getItems().setAll(places));
            } catch (Exception ex) {
                Platform.runLater(() -> weatherBox.getChildren().setAll(new Label("Error: " + ex.getMessage())));
            }
        }).start();
    }

    private void fetchWeather(Place place) {
        weatherBox.getChildren().setAll(new Label("Loading weather for " + place + "..."));
        new Thread(() -> {
            try {
                String url = String.format(
                        Locale.US,
                        "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true",
                        place.latitude, place.longitude);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());
                JSONObject current = json.getJSONObject("current_weather");
                String info = String.format(
                        "Temperature: %.1f°C\nWind: %.1f km/h\nWeather code: %d",
                        current.getDouble("temperature"),
                        current.getDouble("windspeed"),
                        current.getInt("weathercode"));
                Platform.runLater(() -> weatherBox.getChildren().setAll(
                        new Label("Weather for " + place + ":"),
                        new Text(info)));
            } catch (Exception ex) {
                Platform.runLater(() -> weatherBox.getChildren().setAll(new Label("Error: " + ex.getMessage())));
            }
        }).start();
    }

    private static class Place {
        final String name, country;
        final double latitude, longitude;

        Place(String name, String country, double lat, double lon) {
            this.name = name;
            this.country = country;
            this.latitude = lat;
            this.longitude = lon;
        }

        @Override
        public String toString() {
            return name + " (" + country + ")";
        }
    }
}