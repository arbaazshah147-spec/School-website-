package com.jarvis.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jarvis.app.features.AppOpener;
import com.jarvis.app.features.MediaController;
import com.jarvis.app.features.MessageHandler;
import com.jarvis.app.features.NotesManager;
import com.jarvis.app.model.CustomCommand;
import com.jarvis.app.utils.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRouter {

    private final Context context;
    private final Map<String, Command> commandMap;

    public interface Command {
        void execute(String... args);
    }

    public CommandRouter(Context context) {
        this.context = context;
        this.commandMap = new HashMap<>();
        registerDefaultCommands();
        loadCustomCommands();
    }

    private void registerDefaultCommands() {
        // App opening
        commandMap.put("open", new AppOpener(context)::openApp);

        // Messaging
        commandMap.put("send", new MessageHandler(context)::sendMessage);

        // Media control
        MediaController mediaController = new MediaController(context);
        commandMap.put("play", mediaController::play);
        commandMap.put("pause", mediaController::pause);
        commandMap.put("next", mediaController::next);
        commandMap.put("previous", mediaController::previous);

        // Notes
        commandMap.put("make", new NotesManager(context)::createNote);
    }

    public void loadCustomCommands() {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonCommands = prefs.getString("custom_commands", "[]");
        Type type = new TypeToken<ArrayList<CustomCommand>>() {}.getType();
        List<CustomCommand> commands = gson.fromJson(jsonCommands, type);

        // Sort commands by trigger length descending to match longest trigger first
        commands.sort(Comparator.comparingInt(c -> c.getTrigger().length()).reversed());

        for (CustomCommand cmd : commands) {
            registerCustomCommand(cmd.getTrigger(), cmd.getActionType(), cmd.getActionValue());
        }
    }

    public void registerCustomCommand(String trigger, String actionType, String actionValue) {
        Command command = (args) -> {
            switch (actionType) {
                case "Open App":
                    new AppOpener(context).openApp(actionValue);
                    break;
                case "Send Message":
                    new MessageHandler(context).sendMessage(actionValue, String.join(" ", args));
                    break;
                case "Custom Text Response":
                    if(context instanceof CommandRouterListener){
                        ((CommandRouterListener) context).onCustomResponse(actionValue);
                    }
                    break;
                case "Play / Pause / Next / Previous":
                    MediaController mediaController = new MediaController(context);
                    String action = actionValue.toLowerCase();
                    if (action.contains("play")) mediaController.play();
                    if (action.contains("pause")) mediaController.pause();
                    if (action.contains("next")) mediaController.next();
                    if (action.contains("previous")) mediaController.previous();
                    break;
                default:
                     Toast.makeText(context, "Unknown action type for custom command.", Toast.LENGTH_SHORT).show();
            }
        };
        commandMap.put(trigger.toLowerCase(), command);
    }

    public void executeCommand(String rawCommand) {
        String lowerCaseCommand = rawCommand.toLowerCase().trim();
        String bestMatchTrigger = null;

        for (String trigger : commandMap.keySet()) {
            if (lowerCaseCommand.startsWith(trigger)) {
                if (bestMatchTrigger == null || trigger.length() > bestMatchTrigger.length()) {
                    bestMatchTrigger = trigger;
                }
            }
        }

        if (bestMatchTrigger != null) {
            String argsString = lowerCaseCommand.substring(bestMatchTrigger.length()).trim();
            String[] args = argsString.isEmpty() ? new String[0] : argsString.split("\\s+");
            commandMap.get(bestMatchTrigger).execute(args);
        } else {
             Toast.makeText(context, "Command not recognized.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCommand(String triggerWord) {
        // To support multi-word triggers, this check needs to be more robust.
        // The check in JarvisBrain should be sufficient for now.
        return commandMap.keySet().stream().anyMatch(trigger -> trigger.startsWith(triggerWord.toLowerCase()));
    }

    public interface CommandRouterListener {
        void onCustomResponse(String response);
    }
}
