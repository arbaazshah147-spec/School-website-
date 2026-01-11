package com.jarvis.app.features;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MessageHandler {

    private final Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    public void sendMessage(String to, String message) {
        // This is a simplified implementation. A real implementation would
        // allow the user to choose the messaging app.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + to));
        intent.putExtra("sms_body", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
