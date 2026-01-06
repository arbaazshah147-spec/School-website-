package com.jarvis.app.features;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class MediaController {

    private Context context;

    public MediaController(Context context) {
        this.context = context;
    }

    private void sendMediaKeyEvent(int keyCode) {
        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        context.sendBroadcast(downIntent);

        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, keyCode));
        context.sendBroadcast(upIntent);
    }

    public void play(String... args) {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY);
    }

    public void pause(String... args) {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PAUSE);
    }

    public void next(String... args) {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    public void previous(String... args) {
        sendMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }
}
