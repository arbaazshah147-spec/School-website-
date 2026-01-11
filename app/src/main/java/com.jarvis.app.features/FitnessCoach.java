package com.jarvis.app.features;

import java.util.Calendar;
import java.util.Random;

public class FitnessCoach {

    private final String[] workouts = {
        "Push-ups: 3 sets of 12",
        "Squats: 3 sets of 15",
        "Plank: 3 sets of 60 seconds",
        "Jumping Jacks: 3 sets of 30",
        "Lunges: 3 sets of 10 per leg"
    };

    public String getWorkoutOfTheDay() {
        // Use day of the year to get a consistent workout for the day
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Random random = new Random(dayOfYear);

        return workouts[random.nextInt(workouts.length)];
    }
}
