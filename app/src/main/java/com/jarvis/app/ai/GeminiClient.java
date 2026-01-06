package com.jarvis.app.ai;

import com.google.gson.annotations.SerializedName;
import com.jarvis.app.utils.ApiClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class GeminiClient {

    private final GeminiApi geminiApi;
    private final String apiKey;

    public GeminiClient(String apiKey) {
        this.apiKey = apiKey;
        this.geminiApi = ApiClient.getGeminiClient().create(GeminiApi.class);
    }

    public interface GeminiApi {
        @POST("v1beta/models/gemini-pro:generateContent")
        Call<GeminiResponse> generateContent(@Query("key") String apiKey, @Body GeminiRequest body);
    }

    public void getResponse(String query, final AiCallback callback) {
        Content content = new Content(Collections.singletonList(new Part(query)));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(content));

        geminiApi.generateContent(apiKey, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String text = response.body().candidates.get(0).content.parts.get(0).text;
                        callback.onSuccess(text);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse Gemini response.");
                    }
                } else {
                    try {
                        callback.onFailure("Gemini API Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        callback.onFailure("Gemini API Error: Could not read error body.");
                    }
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                callback.onFailure("Gemini network request failed: " + t.getMessage());
            }
        });
    }

    // Request and Response POJOs
    static class GeminiRequest {
        @SerializedName("contents")
        List<Content> contents;

        public GeminiRequest(List<Content> contents) {
            this.contents = contents;
        }
    }

    static class GeminiResponse {
        @SerializedName("candidates")
        List<Candidate> candidates;
    }

    static class Content {
        @SerializedName("parts")
        List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }
    }

    static class Candidate {
        @SerializedName("content")
        Content content;
    }

    static class Part {
        @SerializedName("text")
        String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
