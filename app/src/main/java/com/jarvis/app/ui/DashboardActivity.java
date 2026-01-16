package com.jarvis.app.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.jarvis.app.R;
import com.jarvis.app.features.FitnessCoach;
import com.jarvis.app.features.NotesManager;
import com.jarvis.app.features.TaskManager;
import com.jarvis.app.features.WardrobeManager;

public class DashboardActivity extends AppCompatActivity {

    private TextView tasksContent, notesContent, wardrobeContent, fitnessContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tasksContent = findViewById(R.id.tasks_content);
        notesContent = findViewById(R.id.notes_content);
        wardrobeContent = findViewById(R.id.wardrobe_content);
        fitnessContent = findViewById(R.id.fitness_content);

        loadDashboardData();
    }

    private void loadDashboardData() {
        // Task Manager
        TaskManager taskManager = new TaskManager(this);
        StringBuilder taskBuilder = new StringBuilder("Tasks:\n");
        for (TaskManager.Task task : taskManager.getTasks()) {
            taskBuilder.append("- ").append(task.getTitle()).append("\n");
        }
        tasksContent.setText(taskBuilder.toString());

        // Notes Manager
        NotesManager notesManager = new NotesManager(this);
        StringBuilder notesBuilder = new StringBuilder("Notes:\n");
        for (NotesManager.Note note : notesManager.getNotes()) {
            notesBuilder.append("- ").append(note.getContent()).append("\n");
        }
        notesContent.setText(notesBuilder.toString());

        // Wardrobe Manager
        WardrobeManager wardrobeManager = new WardrobeManager();
        StringBuilder wardrobeBuilder = new StringBuilder("Wardrobe Suggestions:\n");
        for (WardrobeManager.Outfit outfit : wardrobeManager.getWardrobeSuggestions()) {
            wardrobeBuilder.append("- ").append(outfit.getTop()).append(", ").append(outfit.getBottom()).append("\n");
        }
        wardrobeContent.setText(wardrobeBuilder.toString());

        // Fitness Coach
        FitnessCoach fitnessCoach = new FitnessCoach();
        FitnessCoach.FitnessTip fitnessTip = fitnessCoach.getDailyFitnessTip();
        String fitnessString = "Fitness Tip:\n- Workout: " + fitnessTip.getWorkout() + "\n- Mindfulness: " + fitnessTip.getMindfulness();
        fitnessContent.setText(fitnessString);
    }
}
