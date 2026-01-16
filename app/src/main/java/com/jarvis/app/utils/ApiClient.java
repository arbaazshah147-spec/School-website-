package com.jarvis.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    private final OkHttpClient client;
    private final Context context;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ApiClient(Context context) {
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Constants.API_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(Constants.API_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(Constants.API_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void post(String url, String json, String apiKey, final ApiCallback callback) {
        if (!isNetworkAvailable()) {
            callback.onFailure(new IOException("No internet connection"));
            return;
        }

        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);

        // Gemini uses a URL param for the key, others use a Bearer token
        if (!url.contains("generativelanguage.googleapis.com")) {
            requestBuilder.addHeader("Authorization", "Bearer " + apiKey);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    callback.onFailure(new IOException("API Error: " + response.code() + " " + errorBody));
                }
            }
        });
    }

    public interface ApiCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }
}
