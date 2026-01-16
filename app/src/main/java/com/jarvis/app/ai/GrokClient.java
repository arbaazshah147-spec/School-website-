package com.jarvis.app.ai;

import android.content.Context;
import com.google.gson.Gson;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import java.util.Collections;

public class GrokClient {

    private final ApiClient apiClient;
    private final Gson gson;
    private final MemoryStore memoryStore;
    private final Context context;

    public GrokClient(Context context) {
        this.context = context;
        this.apiClient = new ApiClient(context);
        this.gson = new Gson();
        this.memoryStore = new MemoryStore(context);
    }

    public void getResponse(String query, final ApiClient.ApiCallback callback) {
        String apiKey = memoryStore.get("grok_api_key", "");
        if (apiKey.isEmpty()) {
            callback.onFailure(new Exception("Grok API key not found."));
            return;
        }
        String url = Constants.GROK_API_URL;

        // Create the request body - Note: This is a speculative structure for Grok
        Message message = new Message("user", query);
        GrokRequest request = new GrokRequest("grok-1", Collections.singletonList(message));
        String jsonBody = gson.toJson(request);

        apiClient.post(url, jsonBody, apiKey, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    // This response parsing is speculative and may need adjustment
                    GrokResponse grokResponse = gson.fromJson(response, GrokResponse.class);
                    String text = grokResponse.choices.get(0).message.content;
                    callback.onSuccess(text);
                } catch (Exception e) {
                    callback.onFailure(new Exception("Failed to parse Grok response."));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    // POJOs for Grok Request/Response (speculative, based on OpenAI's format)
    private static class GrokRequest {
        private final String model;
        private final java.util.List<Message> messages;
        public GrokRequest(String model, java.util.List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }
    private static class GrokResponse {
        public java.util.List<Choice> choices;
    }
    private static class Message {
        private final String role;
        private final String content;
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
    private static class Choice {
        public Message message;
    }
}
