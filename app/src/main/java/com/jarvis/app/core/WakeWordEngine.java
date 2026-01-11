package com.jarvis.app.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class WakeWordEngine {

    private final WakeWordListener listener;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public WakeWordEngine(Context context, String wakeWord, WakeWordListener listener) {
        // The context and wakeWord are no longer used in this simulated version,
        // but are kept for API compatibility with a future, real implementation.
        this.listener = listener;
    }

    public void startListening() {
        // In this simulated version, startListening() does nothing.
        // The wake word is triggered manually.
    }

    public void stopListening() {
        // In this simulated version, stopListening() does nothing.
    }

    /**
     * Simulates the detection of a wake word. In a real implementation,
     * this would be triggered by an on-device model.
     */
    public void triggerWakeWord() {
        handler.post(() -> listener.onWakeWordDetected());
    }

    public interface WakeWordListener {
        void onWakeWordDetected();
    }
}
