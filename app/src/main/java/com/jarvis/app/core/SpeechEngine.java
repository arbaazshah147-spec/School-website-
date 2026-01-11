package com.jarvis.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import java.util.ArrayList;
import java.util.Locale;

public class SpeechEngine {

    private final TextToSpeech tts;
    private final SpeechRecognizer stt;
    private final Intent sttIntent;
    private final Context context;

    public SpeechEngine(Context context, SpeechEngineListener listener) {
        this.context = context;

        // Initialize TTS
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        // Initialize STT
        stt = SpeechRecognizer.createSpeechRecognizer(context);
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        sttIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        stt.setRecognitionListener(new RecognitionListener() {
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
                listener.onError(error);
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    listener.onSpeechResult(matches.get(0));
                }
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
                 ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    listener.onPartialSpeechResult(matches.get(0));
                }
            }
            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    public void speak(String text, float pitch, float speechRate) {
        tts.setPitch(pitch);
        tts.setSpeechRate(speechRate);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void startListening() {
        stt.startListening(sttIntent);
    }

    public void stopListening() {
        stt.stopListening();
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (stt != null) {
            stt.destroy();
        }
    }

    public interface SpeechEngineListener {
        void onSpeechResult(String result);
        void onPartialSpeechResult(String partialResult);
        void onError(int errorCode);
    }
}
