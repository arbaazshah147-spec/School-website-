package com.jarvis.app.ai;

import android.content.Context;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.Constants;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class WorkflowEngine {

    private final GeminiClient geminiClient;
    private final ChatGPTClient chatGPTClient;
    private final GrokClient grokClient;
    private final ResultComposer resultComposer;

    public WorkflowEngine(Context context) {
        MemoryStore memoryStore = new MemoryStore(context);
        String geminiKey = memoryStore.getData(Constants.KEY_GEMINI_API_KEY, "");
        String chatgptKey = memoryStore.getData(Constants.KEY_CHATGPT_API_KEY, "");
        String grokKey = memoryStore.getData(Constants.KEY_GROK_API_KEY, "");

        this.geminiClient = new GeminiClient(geminiKey);
        this.chatGPTClient = new ChatGPTClient(chatgptKey);
        this.grokClient = new GrokClient(grokKey);
        this.resultComposer = new ResultComposer();
    }

    public void processWorkflow(String input, IntentFilter.IntentType intent, WorkflowCallback callback) {
        // For simplicity, this example calls all three and composes.
        // A more advanced implementation would conditionally call them based on intent.

        final String[] results = new String[3]; // 0: Gemini, 1: ChatGPT, 2: Grok
        final int[] completedCalls = {0};
        int totalCalls = 0;

        // Planning / factual -> Gemini
        if(intent == IntentFilter.IntentType.PLAN || intent == IntentFilter.IntentType.UNKNOWN){
            totalCalls++;
            geminiClient.generateContent(input, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handleFailure(callback, completedCalls, totalCalls, results);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        results[0] = resultComposer.parseGeminResponse(response.body().string());
                    }
                    handleResponse(callback, completedCalls, totalCalls, results);
                }
            });
        }


        // Personalization -> ChatGPT
        if(intent == IntentFilter.IntentType.PERSONAL || intent == IntentFilter.IntentType.UNKNOWN){
            totalCalls++;
            chatGPTClient.generateCompletion(input, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handleFailure(callback, completedCalls, totalCalls, results);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        results[1] = resultComposer.parseChatGPTResponse(response.body().string());
                    }
                    handleResponse(callback, completedCalls, totalCalls, results);
                }
            });
        }


        // Analysis / summary -> Grok
        if(intent == IntentFilter.IntentType.FINANCE || intent == IntentFilter.IntentType.WORK || intent == IntentFilter.IntentType.UNKNOWN) {
            totalCalls++;
            grokClient.generateSummary(input, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handleFailure(callback, completedCalls, totalCalls, results);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        results[2] = resultComposer.parseGrokResponse(response.body().string());
                    }
                    handleResponse(callback, completedCalls, totalCalls, results);
                }
            });
        }

        if(totalCalls == 0){
             callback.onResult("I'm not sure how to handle that.");
        }
    }

    private synchronized void handleResponse(WorkflowCallback callback, int[] completedCalls, int totalCalls, String[] results) {
        completedCalls[0]++;
        if (completedCalls[0] == totalCalls) {
            callback.onResult(resultComposer.composeResult(results[0], results[1], results[2]));
        }
    }

    private synchronized void handleFailure(WorkflowCallback callback, int[] completedCalls, int totalCalls, String[] results) {
        completedCalls[0]++;
        if (completedCalls[0] == totalCalls) {
             callback.onResult(resultComposer.composeResult(results[0], results[1], results[2]));
        }
    }


    public interface WorkflowCallback {
        void onResult(String result);
        void onError(String error);
    }
}
