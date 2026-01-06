package com.jarvis.app.features;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Context context;
    private final List<Task> tasks;

    public static class Task {
        private final String title;
        private boolean isCompleted;

        public Task(String title) {
            this.title = title;
            this.isCompleted = false;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }
    }

    public TaskManager(Context context) {
        this.context = context;
        this.tasks = new ArrayList<>();
        // In a real app, load tasks from a database or SharedPreferences here.
    }

    public void addTask(String title) {
        tasks.add(new Task(title));
        // Save tasks to persistence.
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void completeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setCompleted(true);
            // Save tasks to persistence.
        }
    }

    public String getTasksForDisplay() {
        StringBuilder sb = new StringBuilder("Your tasks:\n");
        if (tasks.isEmpty()) {
            return "You have no tasks.";
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sb.append(i + 1).append(". ").append(task.getTitle());
            if (task.isCompleted()) {
                sb.append(" (Completed)");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
