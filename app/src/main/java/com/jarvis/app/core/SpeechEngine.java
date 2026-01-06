package com.jarvis.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechEngine implements TextToSpeech.OnInitListener {

    private static final String TAG = "SpeechEngine";

    private final SpeechRecognizer speechRecognizer;
    private final TextToSpeech textToSpeech;
    private final Intent speechRecognizerIntent;
    private final SpeechEngineListener listener;
    private boolean isTtsInitialized = false;

    public interface SpeechEngineListener {
        void onSpeechResult(String result);
        void onSpeechError(String error);
        void onTtsInitialized();
        void onTtsSpoken();
    }

    public SpeechEngine(Context context, SpeechEngineListener listener) {
        this.listener = listener;

        // Initialize STT
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        // Add Hindi support
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US,hi-IN");


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { Log.d(TAG, "onReadyForSpeech"); }
            @Override
            public void onBeginningOfSpeech() { Log.d(TAG, "onBeginningOfSpeech"); }
            @Override
            public void onRmsChanged(float rmsdB) { /* Do nothing */ }
            @Override
            public void onBufferReceived(byte[] buffer) { /* Do nothing */ }
            @Override
            public void onEndOfSpeech() { Log.d(TAG, "onEndOfSpeech"); }
            @Override
            public void onError(int error) {
                listener.onSpeechError("STT Error: " + error);
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    listener.onSpeechResult(matches.get(0));
                }
            }
            @Override
            public void onPartialResults(Bundle partialResults) { /* Do nothing */ }
            @Override
            public void onEvent(int eventType, Bundle params) { /* Do nothing */ }
        });

        // Initialize TTS
        textToSpeech = new TextToSpeech(context, this);
    }

    public void startListening() {
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public void speak(String text, Locale locale) {
        if (isTtsInitialized) {
            textToSpeech.setLanguage(locale);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "JarvisUtterance");
        } else {
            Log.e(TAG, "TTS not initialized.");
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true;
            // Set default language to English
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "English is not supported by TTS engine.");
            } else {
                listener.onTtsInitialized();
            }
        } else {
            Log.e(TAG, "TTS Initialization failed.");
        }
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
