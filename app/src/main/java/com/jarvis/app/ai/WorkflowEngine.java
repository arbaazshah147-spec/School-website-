package com.jarvis.app.ai;

import android.content.Context;
import android.content.SharedPreferences;

import com.jarvis.app.utils.Constants;

public class WorkflowEngine {

    private final GeminiClient geminiClient;
    private final ChatGPTClient chatGPTClient;
    private final GrokClient grokClient;
    private final ResultComposer resultComposer;

    public interface WorkflowCallback {
        void onComplete(String result);
        void onError(String error);
    }

    public WorkflowEngine(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String geminiApiKey = prefs.getString(Constants.KEY_GEMINI_API_KEY, "");
        String chatgptApiKey = prefs.getString(Constants.KEY_CHATGPT_API_KEY, "");
        String grokApiKey = prefs.getString(Constants.KEY_GROK_API_KEY, "");

        this.geminiClient = new GeminiClient(geminiApiKey);
        this.chatGPTClient = new ChatGPTClient(chatgptApiKey);
        this.grokClient = new GrokClient(grokApiKey);
        this.resultComposer = new ResultComposer();
    }

    public void processQuery(String query, final WorkflowCallback callback) {
        IntentFilter.IntentType intent = IntentFilter.getIntent(query);

        switch (intent) {
            case PLANNING:
            case GENERAL:
                geminiClient.getResponse(query, new GeminiClient.AiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        callback.onComplete(resultComposer.compose("Gemini", response));
                    }
                    @Override
                    public void onFailure(String error) {
                        callback.onError(error);
                    }
                });
                break;

            case PERSONAL:
                chatGPTClient.getResponse(query, new ChatGPTClient.AiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        callback.onComplete(resultComposer.compose("ChatGPT", response));
                    }
                    @Override
                    public void onFailure(String error) {
                        callback.onError(error);
                    }
                });
                break;

            case FINANCE:
            case WORK:
            case UNKNOWN: // Default to Grok for analysis/summary
                 grokClient.getResponse(query, new GrokClient.AiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        callback.onComplete(resultComposer.compose("Grok", response));
                    }
                    @Override
                    public void onFailure(String error) {
                        callback.onError(error);
                    }
                });
                break;

            default:
                callback.onError("Could not determine intent for the query.");
                break;
        }
    }
}
