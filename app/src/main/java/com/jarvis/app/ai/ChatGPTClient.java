package com.jarvis.app.ai;

import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import okhttp3.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatGPTClient {

    private final ApiClient apiClient;
    private final String apiKey;

    public ChatGPTClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateCompletion(String prompt, Callback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            jsonBody.put("messages", messages);

            apiClient.makeRequest(Constants.CHATGPT_API_URL, jsonBody.toString(), apiKey, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
