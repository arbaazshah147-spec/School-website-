package com.jarvis.app.core;

import java.util.HashMap;
import java.util.Map;

public class EmotionStateManager {

    public enum Emotion {
        NEUTRAL, HAPPY, SAD, ANGRY, EXCITED
    }

    private Emotion currentEmotion = Emotion.NEUTRAL;
    private final Map<String, Emotion> keywordEmotionMap;

    public EmotionStateManager() {
        keywordEmotionMap = new HashMap<>();
        // Simple keyword-based emotion detection
        keywordEmotionMap.put("happy", Emotion.HAPPY);
        keywordEmotionMap.put("excited", Emotion.EXCITED);
        keywordEmotionMap.put("sad", Emotion.SAD);
        keywordEmotionMap.put("angry", Emotion.ANGRY);
        keywordEmotionMap.put("great", Emotion.HAPPY);
        keywordEmotionMap.put("awesome", Emotion.EXCITED);
        keywordEmotionMap.put("terrible", Emotion.SAD);
        keywordEmotionMap.put("frustrated", Emotion.ANGRY);
    }

    public void updateEmotion(String userInput) {
        String lowerCaseInput = userInput.toLowerCase();
        for (Map.Entry<String, Emotion> entry : keywordEmotionMap.entrySet()) {
            if (lowerCaseInput.contains(entry.getKey())) {
                currentEmotion = entry.getValue();
                return; // Prioritize first matched emotion
            }
        }
        currentEmotion = Emotion.NEUTRAL; // Default to neutral if no keywords found
    }

    public Emotion getCurrentEmotion() {
        return currentEmotion;
    }

    public String getEmotionalResponse(String baseResponse) {
        switch (currentEmotion) {
            case HAPPY:
                return "That's great to hear! " + baseResponse;
            case SAD:
                return "I'm sorry to hear that. " + baseResponse;
            case ANGRY:
                return "I understand your frustration. " + baseResponse;
            case EXCITED:
                return "Awesome! " + baseResponse;
            default:
                return baseResponse;
        }
    }
}
