package com.jarvis.app.utils;

public class Constants {

    // API Endpoints
    public static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    public static final String CHATGPT_API_URL = "https://api.openai.com/v1/chat/completions";
    public static final String GROK_API_URL = "https://api.x.ai/v1/grok"; // Placeholder

    // SharedPreferences Keys
    public static final String PREFS_NAME = "JarvisPrefs";
    public static final String KEY_AI_NAME = "ai_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_WAKE_WORD = "wake_word";
    public static final String KEY_WELCOME_RESPONSE = "welcome_response";
    public static final String KEY_GEMINI_API_KEY = "gemini_api_key";
    public static final String KEY_CHATGPT_API_KEY = "chatgpt_api_key";
    public static final String KEY_GROK_API_KEY = "grok_api_key";
    public static final String KEY_PERSONAL_INFO = "personal_info";
    public static final String KEY_CUSTOM_COMMANDS = "custom_commands";

    // Permission Request Codes
    public static final int REQUEST_CODE_PERMISSIONS = 101;
}
