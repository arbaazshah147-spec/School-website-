package com.jarvis.app.ai;

import com.google.gson.annotations.SerializedName;
import com.jarvis.app.utils.ApiClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ChatGPTClient {

    private final ChatGPTApi chatGPTApi;

    public ChatGPTClient(String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).build();

        this.chatGPTApi = ApiClient.getChatGPTClient().newBuilder().client(client).build().create(ChatGPTApi.class);
    }

    public interface ChatGPTApi {
        @POST("v1/chat/completions")
        Call<ChatGPTResponse> generateCompletion(@Body ChatGPTRequest body);
    }

    public void getResponse(String query, final AiCallback callback) {
        Message message = new Message("user", query);
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", Collections.singletonList(message));

        chatGPTApi.generateCompletion(request).enqueue(new Callback<ChatGPTResponse>() {
            @Override
            public void onResponse(Call<ChatGPTResponse> call, Response<ChatGPTResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String text = response.body().choices.get(0).message.content;
                        callback.onSuccess(text);
                    } catch (Exception e) {
                        callback.onFailure("Failed to parse ChatGPT response.");
                    }
                } else {
                    try {
                        callback.onFailure("ChatGPT API Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        callback.onFailure("ChatGPT API Error: Could not read error body.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatGPTResponse> call, Throwable t) {
                callback.onFailure("ChatGPT network request failed: " + t.getMessage());
            }
        });
    }

    // Request and Response POJOs
    static class ChatGPTRequest {
        String model;
        List<Message> messages;

        public ChatGPTRequest(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    static class ChatGPTResponse {
        List<Choice> choices;
    }

    static class Message {
        String role;
        String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    static class Choice {
        Message message;
    }
}
