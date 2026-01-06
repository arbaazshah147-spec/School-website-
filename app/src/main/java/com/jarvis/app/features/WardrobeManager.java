package com.jarvis.app.features;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WardrobeManager {

    private final Context context;
    private final Map<String, List<String>> outfits;

    public WardrobeManager(Context context) {
        this.context = context;
        this.outfits = new HashMap<>();
        // In a real app, load outfits from a database or a file.
        // For now, we'll use a sample set.
        initializeSampleOutfits();
    }

    private void initializeSampleOutfits() {
        List<String> casual = new ArrayList<>();
        casual.add("T-Shirt and Jeans");
        casual.add("Polo and Chinos");
        casual.add("Hoodie and Joggers");
        outfits.put("Casual", casual);

        List<String> formal = new ArrayList<>();
        formal.add("Suit and Tie");
        formal.add("Blazer and Slacks");
        outfits.put("Formal", formal);
    }

    public String suggestOutfit(String occasion) {
        if (outfits.containsKey(occasion)) {
            List<String> occasionOutfits = outfits.get(occasion);
            if (occasionOutfits != null && !occasionOutfits.isEmpty()) {
                Random random = new Random();
                return occasionOutfits.get(random.nextInt(occasionOutfits.size()));
            }
        }
        return "I don't have a suggestion for that occasion. How about a classic T-Shirt and Jeans?";
    }

    // In a real app, you would have methods to add/remove clothing items and outfits,
    // potentially with image URIs to display visual cards.
}
