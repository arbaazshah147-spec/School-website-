package com.jarvis.app.ai;

import android.content.Context;
import com.google.gson.Gson;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import java.util.Collections;

public class GeminiClient {

    private final ApiClient apiClient;
    private final Gson gson;
    private final MemoryStore memoryStore;
    private final Context context;

    public GeminiClient(Context context) {
        this.context = context;
        this.apiClient = new ApiClient(context);
        this.gson = new Gson();
        this.memoryStore = new MemoryStore(context);
    }

    public void getResponse(String query, final ApiClient.ApiCallback callback) {
        String apiKey = memoryStore.get("gemini_api_key", "");
        if (apiKey.isEmpty()) {
            callback.onFailure(new Exception("Gemini API key not found."));
            return;
        }
        String url = Constants.GEMINI_API_URL + "?key=" + apiKey;

        // Create the request body
        Part part = new Part(query);
        Content content = new Content(Collections.singletonList(part));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(content));
        String jsonBody = gson.toJson(request);

        apiClient.post(url, jsonBody, apiKey, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    GeminiResponse geminiResponse = gson.fromJson(response, GeminiResponse.class);
                    String text = geminiResponse.candidates.get(0).content.parts.get(0).text;
                    callback.onSuccess(text);
                } catch (Exception e) {
                    callback.onFailure(new Exception("Failed to parse Gemini response."));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    // POJOs for Gemini Request/Response
    private static class GeminiRequest {
        private final java.util.List<Content> contents;
        public GeminiRequest(java.util.List<Content> contents) { this.contents = contents; }
    }
    private static class GeminiResponse {
        public java.util.List<Candidate> candidates;
    }
    private static class Content {
        private final java.util.List<Part> parts;
        public Content(java.util.List<Part> parts) { this.parts = parts; }
    }
    private static class Candidate {
        public Content content;
    }
    private static class Part {
        private final String text;
        public Part(String text) { this.text = text; }
    }
}
