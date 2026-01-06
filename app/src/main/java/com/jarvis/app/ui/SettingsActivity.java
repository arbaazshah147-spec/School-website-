package com.jarvis.app.ui;

import android.content.SharedPreferences;
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
import com.jarvis.app.model.CustomCommand;
import com.jarvis.app.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private EditText usernameEditText, aiNameEditText, wakeWordEditText, welcomeResponseEditText;
    private EditText geminiApiKeyEditText, chatgptApiKeyEditText, grokApiKeyEditText;
    private EditText commandNameEditText, commandTriggerEditText, commandActionValueEditText;
    private Spinner actionTypeSpinner;
    private Button saveCommandButton, saveSettingsButton;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);

        // User Identity
        usernameEditText = findViewById(R.id.username_edit_text);
        aiNameEditText = findViewById(R.id.ai_name_edit_text);
        wakeWordEditText = findViewById(R.id.wake_word_edit_text);
        welcomeResponseEditText = findViewById(R.id.welcome_response_edit_text);

        // API Management
        geminiApiKeyEditText = findViewById(R.id.gemini_api_key_edit_text);
        chatgptApiKeyEditText = findViewById(R.id.chatgpt_api_key_edit_text);
        grokApiKeyEditText = findViewById(R.id.grok_api_key_edit_text);

        // Custom Command Builder
        commandNameEditText = findViewById(R.id.command_name_edit_text);
        commandTriggerEditText = findViewById(R.id.command_trigger_edit_text);
        commandActionValueEditText = findViewById(R.id.command_action_value_edit_text);
        actionTypeSpinner = findViewById(R.id.action_type_spinner);
        saveCommandButton = findViewById(R.id.save_command_button);

        saveSettingsButton = findViewById(R.id.save_settings_button);

        setupSpinner();
        loadSettings();

        saveSettingsButton.setOnClickListener(v -> saveSettings());
        saveCommandButton.setOnClickListener(v -> saveCustomCommand());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.action_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionTypeSpinner.setAdapter(adapter);
    }

    private void loadSettings() {
        usernameEditText.setText(prefs.getString(Constants.KEY_USERNAME, ""));
        aiNameEditText.setText(prefs.getString(Constants.KEY_AI_NAME, "Jarvis"));
        wakeWordEditText.setText(prefs.getString(Constants.KEY_WAKE_WORD, "Hey Jarvis"));
        welcomeResponseEditText.setText(prefs.getString(Constants.KEY_WELCOME_RESPONSE, "Hi {username}, I’m {ai_name}. Ready when you are."));

        geminiApiKeyEditText.setText(prefs.getString(Constants.KEY_GEMINI_API_KEY, ""));
        chatgptApiKeyEditText.setText(prefs.getString(Constants.KEY_CHATGPT_API_KEY, ""));
        grokApiKeyEditText.setText(prefs.getString(Constants.KEY_GROK_API_KEY, ""));
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.KEY_USERNAME, usernameEditText.getText().toString());
        editor.putString(Constants.KEY_AI_NAME, aiNameEditText.getText().toString());
        editor.putString(Constants.KEY_WAKE_WORD, wakeWordEditText.getText().toString());
        editor.putString(Constants.KEY_WELCOME_RESPONSE, welcomeResponseEditText.getText().toString());

        editor.putString(Constants.KEY_GEMINI_API_KEY, geminiApiKeyEditText.getText().toString());
        editor.putString(Constants.KEY_CHATGPT_API_KEY, chatgptApiKeyEditText.getText().toString());
        editor.putString(Constants.KEY_GROK_API_KEY, grokApiKeyEditText.getText().toString());

        editor.apply();

        Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
    }

    private void saveCustomCommand() {
        String name = commandNameEditText.getText().toString().trim();
        String trigger = commandTriggerEditText.getText().toString().trim().toLowerCase();
        String actionType = actionTypeSpinner.getSelectedItem().toString();
        String actionValue = commandActionValueEditText.getText().toString().trim();

        if (name.isEmpty() || trigger.isEmpty() || actionValue.isEmpty()) {
            Toast.makeText(this, "Command name, trigger, and action value cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        String jsonCommands = prefs.getString("custom_commands", "[]");
        Type type = new TypeToken<ArrayList<CustomCommand>>() {}.getType();
        List<CustomCommand> commands = gson.fromJson(jsonCommands, type);

        commands.add(new CustomCommand(name, trigger, actionType, actionValue));

        String newJsonCommands = gson.toJson(commands);
        prefs.edit().putString("custom_commands", newJsonCommands).apply();

        Toast.makeText(this, "Custom command '" + name + "' saved.", Toast.LENGTH_SHORT).show();

        // Clear fields for next command
        commandNameEditText.setText("");
        commandTriggerEditText.setText("");
        commandActionValueEditText.setText("");
    }
}
