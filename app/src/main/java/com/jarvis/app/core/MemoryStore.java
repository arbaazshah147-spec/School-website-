package com.jarvis.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import com.jarvis.app.utils.Constants;

public class MemoryStore {

    private final SharedPreferences sharedPreferences;

    public MemoryStore(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
}
