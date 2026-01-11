package com.jarvis.app.ai;

import com.google.gson.Gson;
import com.jarvis.app.utils.ApiClient;
import okhttp3.Callback;
import java.util.Collections;

public class GeminiClient {

    private final ApiClient apiClient;
    private final String apiKey;
    private final Gson gson = new Gson();

    public GeminiClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateContent(String prompt, Callback callback) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        TextPart textPart = new TextPart(prompt);
        Content content = new Content(Collections.singletonList(textPart));
        GeminiRequest requestBody = new GeminiRequest(Collections.singletonList(content));

        String jsonBody = gson.toJson(requestBody);

        apiClient.makeRequest(url, jsonBody, "", callback); // API key is in URL for Gemini
    }

    // Inner classes for Gson serialization
    private static class GeminiRequest {
        private final java.util.List<Content> contents;

        public GeminiRequest(java.util.List<Content> contents) {
            this.contents = contents;
        }
    }

    private static class Content {
        private final java.util.List<TextPart> parts;

        public Content(java.util.List<TextPart> parts) {
            this.parts = parts;
        }
    }

    private static class TextPart {
        private final String text;

        public TextPart(String text) {
            this.text = text;
        }
    }
}
