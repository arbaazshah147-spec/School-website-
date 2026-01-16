package com.jarvis.app.ai;

import android.content.Context;
import com.jarvis.app.utils.ApiClient;

public class WorkflowEngine {

    private final IntentFilter intentFilter;
    private final GeminiClient geminiClient;
    private final ChatGPTClient chatGPTClient;
    private final GrokClient grokClient;
    private final ResultComposer resultComposer;

    public interface WorkflowCallback {
        void onWorkflowComplete(String result);
        void onWorkflowError(String error);
    }

    public WorkflowEngine(Context context) {
        this.intentFilter = new IntentFilter();
        this.geminiClient = new GeminiClient(context);
        this.chatGPTClient = new ChatGPTClient(context);
        this.grokClient = new GrokClient(context);
        this.resultComposer = new ResultComposer();
    }

    public void process(String text, WorkflowCallback callback) {
        IntentFilter.Intent intent = intentFilter.detectIntent(text);

        ApiClient.ApiCallback apiCallback = new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                String composedResult = resultComposer.composeResult(response);
                callback.onWorkflowComplete(composedResult);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onWorkflowError("AI request failed: " + e.getMessage());
            }
        };

        switch (intent) {
            case PLANNING:
                geminiClient.getResponse(text, apiCallback);
                break;
            case PERSONALIZATION:
                chatGPTClient.getResponse(text, apiCallback);
                break;
            case ANALYSIS:
                grokClient.getResponse(text, apiCallback);
                break;
            default: // UNKNOWN
                geminiClient.getResponse(text, apiCallback);
                break;
        }
    }
}
