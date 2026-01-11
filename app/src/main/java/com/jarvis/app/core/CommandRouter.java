package com.jarvis.app.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvis.app.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CommandRouter {

    private final Context context;
    private final MemoryStore memoryStore;
    private Map<String, CustomCommand> customCommands;

    public CommandRouter(Context context) {
        this.context = context;
        this.memoryStore = new MemoryStore(context);
        loadCustomCommands();
    }

    private void loadCustomCommands() {
        String commandsJson = memoryStore.getData(Constants.KEY_CUSTOM_COMMANDS, "{}");
        Type type = new TypeToken<HashMap<String, CustomCommand>>(){}.getType();
        customCommands = new Gson().fromJson(commandsJson, type);
        if (customCommands == null) {
            customCommands = new HashMap<>();
        }
    }

    public void executeCommand(String command) {
        String lowerCaseCommand = command.toLowerCase();

        // Check for custom commands first
        for (CustomCommand customCommand : customCommands.values()) {
            if (lowerCaseCommand.contains(customCommand.getTriggerPhrase().toLowerCase())) {
                performAction(customCommand.getActionType(), customCommand.getActionValue());
                return;
            }
        }

        // Built-in commands
        if (lowerCaseCommand.contains("open")) {
            String appName = lowerCaseCommand.replace("open", "").trim();
            openApp(appName);
        } else if (lowerCaseCommand.contains("go to settings")) {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
        // ... more built-in commands
    }

    private void performAction(String actionType, String actionValue) {
        switch (actionType.toUpperCase()) {
            case "OPEN_APP":
                openApp(actionValue);
                break;
            case "OPEN_URL":
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionValue));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            // ... more action types
        }
    }

    private void openApp(String appName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
             // Try to find app by name if package name is not provided
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // ... (code to query package manager for app list)
        }
    }

    public void addCustomCommand(String name, String trigger, String actionType, String actionValue) {
        customCommands.put(name, new CustomCommand(name, trigger, actionType, actionValue));
        String commandsJson = new Gson().toJson(customCommands);
        memoryStore.saveData(Constants.KEY_CUSTOM_COMMANDS, commandsJson);
    }

    public static class CustomCommand {
        private String name;
        private String triggerPhrase;
        private String actionType;
        private String actionValue;

        public CustomCommand(String name, String triggerPhrase, String actionType, String actionValue) {
            this.name = name;
            this.triggerPhrase = triggerPhrase;
            this.actionType = actionType;
            this.actionValue = actionValue;
        }

        public String getName() { return name; }
        public String getTriggerPhrase() { return triggerPhrase; }
        public String getActionType() { return actionType; }
        public String getActionValue() { return actionValue; }
    }
}
