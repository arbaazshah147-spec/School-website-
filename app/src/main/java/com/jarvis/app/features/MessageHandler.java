package com.jarvis.app.features;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class MessageHandler {
    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    public void sendMessage(String... args) {
        if (args.length < 2) {
            Toast.makeText(context, "Please specify a recipient and a message.", Toast.LENGTH_SHORT).show();
            return;
        }
        String recipient = args[0];
        String message = "";
        for (int i = 1; i < args.length; i++) {
            message += args[i] + " ";
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + recipient));
        intent.putExtra("sms_body", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
