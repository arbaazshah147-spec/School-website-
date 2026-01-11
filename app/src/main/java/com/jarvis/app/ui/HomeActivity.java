package com.jarvis.app.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.app.R;
import com.jarvis.app.core.JarvisBrain;
import com.jarvis.app.utils.Constants;
import com.jarvis.app.utils.PermissionUtil;

public class HomeActivity extends AppCompatActivity implements JarvisBrain.BrainCallback {

    private JarvisBrain jarvisBrain;
    private TextView welcomeMessage;
    private ImageView micButton;

    private final String[] permissions = {
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeMessage = findViewById(R.id.welcome_message);
        micButton = findViewById(R.id.mic_button);
        Button dashboardButton = findViewById(R.id.dashboard_button);

        jarvisBrain = new JarvisBrain(this, this);

        micButton.setOnClickListener(v -> {
            if (PermissionUtil.checkPermissions(this, permissions)) {
                jarvisBrain.startListening();
            } else {
                PermissionUtil.requestPermissions(this, permissions, Constants.REQUEST_CODE_PERMISSIONS);
            }
        });

        dashboardButton.setOnClickListener(v ->
            startActivity(new Intent(HomeActivity.this, DashboardActivity.class)));

        // Personalize welcome message
        // In a real app, this would be loaded from MemoryStore
        welcomeMessage.setText("Welcome, User!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (PermissionUtil.checkPermissions(this, this.permissions)) {
                jarvisBrain.startListening();
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jarvisBrain.shutdown();
    }

    // BrainCallback Methods
    @Override
    public void onResponse(String response) {
        // For now, just show a toast. In a real app, this would update the UI.
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSpeechInput(String text) {
        // This is the final recognized text. Update UI if needed.
    }

    @Override
    public void onPartialSpeech(String partialText) {
        // Update UI with partial text to show recognition is in progress
        welcomeMessage.setText(partialText);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListening(boolean isListening) {
        // Animate mic button based on listening state
        if (isListening) {
            micButton.setAlpha(0.5f); // Simple animation
        } else {
            micButton.setAlpha(1.0f);
        }
    }
}
