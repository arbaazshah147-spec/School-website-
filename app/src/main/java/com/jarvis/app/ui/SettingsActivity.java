package com.jarvis.app.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvis.app.R;
import com.jarvis.app.ai.ChatGPTClient;
import com.jarvis.app.ai.GeminiClient;
import com.jarvis.app.ai.GrokClient;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.model.CustomCommand;
import com.jarvis.app.utils.ApiClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private EditText aiNameField, userNameField, wakeWordField, welcomeMessageField;
    private EditText geminiApiKeyField, chatgptApiKeyField, grokApiKeyField;
    private EditText commandTriggerField, commandActionValueField;
    private Button saveButton, testGeminiButton, testChatgptButton, testGrokButton, saveCommandButton;
    private Spinner actionTypeSpinner;

    private MemoryStore memoryStore;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        memoryStore = new MemoryStore(this);
        gson = new Gson();

        // User Identity
        aiNameField = findViewById(R.id.ai_name_field);
        userNameField = findViewById(R.id.user_name_field);
        wakeWordField = findViewById(R.id.wake_word_field);
        welcomeMessageField = findViewById(R.id.welcome_message_field);

        // API Management
        geminiApiKeyField = findViewById(R.id.gemini_api_key_field);
        chatgptApiKeyField = findViewById(R.id.chatgpt_api_key_field);
        grokApiKeyField = findViewById(R.id.grok_api_key_field);
        testGeminiButton = findViewById(R.id.test_gemini_button);
        testChatgptButton = findViewById(R.id.test_chatgpt_button);
        testGrokButton = findViewById(R.id.test_grok_button);

        // Custom Commands
        commandTriggerField = findViewById(R.id.command_trigger_edit_text);
        commandActionValueField = findViewById(R.id.command_action_value_edit_text);
        actionTypeSpinner = findViewById(R.id.action_type_spinner);
        saveCommandButton = findViewById(R.id.save_command_button);

        saveButton = findViewById(R.id.save_button);

        setupSpinner();
        loadSettings();
        setupListeners();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.action_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionTypeSpinner.setAdapter(adapter);
    }

    private void loadSettings() {
        aiNameField.setText(memoryStore.get("ai_name", "Jarvis"));
        userNameField.setText(memoryStore.get("user_name", "User"));
        wakeWordField.setText(memoryStore.get("wake_word", "Hey Jarvis"));
        welcomeMessageField.setText(memoryStore.get("welcome_message", "How can I help?"));
        geminiApiKeyField.setText(memoryStore.get("gemini_api_key", ""));
        chatgptApiKeyField.setText(memoryStore.get("chatgpt_api_key", ""));
        grokApiKeyField.setText(memoryStore.get("grok_api_key", ""));
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveSettings());

        testGeminiButton.setOnClickListener(v -> testApiKey(new GeminiClient(this), "Hello", "gemini_api_key"));
        testChatgptButton.setOnClickListener(v -> testApiKey(new ChatGPTClient(this), "Hello", "chatgpt_api_key"));
        testGrokButton.setOnClickListener(v -> testApiKey(new GrokClient(this), "Hello", "grok_api_key"));

        saveCommandButton.setOnClickListener(v -> saveCustomCommand());
    }

    private void testApiKey(Object client, String testQuery, String keyName) {
        String apiKey = memoryStore.get(keyName, "");
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "API Key is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.ApiCallback callback = new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "API Key is valid!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "API Key is invalid: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        };

        if (client instanceof GeminiClient) {
            ((GeminiClient) client).getResponse(testQuery, callback);
        } else if (client instanceof ChatGPTClient) {
            ((ChatGPTClient) client).getResponse(testQuery, callback);
        } else if (client instanceof GrokClient) {
            ((GrokClient) client).getResponse(testQuery, callback);
        }
    }


    private void saveSettings() {
        memoryStore.save("ai_name", aiNameField.getText().toString());
        memoryStore.save("user_name", userNameField.getText().toString());
        memoryStore.save("wake_word", wakeWordField.getText().toString());
        memoryStore.save("welcome_message", welcomeMessageField.getText().toString());
        memoryStore.save("gemini_api_key", geminiApiKeyField.getText().toString());
        memoryStore.save("chatgpt_api_key", chatgptApiKeyField.getText().toString());
        memoryStore.save("grok_api_key", grokApiKeyField.getText().toString());
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    private void saveCustomCommand() {
        String trigger = commandTriggerField.getText().toString().trim().toLowerCase();
        String actionType = actionTypeSpinner.getSelectedItem().toString();
        String actionValue = commandActionValueField.getText().toString().trim();

        if (trigger.isEmpty() || actionValue.isEmpty()) {
            Toast.makeText(this, "Trigger and Action Value cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        String json = memoryStore.get("custom_commands", "[]");
        Type type = new TypeToken<ArrayList<CustomCommand>>() {}.getType();
        List<CustomCommand> commands = gson.fromJson(json, type);
        if (commands == null) {
            commands = new ArrayList<>();
        }

        commands.add(new CustomCommand(trigger, actionType, actionValue));
        memoryStore.saveObject("custom_commands", commands);

        Toast.makeText(this, "Custom command saved!", Toast.LENGTH_SHORT).show();
        commandTriggerField.setText("");
        commandActionValueField.setText("");
    }
}
