package com.jarvis.app.ai;

import java.util.Arrays;
import java.util.List;

public class IntentFilter {

    public enum IntentType {
        FINANCE,
        WORK,
        PERSONAL,
        PLANNING,
        GENERAL,
        UNKNOWN
    }

    // Simple keyword-based intent classification
    public static IntentType getIntent(String query) {
        String lowerCaseQuery = query.toLowerCase();

        // Keywords for each intent type
        List<String> financeKeywords = Arrays.asList("money", "expense", "budget", "stock", "market", "finance", "price", "cost");
        List<String> workKeywords = Arrays.asList("meeting", "email", "schedule", "deadline", "project", "task", "work", "office");
        List<String> personalKeywords = Arrays.asList("note", "reminder", "call", "message", "friend", "family", "personal");
        List<String> planningKeywords = Arrays.asList("plan", "schedule", "what's my day", "itinerary", "book", "reserve");

        if (containsKeyword(lowerCaseQuery, financeKeywords)) {
            return IntentType.FINANCE;
        } else if (containsKeyword(lowerCaseQuery, workKeywords)) {
            return IntentType.WORK;
        } else if (containsKeyword(lowerCaseQuery, personalKeywords)) {
            return IntentType.PERSONAL;
        } else if (containsKeyword(lowerCaseQuery, planningKeywords)) {
            return IntentType.PLANNING;
        } else {
            // More sophisticated NLP would be needed for better classification.
            // For now, if no specific keywords are found, we can classify it as GENERAL.
            // Or we could try to identify questions.
            if (lowerCaseQuery.startsWith("who") || lowerCaseQuery.startsWith("what") ||
                lowerCaseQuery.startsWith("where") || lowerCaseQuery.startsWith("when") ||
                lowerCaseQuery.startsWith("why") || lowerCaseQuery.startsWith("how") ||
                lowerCaseQuery.contains("?")) {
                return IntentType.GENERAL;
            }
        }

        return IntentType.UNKNOWN;
    }

    private static boolean containsKeyword(String text, List<String> keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
