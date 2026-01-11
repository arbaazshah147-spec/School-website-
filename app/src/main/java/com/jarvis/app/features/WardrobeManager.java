package com.jarvis.app.features;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class WardrobeManager {

    private final List<Outfit> outfits;

    public WardrobeManager() {
        this.outfits = new ArrayList<>();
        // Predefined outfits for demonstration
        outfits.add(new Outfit("Casual", "T-Shirt and Jeans", "sneakers"));
        outfits.add(new Outfit("Formal", "Suit and Tie", "dress_shoes"));
        outfits.add(new Outfit("Sporty", "Tracksuit", "running_shoes"));
        outfits.add(new Outfit("Business Casual", "Polo Shirt and Chinos", "loafers"));
        outfits.add(new Outfit("Winter Wear", "Jacket and Beanie", "boots"));
        outfits.add(new Outfit("Summer Vibe", "Shorts and Vest", "sandals"));
    }

    public List<Outfit> getDailySuggestions() {
        List<Outfit> suggestions = new ArrayList<>();

        // Use the day of the year to seed the random number generator
        // for consistent daily suggestions.
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        Random random = new Random(dayOfYear);

        int numOutfits = outfits.size();
        if (numOutfits < 3) {
            return outfits; // Return all if less than 3
        }

        while (suggestions.size() < 3) {
            Outfit suggestion = outfits.get(random.nextInt(numOutfits));
            if (!suggestions.contains(suggestion)) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    public static class Outfit {
        private final String style;
        private final String description;
        private final String drawableName; // To link with a visual representation

        public Outfit(String style, String description, String drawableName) {
            this.style = style;
            this.description = description;
            this.drawableName = drawableName;
        }

        public String getStyle() {
            return style;
        }

        public String getDescription() {
            return description;
        }

        public String getDrawableName() {
            return drawableName;
        }
    }
}
