package com.jarvis.app.ai;

public interface AiCallback {
    void onSuccess(String response);
    void onFailure(String error);
}
