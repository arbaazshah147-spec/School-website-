package com.jarvis.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.app.R;
import com.jarvis.app.core.JarvisBrain;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.PermissionUtil;

public class HomeActivity extends AppCompatActivity implements JarvisBrain.JarvisCallback {

    private TextView conversationView;
    private EditText inputField;
    private ImageButton micButton;
    private Button settingsButton;

    private JarvisBrain jarvisBrain;
    private MemoryStore memoryStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        conversationView = findViewById(R.id.conversation_view);
        inputField = findViewById(R.id.input_field);
        micButton = findViewById(R.id.mic_button);
        settingsButton = findViewById(R.id.settings_button);

        jarvisBrain = new JarvisBrain(this, this);
        memoryStore = new MemoryStore(this);

        micButton.setOnClickListener(v -> {
            String inputText = inputField.getText().toString();
            if (!inputText.isEmpty()) {
                conversationView.append("\nUser: " + inputText);
                jarvisBrain.processText(inputText);
                inputField.setText("");
            } else {
                jarvisBrain.startListening();
            }
        });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        requestPermissions();
    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
        if (!PermissionUtil.checkAndRequestPermissions(this, permissions)) {
            // Permissions are being requested, handle the result in onRequestPermissionsResult
        } else {
            // Permissions are already granted, proceed with app initialization
            initializeApp();
        }
    }

    private void initializeApp() {
        micButton.setEnabled(true);
        String welcomeMessage = memoryStore.get("welcome_message", "How can I help?");
        jarvisBrain.speak(welcomeMessage);
        jarvisBrain.startListening(); // Start listening for wake word
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.REQUEST_CODE_PERMISSIONS) {
            boolean audioGranted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (Manifest.permission.RECORD_AUDIO.equals(permissions[i]) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    audioGranted = true;
                    break;
                }
            }

            if (audioGranted) {
                initializeApp();
            } else {
                String message = "Microphone permission is required for voice commands. Please grant the permission in settings.";
                jarvisBrain.speak(message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                micButton.setEnabled(false);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Re-initialize JarvisBrain to load new custom commands and settings
        jarvisBrain = new JarvisBrain(this, this);
        // Reload settings in case they changed
        String welcomeMessage = memoryStore.get("welcome_message", "How can I help?");
    }

    @Override
    public void onResponse(String response) {
        runOnUiThread(() -> {
            conversationView.append("\nJarvis: " + response);
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            conversationView.append("\nError: " + error);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jarvisBrain.shutdown();
    }
}
