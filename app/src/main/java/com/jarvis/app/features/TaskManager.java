package com.jarvis.app.features;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private static final String TASKS_FILE = "tasks.json";
    private final Context context;
    private List<Task> tasks;

    public TaskManager(Context context) {
        this.context = context;
        loadTasks();
    }

    private void loadTasks() {
        try {
            File file = new File(context.getFilesDir(), TASKS_FILE);
            if (!file.exists()) {
                this.tasks = new ArrayList<>();
                return;
            }
            FileReader reader = new FileReader(file);
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            this.tasks = new Gson().fromJson(reader, type);
            reader.close();

            // Set the nextId to be one greater than the highest id in the loaded tasks
            if (this.tasks != null && !this.tasks.isEmpty()) {
                Task.setNextId(this.tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1);
            } else {
                 this.tasks = new ArrayList<>();
            }

        } catch (IOException e) {
            e.printStackTrace();
            this.tasks = new ArrayList<>(); // Start fresh in case of an error
        }
    }

    private void saveTasks() {
        try {
            File file = new File(context.getFilesDir(), TASKS_FILE);
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(this.tasks, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String description) {
        tasks.add(new Task(description));
        saveTasks();
    }

    public void deleteTask(int taskId) {
        tasks.removeIf(task -> task.getId() == taskId);
        saveTasks();
    }

    public void markTaskAsCompleted(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setCompleted(true);
                saveTasks();
                return;
            }
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

        public static void setNextId(int id) {
            nextId = id;
        }
    }
}
