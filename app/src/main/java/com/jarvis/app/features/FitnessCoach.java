package com.jarvis.app.features;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FitnessCoach {

    private final Context context;
    private final Map<String, String> workoutPlans;

    public FitnessCoach(Context context) {
        this.context = context;
        this.workoutPlans = new HashMap<>();
        initializeWorkoutPlans();
    }

    private void initializeWorkoutPlans() {
        workoutPlans.put("Beginner", "30 minutes of brisk walking or jogging.");
        workoutPlans.put("Intermediate", "45 minutes of circuit training: push-ups, squats, and planks.");
        workoutPlans.put("Advanced", "60 minutes of high-intensity interval training (HIIT).");
    }

    public String suggestWorkout(String level) {
        return workoutPlans.getOrDefault(level, "I don't have a plan for that level. How about a 30-minute walk?");
    }

    public String getGeneralTip() {
        String[] tips = {
                "Remember to stay hydrated throughout the day.",
                "Stretching before and after your workout is important.",
                "Consistency is key to achieving your fitness goals.",
                "A balanced diet is just as important as exercise."
        };
        Random random = new Random();
        return tips[random.nextInt(tips.length)];
    }
}
