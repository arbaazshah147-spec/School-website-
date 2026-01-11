package com.jarvis.app.features;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String description) {
        tasks.add(new Task(description));
    }

    public void markTaskAsCompleted(int taskId) {
        if (taskId >= 0 && taskId < tasks.size()) {
            tasks.get(taskId).setCompleted(true);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public static class Task {
        private static int nextId = 0;
        private final int id;
        private final String description;
        private boolean isCompleted;

        public Task(String description) {
            this.id = nextId++;
            this.description = description;
            this.isCompleted = false;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }
    }
}
