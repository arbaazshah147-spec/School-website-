package com.jarvis.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class WakeWordEngine {

    private static final String TAG = "WakeWordEngine";

    private final Context context;
    private final SpeechRecognizer speechRecognizer;
    private final WakeWordListener listener;
    private String wakeWord;

    public interface WakeWordListener {
        void onWakeWordDetected();
        void onError(String error);
    }

    public WakeWordEngine(Context context, String wakeWord, WakeWordListener listener) {
        this.context = context;
        this.wakeWord = wakeWord.toLowerCase();
        this.listener = listener;

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onError(int error) {
                // Restart listening on error, unless it's a "no match" error, which is common.
                if (error != SpeechRecognizer.ERROR_NO_MATCH) {
                    Log.e(TAG, "STT Error: " + error);
                    listener.onError("Wake word engine error: " + error);
                }
                startListening();
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    for (String match : matches) {
                        if (match.toLowerCase().contains(wakeWord)) {
                            listener.onWakeWordDetected();
                            return; // Stop processing once wake word is found
                        }
                    }
                }
                // If wake word is not found, start listening again.
                startListening();
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 if (matches != null) {
                    for (String match : matches) {
                        if (match.toLowerCase().contains(wakeWord)) {
                            listener.onWakeWordDetected();
                            return;
                        }
                    }
                }
            }
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechRecognizer.startListening(intent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public void setWakeWord(String wakeWord) {
        this.wakeWord = wakeWord.toLowerCase();
    }
}
