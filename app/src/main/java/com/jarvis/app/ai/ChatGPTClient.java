package com.jarvis.app.ai;

import android.content.Context;
import com.google.gson.Gson;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import java.util.Collections;

public class ChatGPTClient {

    private final ApiClient apiClient;
    private final Gson gson;
    private final MemoryStore memoryStore;
    private final Context context;

    public ChatGPTClient(Context context) {
        this.context = context;
        this.apiClient = new ApiClient(context);
        this.gson = new Gson();
        this.memoryStore = new MemoryStore(context);
    }

    public void getResponse(String query, final ApiClient.ApiCallback callback) {
        String apiKey = memoryStore.get("chatgpt_api_key", "");
        if (apiKey.isEmpty()) {
            callback.onFailure(new Exception("ChatGPT API key not found."));
            return;
        }
        String url = Constants.CHATGPT_API_URL;

        // Create the request body
        Message message = new Message("user", query);
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", Collections.singletonList(message));
        String jsonBody = gson.toJson(request);

        apiClient.post(url, jsonBody, apiKey, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    ChatGPTResponse chatGPTResponse = gson.fromJson(response, ChatGPTResponse.class);
                    String text = chatGPTResponse.choices.get(0).message.content;
                    callback.onSuccess(text);
                } catch (Exception e) {
                    callback.onFailure(new Exception("Failed to parse ChatGPT response."));
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    // POJOs for ChatGPT Request/Response
    private static class ChatGPTRequest {
        private final String model;
        private final java.util.List<Message> messages;
        public ChatGPTRequest(String model, java.util.List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }
    private static class ChatGPTResponse {
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
