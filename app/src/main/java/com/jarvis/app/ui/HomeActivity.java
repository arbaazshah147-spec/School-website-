package com.jarvis.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.app.R;
import com.jarvis.app.core.CommandRouter;
import com.jarvis.app.core.JarvisBrain;
import com.jarvis.app.core.SpeechEngine;
import com.jarvis.app.core.WakeWordEngine;
import com.jarvis.app.utils.Constants;
import com.jarvis.app.utils.PermissionUtil;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements
        JarvisBrain.BrainCallback,
        SpeechEngine.SpeechEngineListener,
        WakeWordEngine.WakeWordListener,
        CommandRouter.CommandRouterListener {

    private JarvisBrain jarvisBrain;
    private SpeechEngine speechEngine;
    private WakeWordEngine wakeWordEngine;
    private TextView welcomeMessage, statusText;
    private EditText inputText;
    private ImageView micRing, settingsButton;

    private ActivityResultLauncher<Intent> settingsLauncher;
    private boolean isListeningForCommand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!PermissionUtil.allPermissionsGranted(this)) {
            PermissionUtil.requestMissingPermissions(this);
        }

        jarvisBrain = new JarvisBrain(this, this);
        speechEngine = new SpeechEngine(this, this);

        welcomeMessage = findViewById(R.id.welcome_message);
        inputText = findViewById(R.id.input_text);
        micRing = findViewById(R.id.mic_ring);
        settingsButton = findViewById(R.id.settings_button);
        // statusText will be part of the welcome_message for now

        micRing.setOnClickListener(v -> {
            if (isListeningForCommand) {
                speechEngine.stopListening();
                startWakeWordDetection();
            } else {
                startCommandListening();
            }
        });
        settingsButton.setOnClickListener(v -> openSettings());

        loadSettingsAndStart();

        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    loadSettingsAndStart();
                    jarvisBrain = new JarvisBrain(this, this); // Re-initialize to load new commands
                }
        );
    }

    private void loadSettingsAndStart() {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(Constants.KEY_USERNAME, "User");
        String aiName = prefs.getString(Constants.KEY_AI_NAME, "Jarvis");
        String wakeWord = prefs.getString(Constants.KEY_WAKE_WORD, "hey jarvis");
        String welcomeFormat = prefs.getString(Constants.KEY_WELCOME_RESPONSE, "Hi {username}, I’m {ai_name}. Ready when you are.");

        String finalWelcome = welcomeFormat
                .replace("{username}", username)
                .replace("{ai_name}", aiName);
        welcomeMessage.setText(finalWelcome);

        if (wakeWordEngine != null) {
            wakeWordEngine.destroy();
        }
        wakeWordEngine = new WakeWordEngine(this, wakeWord, this);
        startWakeWordDetection();
    }

    private void startWakeWordDetection() {
        isListeningForCommand = false;
        runOnUiThread(() -> welcomeMessage.setText("Listening for wake word..."));
        wakeWordEngine.startListening();
    }

    private void startCommandListening() {
        isListeningForCommand = true;
        wakeWordEngine.stopListening();
        runOnUiThread(() -> welcomeMessage.setText("Listening for command..."));
        speechEngine.startListening();
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        settingsLauncher.launch(intent);
    }

    @Override
    public void onWakeWordDetected() {
        startCommandListening();
    }

    @Override
    public void onResponse(String response) {
        runOnUiThread(() -> {
            inputText.setText(response);
            speechEngine.speak(response, Locale.US);
            startWakeWordDetection(); // Go back to wake word listening after response
        });
    }

    @Override
    public void onCustomResponse(String response) {
        onResponse(response);
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> Toast.makeText(HomeActivity.this, error, Toast.LENGTH_LONG).show());
        startWakeWordDetection(); // Go back to wake word listening on error
    }

    @Override
    public void onSpeechResult(String result) {
        inputText.setText(result);
        jarvisBrain.processInput(result);
    }

    @Override
    public void onSpeechError(String error) {
        runOnUiThread(() -> Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show());
        startWakeWordDetection();
    }

    @Override
    public void onTtsInitialized() {}

    @Override
    public void onTtsSpoken() {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.REQUEST_CODE_PERMISSIONS) {
            if (!PermissionUtil.allPermissionsGranted(this)) {
                Toast.makeText(this, "Permissions not granted. Some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechEngine != null) speechEngine.destroy();
        if (wakeWordEngine != null) wakeWordEngine.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startWakeWordDetection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeWordEngine != null) wakeWordEngine.stopListening();
        if (speechEngine != null) speechEngine.stopListening();
    }
}
