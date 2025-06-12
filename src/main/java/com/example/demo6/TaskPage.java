package com.example.demo6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class TaskPage extends VBox {
    private final TextField taskInput = new TextField();
    private final Button addButton = new Button("Add Task");
    private final Button clearCompletedButton = new Button("Clear Completed");
    private final ListView<Task> taskList = new ListView<>();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private int editingIndex = -1;

    public TaskPage() {
        Label title = new Label("Task List");
        title.getStyleClass().add("title-label");
        this.getStyleClass().add("vbox");
        taskInput.getStyleClass().add("text-field");
        addButton.getStyleClass().add("button");
        clearCompletedButton.getStyleClass().add("button");
        taskList.getStyleClass().add("list-view");

        HBox inputBox = new HBox(10, taskInput, addButton, clearCompletedButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setSpacing(10);
        inputBox.getStyleClass().add("hbox");

        taskList.setItems(tasks);
        taskList.setCellFactory(listView -> new TaskCell());

        addButton.setOnAction(e -> onAddTask());
        clearCompletedButton.setOnAction(e -> onClearCompleted());

        this.setSpacing(15);
        this.setPadding(new Insets(25));
        this.getChildren().addAll(title, inputBox, taskList);
    }

    private void onAddTask() {
        String taskText = taskInput.getText();
        if (taskText != null && !taskText.trim().isEmpty()) {
            if (editingIndex >= 0) {
                Task task = tasks.get(editingIndex);
                task.setText(taskText);
                tasks.set(editingIndex, task);
                editingIndex = -1;
                addButton.setText("Add");
            } else {
                tasks.add(new Task(taskText));
            }
            taskInput.clear();
        }
    }

    private void onClearCompleted() {
        tasks.removeIf(Task::isCompleted);
        if (editingIndex >= 0 && (editingIndex >= tasks.size() || tasks.get(editingIndex).isCompleted())) {
            editingIndex = -1;
            addButton.setText("Add");
            taskInput.clear();
        }
    }

    private class TaskCell extends ListCell<Task> {
        private final HBox hbox = new HBox(10);
        private final CheckBox checkBox = new CheckBox();
        private final Label label = new Label();
        private final Button editButton = new Button("Edit");
        private final Button deleteButton = new Button("Delete");

        public TaskCell() {
            editButton.getStyleClass().add("button");
            deleteButton.getStyleClass().addAll("button", "delete");

            hbox.getChildren().addAll(checkBox, label, editButton, deleteButton);
            hbox.getStyleClass().add("hbox");

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
                    addButton.setText("Save");
                }
            });

            deleteButton.setOnAction(event -> {
                int index = getIndex();
                if (index >= 0 && index < tasks.size()) {
                    tasks.remove(index);
                    if (editingIndex == index) {
                        editingIndex = -1;
                        addButton.setText("Add");
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