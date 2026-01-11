package com.jarvis.app.ai;

import com.google.gson.Gson;
import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import okhttp3.Callback;

public class GrokClient {

    private final ApiClient apiClient;
    private final String apiKey;
    private final Gson gson = new Gson();

    public GrokClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateSummary(String prompt, Callback callback) {
        // NOTE: This is a placeholder implementation as the Grok API is not public.
        // The request format is an assumption.
        GrokRequest requestBody = new GrokRequest("grok-1", prompt);
        String jsonBody = gson.toJson(requestBody);

        apiClient.makeRequest(Constants.GROK_API_URL, jsonBody, apiKey, callback);
    }

    // Inner class for Gson serialization
    private static class GrokRequest {
        private final String model;
        private final String prompt;

        public GrokRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
        }
    }
}
