package com.jarvis.app.ai;

import com.jarvis.app.utils.ApiClient;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class GeminiClient {

    private final ApiClient apiClient;
    private final String apiKey;

    public GeminiClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateContent(String prompt, Callback callback) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        try {
            JSONObject jsonBody = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject partsContainer = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject textPart = new JSONObject();

            textPart.put("text", prompt);
            parts.put(textPart);
            partsContainer.put("parts", parts);
            contents.put(partsContainer);
            jsonBody.put("contents", contents);

            apiClient.makeRequest(url, jsonBody.toString(), "", callback); // API key is in URL for Gemini
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
