package com.jarvis.app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.jarvis.app.R;
import com.jarvis.app.core.CommandRouter;
import com.jarvis.app.core.MemoryStore;
import com.jarvis.app.utils.Constants;

public class SettingsActivity extends AppCompatActivity {

    private MemoryStore memoryStore;
    private CommandRouter commandRouter;

    private EditText aiName, username, wakeWord, welcomeResponse;
    private EditText geminiKey, chatgptKey, grokKey;
    private EditText personalInfo;
    private EditText commandName, commandTrigger, commandActionType, commandActionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        memoryStore = new MemoryStore(this);
        commandRouter = new CommandRouter(this);

        // Bind all EditTexts
        aiName = findViewById(R.id.edit_ai_name);
        username = findViewById(R.id.edit_username);
        wakeWord = findViewById(R.id.edit_wake_word);
        welcomeResponse = findViewById(R.id.edit_welcome_response);
        geminiKey = findViewById(R.id.edit_gemini_api_key);
        chatgptKey = findViewById(R.id.edit_chatgpt_api_key);
        grokKey = findViewById(R.id.edit_grok_api_key);
        personalInfo = findViewById(R.id.edit_personal_info);
        commandName = findViewById(R.id.edit_command_name);
        commandTrigger = findViewById(R.id.edit_command_trigger);
        commandActionType = findViewById(R.id.edit_command_action_type);
        commandActionValue = findViewById(R.id.edit_command_action_value);

        Button saveButton = findViewById(R.id.save_settings_button);
        Button addCommandButton = findViewById(R.id.add_command_button);

        loadSettings();

        saveButton.setOnClickListener(v -> saveSettings());
        addCommandButton.setOnClickListener(v -> addCustomCommand());
    }

    private void loadSettings() {
        aiName.setText(memoryStore.getData(Constants.KEY_AI_NAME, "Jarvis"));
        username.setText(memoryStore.getData(Constants.KEY_USERNAME, ""));
        wakeWord.setText(memoryStore.getData(Constants.KEY_WAKE_WORD, "Hey Jarvis"));
        welcomeResponse.setText(memoryStore.getData(Constants.KEY_WELCOME_RESPONSE, "Hello, how can I help?"));
        geminiKey.setText(memoryStore.getData(Constants.KEY_GEMINI_API_KEY, ""));
        chatgptKey.setText(memoryStore.getData(Constants.KEY_CHATGPT_API_KEY, ""));
        grokKey.setText(memoryStore.getData(Constants.KEY_GROK_API_KEY, ""));
        personalInfo.setText(memoryStore.getData(Constants.KEY_PERSONAL_INFO, ""));
    }

    private void saveSettings() {
        memoryStore.saveData(Constants.KEY_AI_NAME, aiName.getText().toString());
        memoryStore.saveData(Constants.KEY_USERNAME, username.getText().toString());
        memoryStore.saveData(Constants.KEY_WAKE_WORD, wakeWord.getText().toString());
        memoryStore.saveData(Constants.KEY_WELCOME_RESPONSE, welcomeResponse.getText().toString());
        memoryStore.saveData(Constants.KEY_GEMINI_API_KEY, geminiKey.getText().toString());
        memoryStore.saveData(Constants.KEY_CHATGPT_API_KEY, chatgptKey.getText().toString());
        memoryStore.saveData(Constants.KEY_GROK_API_KEY, grokKey.getText().toString());
        memoryStore.saveData(Constants.KEY_PERSONAL_INFO, personalInfo.getText().toString());

        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    private void addCustomCommand() {
        String name = commandName.getText().toString();
        String trigger = commandTrigger.getText().toString();
        String type = commandActionType.getText().toString();
        String value = commandActionValue.getText().toString();

        if (!name.isEmpty() && !trigger.isEmpty() && !type.isEmpty() && !value.isEmpty()) {
            commandRouter.addCustomCommand(name, trigger, type, value);
            Toast.makeText(this, "Custom Command Added", Toast.LENGTH_SHORT).show();
            // Clear fields after adding
            commandName.setText("");
            commandTrigger.setText("");
            commandActionType.setText("");
            commandActionValue.setText("");
        } else {
            Toast.makeText(this, "All command fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}
