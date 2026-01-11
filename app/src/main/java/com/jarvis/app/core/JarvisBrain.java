package com.jarvis.app.core;

import android.content.Context;
import com.jarvis.app.ai.IntentFilter;
import com.jarvis.app.ai.WorkflowEngine;

public class JarvisBrain implements SpeechEngine.SpeechEngineListener {

    private final Context context;
    private final MemoryStore memoryStore;
    private final SpeechEngine speechEngine;
    private final EmotionStateManager emotionStateManager;
    private final CommandRouter commandRouter;
    private final IntentFilter intentFilter;
    private final WorkflowEngine workflowEngine;
    private final BrainCallback callback;

    public JarvisBrain(Context context, BrainCallback callback) {
        this.context = context;
        this.callback = callback;
        this.memoryStore = new MemoryStore(context);
        this.speechEngine = new SpeechEngine(context, this);
        this.emotionStateManager = new EmotionStateManager();
        this.commandRouter = new CommandRouter(context);
        this.intentFilter = new IntentFilter();
        this.workflowEngine = new WorkflowEngine(context);
    }

    public void processInput(String input) {
        emotionStateManager.updateEmotion(input);

        // First, check if it's a direct command
        // A more sophisticated check would be needed here
        if (isCommand(input)) {
            commandRouter.executeCommand(input);
        } else {
            // If not a command, process with AI workflow
            IntentFilter.IntentType intent = intentFilter.classifyIntent(input);
            workflowEngine.processWorkflow(input, intent, new WorkflowEngine.WorkflowCallback() {
                @Override
                public void onResult(String result) {
                    String emotionalResult = emotionStateManager.getEmotionalResponse(result);
                    callback.onResponse(emotionalResult);
                    speak(emotionalResult);
                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                }
            });
        }
    }

    private boolean isCommand(String input) {
        String lowerInput = input.toLowerCase();
        return lowerInput.startsWith("open") || lowerInput.contains("go to"); // Simple check
    }

    public void startListening() {
        speechEngine.startListening();
        callback.onListening(true);
    }

    public void stopListening() {
        speechEngine.stopListening();
        callback.onListening(false);
    }

    public void speak(String text) {
        // Adjust TTS based on emotion
        float pitch = 1.0f;
        float rate = 1.0f;
        switch(emotionStateManager.getCurrentEmotion()) {
            case HAPPY:
            case EXCITED:
                pitch = 1.2f;
                rate = 1.1f;
                break;
            case SAD:
                pitch = 0.8f;
                rate = 0.9f;
                break;
        }
        speechEngine.speak(text, pitch, rate);
    }

    @Override
    public void onSpeechResult(String result) {
        callback.onListening(false);
        callback.onSpeechInput(result);
        processInput(result);
    }

    @Override
    public void onPartialSpeechResult(String partialResult) {
        callback.onPartialSpeech(partialResult);
    }

    @Override
    public void onError(int errorCode) {
        callback.onListening(false);
        callback.onError("Speech recognition error: " + errorCode);
    }

    public void shutdown() {
        speechEngine.shutdown();
    }

    public interface BrainCallback {
        void onResponse(String response);
        void onSpeechInput(String text);
        void onPartialSpeech(String partialText);
        void onError(String error);
        void onListening(boolean isListening);
    }
}
