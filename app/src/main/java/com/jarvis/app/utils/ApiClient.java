package com.jarvis.app.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import java.io.IOException;

public class ApiClient {

    private final OkHttpClient client;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ApiClient() {
        this.client = new OkHttpClient();
    }

    public void makeRequest(String url, String json, String apiKey, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
