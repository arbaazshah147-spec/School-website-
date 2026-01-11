package com.jarvis.app.features;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareManager {

    private final Context context;

    public ShareManager(Context context) {
        this.context = context;
    }

    public void shareText(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, "Share Text"));
    }

    public void shareMedia(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Media"));
    }
}
