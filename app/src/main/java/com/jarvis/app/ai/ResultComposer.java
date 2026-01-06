package com.jarvis.app.ai;

public class ResultComposer {

    /**
     * Composes the final result by prepending the AI source.
     * @param source The AI that generated the response (e.g., "Gemini", "ChatGPT").
     * @param response The response from the AI.
     * @return A composed string.
     */
    public String compose(String source, String response) {
        // A more complex implementation could involve merging results from multiple AIs,
        // or formatting the output in a specific way.
        // For now, we'll just prepend the source for clarity.
        return "[" + source + "]: " + response;
    }
}
