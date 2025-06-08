package com.example.demo6;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.ContentDisplay;
import javafx.util.Callback;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField taskInput;

    @FXML
    private ListView<Task> taskList;

    @FXML
    private Button addButton;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    private int editingIndex = -1;

    @FXML
    public void initialize() {
        taskList.setItems(tasks);
        taskList.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> listView) {
                return new TaskCell();
            }
        });
    }

    @FXML
    protected void onAddTask() {
        String taskText = taskInput.getText();
        if (taskText != null && !taskText.trim().isEmpty()) {
            if (editingIndex >= 0) {
                Task task = tasks.get(editingIndex);
                task.setText(taskText);
                tasks.set(editingIndex, task);
                editingIndex = -1;
                addButton.setText("Add Task");
            } else {
                tasks.add(new Task(taskText));
            }
            taskInput.clear();
        }
    }

    @FXML
    protected void onClearCompleted() {
        tasks.removeIf(Task::isCompleted);
        if (editingIndex >= 0 && (editingIndex >= tasks.size() || tasks.get(editingIndex).isCompleted())) {
            editingIndex = -1;
            addButton.setText("Add Task");
            taskInput.clear();
        }
    }

    private class TaskCell extends ListCell<Task> {
        private HBox hbox = new HBox(10);
        private CheckBox checkBox = new CheckBox();
        private Label label = new Label();
        private Button editButton = new Button("Edit");
        private Button deleteButton = new Button("Delete");

        public TaskCell() {
            super();
            hbox.getChildren().addAll(checkBox, label, editButton, deleteButton);

            checkBox.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setCompleted(checkBox.isSelected());
                    updateItem(task, false);
                }
            });

            editButton.setOnAction(event -> {
                editingIndex = getIndex();
                Task task = getItem();
                if (task != null) {
                    taskInput.setText(task.getText());
                    addButton.setText("Save Task");
                }
            });

            deleteButton.setOnAction(event -> {
                int index = getIndex();
                if (index >= 0 && index < tasks.size()) {
                    tasks.remove(index);
                    if (editingIndex == index) {
                        editingIndex = -1;
                        addButton.setText("Add Task");
                        taskInput.clear();
                    }
                }
            });
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                label.setText(task.getText());
                label.setStyle(task.isCompleted() ? "-fx-strikethrough: true; -fx-text-fill: gray;" : "");
                checkBox.setSelected(task.isCompleted());
                setGraphic(hbox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
        }
    }

    public static class Task {
        private String text;
        private boolean completed;

        public Task(String text) {
            this.text = text;
            this.completed = false;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
