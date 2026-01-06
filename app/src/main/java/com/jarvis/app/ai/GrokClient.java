package com.jarvis.app.ai;

import com.google.gson.annotations.SerializedName;
import com.jarvis.app.utils.ApiClient;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class GrokClient {

    private final GrokApi grokApi;

    public GrokClient(String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    // Grok's authentication might be different (e.g., x-api-key)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).build();

        this.grokApi = ApiClient.getGrokClient().newBuilder().client(client).build().create(GrokApi.class);
    }

    // Speculative endpoint
    public interface GrokApi {
        @POST("v1/chat/completions")
        Call<GrokResponse> generateCompletion(@Body GrokRequest body);
    }

    public void getResponse(String query, final AiCallback callback) {
        // Speculative request structure
        GrokRequest request = new GrokRequest("grok-1", query, 28000);

        grokApi.generateCompletion(request).enqueue(new Callback<GrokResponse>() {
            @Override
            public void onResponse(Call<GrokResponse> call, Response<GrokResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Speculative response structure
                        String text = response.body().text;
                        callback.onSuccess(text);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse Grok response.");
                    }
                } else {
                    try {
                        callback.onFailure("Grok API Error: " + response.code() + " " + response.errorBody().string());
                    } catch (IOException e) {
                        callback.onFailure("Grok API Error: Could not read error body.");
                    }
                }
            }

            @Override
            public void onFailure(Call<GrokResponse> call, Throwable t) {
                callback.onFailure("Grok network request failed: " + t.getMessage());
            }
        });
    }

    // Request and Response POJOs based on speculative API structure
    static class GrokRequest {
        String model;
        String prompt;
        @SerializedName("max_tokens")
        int maxTokens;

        public GrokRequest(String model, String prompt, int maxTokens) {
            this.model = model;
            this.prompt = prompt;
            this.maxTokens = maxTokens;
        }
    }

    static class GrokResponse {
        String text;
        // Other potential fields
    }
}
