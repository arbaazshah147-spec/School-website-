package com.jarvis.app.ai;

import com.google.gson.Gson;
import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import okhttp3.Callback;
import java.util.Collections;

public class ChatGPTClient {

    private final ApiClient apiClient;
    private final String apiKey;
    private final Gson gson = new Gson();

    public ChatGPTClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateCompletion(String prompt, Callback callback) {
        Message message = new Message("user", prompt);
        ChatGPTRequest requestBody = new ChatGPTRequest("gpt-3.5-turbo", Collections.singletonList(message));

        String jsonBody = gson.toJson(requestBody);

        apiClient.makeRequest(Constants.CHATGPT_API_URL, jsonBody, apiKey, callback);
    }

    // Inner classes for Gson serialization
    private static class ChatGPTRequest {
        private final String model;
        private final java.util.List<Message> messages;

        public ChatGPTRequest(String model, java.util.List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    private static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
