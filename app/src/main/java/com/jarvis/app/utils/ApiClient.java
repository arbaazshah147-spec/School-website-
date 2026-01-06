package com.jarvis.app.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit geminiRetrofit = null;
    private static Retrofit chatgptRetrofit = null;
    private static Retrofit grokRetrofit = null;

    public static Retrofit getGeminiClient() {
        if (geminiRetrofit == null) {
            geminiRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GEMINI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return geminiRetrofit;
    }

    public static Retrofit getChatGPTClient() {
        if (chatgptRetrofit == null) {
            chatgptRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.CHATGPT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return chatgptRetrofit;
    }

    public static Retrofit getGrokClient() {
        if (grokRetrofit == null) {
            grokRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GROK_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return grokRetrofit;
    }
}
