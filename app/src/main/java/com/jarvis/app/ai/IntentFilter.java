package com.jarvis.app.ai;

public class IntentFilter {

    public enum IntentType {
        FINANCE, WORK, PERSONAL, PLAN, UNKNOWN
    }

    public IntentType classifyIntent(String userInput) {
        String lowerCaseInput = userInput.toLowerCase();

        // Simple keyword-based intent classification
        if (lowerCaseInput.contains("stock") || lowerCaseInput.contains("market") || lowerCaseInput.contains("price")) {
            return IntentType.FINANCE;
        } else if (lowerCaseInput.contains("schedule") || lowerCaseInput.contains("meeting") || lowerCaseInput.contains("email")) {
            return IntentType.WORK;
        } else if (lowerCaseInput.contains("how are you") || lowerCaseInput.contains("tell me a joke")) {
            return IntentType.PERSONAL;
        } else if (lowerCaseInput.contains("plan") || lowerCaseInput.contains("what should i do")) {
            return IntentType.PLAN;
        }

        return IntentType.UNKNOWN;
    }
}
