package com.jarvis.app.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MemoryStore {

    private static final String PREFS_NAME = "JarvisMemory";
    private static final String KEY_CONVERSATION_HISTORY = "conversation_history";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public MemoryStore(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    /**
     * Adds a message to the conversation history.
     * @param message The message to add.
     */
    public void addToHistory(String message) {
        List<String> history = getConversationHistory();
        history.add(message);
        saveConversationHistory(history);
    }

    /**
     * Retrieves the entire conversation history.
     * @return A list of strings representing the conversation history.
     */
    public List<String> getConversationHistory() {
        String jsonHistory = sharedPreferences.getString(KEY_CONVERSATION_HISTORY, "[]");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(jsonHistory, type);
    }

    /**
     * Saves the conversation history to SharedPreferences.
     * @param history The list of messages to save.
     */
    private void saveConversationHistory(List<String> history) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonHistory = gson.toJson(history);
        editor.putString(KEY_CONVERSATION_HISTORY, jsonHistory);
        editor.apply();
    }

    /**
     * Clears the conversation history.
     */
    public void clearHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CONVERSATION_HISTORY);
        editor.apply();
    }

    /**
     * Stores a generic preference value.
     * @param key The key for the preference.
     * @param value The value of the preference.
     */
    public void savePreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retrieves a generic preference value.
     * @param key The key for the preference.
     * @param defaultValue The default value to return if the key is not found.
     * @return The preference value, or the default value.
     */
    public String getPreference(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
}
