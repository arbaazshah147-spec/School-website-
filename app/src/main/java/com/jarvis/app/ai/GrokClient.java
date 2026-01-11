package com.jarvis.app.ai;

import com.jarvis.app.utils.ApiClient;
import com.jarvis.app.utils.Constants;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class GrokClient {

    private final ApiClient apiClient;
    private final String apiKey;

    public GrokClient(String apiKey) {
        this.apiClient = new ApiClient();
        this.apiKey = apiKey;
    }

    public void generateSummary(String prompt, Callback callback) {
        // NOTE: This is a placeholder implementation as the Grok API is not public.
        // The request format is an assumption.
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("prompt", prompt);
            jsonBody.put("model", "grok-1");
            apiClient.makeRequest(Constants.GROK_API_URL, jsonBody.toString(), apiKey, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
