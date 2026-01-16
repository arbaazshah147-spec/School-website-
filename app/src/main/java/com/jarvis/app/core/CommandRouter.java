package com.jarvis.app.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.jarvis.app.ai.WorkflowEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvis.app.ai.WorkflowEngine;
import com.jarvis.app.features.AppOpener;
import com.jarvis.app.features.MediaController;
import com.jarvis.app.features.MessageHandler;
import com.jarvis.app.model.CustomCommand;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRouter {

    private static final String TAG = "CommandRouter";
    private final WorkflowEngine workflowEngine;
    private final Context context;
    private final MemoryStore memoryStore;
    private final Gson gson;
    private Map<String, CustomCommand> customCommandMap = new HashMap<>();

    public interface CommandCallback {
        void onCommandProcessed(String response);
        void onCommandError(String error);
    }

    public CommandRouter(Context context) {
        this.context = context;
        this.workflowEngine = new WorkflowEngine(context);
        this.memoryStore = new MemoryStore(context);
        this.gson = new Gson();
        loadCustomCommands();
    }

    public void loadCustomCommands() {
        String json = memoryStore.get("custom_commands", "[]");
        Type type = new TypeToken<ArrayList<CustomCommand>>() {}.getType();
        List<CustomCommand> commands = gson.fromJson(json, type);
        if (commands != null) {
            for (CustomCommand command : commands) {
                customCommandMap.put(command.getTrigger().toLowerCase(), command);
            }
        }
    }

    public void processCommand(String command, CommandCallback callback) {
        String lowerCaseCommand = command.toLowerCase().trim();
        Log.d(TAG, "Processing command: " + command);

        if (handleCustomCommands(lowerCaseCommand, callback) || handleDirectCommands(lowerCaseCommand, command, callback)) {
            return;
        }

        // If no command is matched, route to the AI workflow
        Log.d(TAG, "No direct or custom command matched. Routing to AI workflow.");
        workflowEngine.process(command, new WorkflowEngine.WorkflowCallback() {
            @Override
            public void onWorkflowComplete(String result) {
                Log.d(TAG, "AI workflow completed. Result: " + result);
                callback.onCommandProcessed(result);
            }

            @Override
            public void onWorkflowError(String error) {
                Log.e(TAG, "AI workflow error: " + error);
                callback.onCommandError(error);
            }
        });
    }

    private boolean handleDirectCommands(String lowerCaseCommand, String originalCommand, CommandCallback callback) {
        if (lowerCaseCommand.startsWith("open ")) {
            String appName = originalCommand.substring(5).trim();
            if (AppOpener.openApp(context, appName)) {
                callback.onCommandProcessed("Opening " + appName);
            } else {
                callback.onCommandError("Could not open " + appName);
            }
            return true;
        } else if (lowerCaseCommand.startsWith("call ")) {
            String contact = originalCommand.substring(5).trim();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact));
            try {
                context.startActivity(callIntent);
                callback.onCommandProcessed("Calling " + contact);
            } catch (SecurityException e) {
                callback.onCommandError("Call permission not granted.");
            }
            return true;
        } else if (lowerCaseCommand.startsWith("send text to ")) {
            try {
                String[] parts = originalCommand.split(" message ");
                String phoneNumber = parts[0].substring(14).trim();
                String message = parts[1].trim();
                if (MessageHandler.sendSms(context, phoneNumber, message)) {
                    callback.onCommandProcessed("Sending message to " + phoneNumber);
                } else {
                    callback.onCommandError("Could not send message.");
                }
            } catch (Exception e) {
                callback.onCommandError("Invalid message format. Use 'send text to [number] message [message]'.");
            }
            return true;
        } else if (lowerCaseCommand.startsWith("play ")) {
            String query = originalCommand.substring(5).trim();
            MediaController.searchAndPlayYouTube(context, query);
            callback.onCommandProcessed("Playing " + query + " on YouTube");
            return true;
        } else if (lowerCaseCommand.startsWith("send whatsapp to ")) {
            try {
                String[] parts = originalCommand.split(" message ");
                String phoneNumber = parts[0].substring(17).trim();
                String message = parts[1].trim();
                MessageHandler.sendWhatsAppMessage(context, phoneNumber, message);
                callback.onCommandProcessed("Opening WhatsApp to message " + phoneNumber);
            } catch (Exception e) {
                callback.onCommandError("Invalid WhatsApp message format. Use 'send whatsapp to [number] message [message]'.");
            }
            return true;
        } else if (lowerCaseCommand.startsWith("search for ")) {
            String query = originalCommand.substring(11).trim();
            Intent searchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + query));
            context.startActivity(searchIntent);
            callback.onCommandProcessed("Searching for " + query);
            return true;
        }
        return false;
    }

    private boolean handleCustomCommands(String lowerCaseCommand, CommandCallback callback) {
        for (String trigger : customCommandMap.keySet()) {
            if (lowerCaseCommand.startsWith(trigger)) {
                CustomCommand command = customCommandMap.get(trigger);
                String actionType = command.getActionType();
                String actionValue = command.getActionValue();
                String argument = "";

                if (command.hasArgument()) {
                    argument = lowerCaseCommand.substring(trigger.length()).trim();
                }

                switch (actionType) {
                    case "Open App":
                        if (AppOpener.openApp(context, actionValue)) {
                            callback.onCommandProcessed("Opening " + actionValue);
                        } else {
                            callback.onCommandError("Could not open " + actionValue);
                        }
                        break;
                    case "Send WhatsApp Message":
                        // Assumes the argument is "phoneNumber message message"
                        try {
                            String[] parts = argument.split(" message ");
                            String phoneNumber = parts[0].trim();
                            String message = parts[1].trim();
                            MessageHandler.sendWhatsAppMessage(context, phoneNumber, message);
                            callback.onCommandProcessed("Opening WhatsApp to message " + phoneNumber);
                        } catch (Exception e) {
                            callback.onCommandError("Invalid WhatsApp message format. Use '[trigger] [number] message [message]'.");
                        }
                        break;
                    case "Send SMS":
                        // Assumes the argument is "phoneNumber message message"
                        try {
                            String[] parts = argument.split(" message ");
                            String phoneNumber = parts[0].trim();
                            String message = parts[1].trim();
                            if (MessageHandler.sendSms(context, phoneNumber, message)) {
                                callback.onCommandProcessed("Sending message to " + phoneNumber);
                            } else {
                                callback.onCommandError("Could not send message.");
                            }
                        } catch (Exception e) {
                            callback.onCommandError("Invalid SMS format. Use '[trigger] [number] message [message]'.");
                        }
                        break;
                    case "Play YouTube Video":
                        MediaController.searchAndPlayYouTube(context, argument);
                        callback.onCommandProcessed("Playing " + argument + " on YouTube");
                        break;
                    case "Web Search":
                        Intent searchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + argument));
                        context.startActivity(searchIntent);
                        callback.onCommandProcessed("Searching for " + argument);
                        break;
                }
                return true;
            }
        }
        return false;
    }
}
