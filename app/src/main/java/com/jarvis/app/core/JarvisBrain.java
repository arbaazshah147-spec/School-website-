package com.jarvis.app.core;

import android.content.Context;
import com.jarvis.app.ai.WorkflowEngine;

public class JarvisBrain {

    private final Context context;
    private final WorkflowEngine workflowEngine;
    private final CommandRouter commandRouter;
    private final MemoryStore memoryStore;
    private final BrainCallback callback;

    public interface BrainCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public JarvisBrain(Context context, BrainCallback callback) {
        this.context = context;
        this.callback = callback;
        this.workflowEngine = new WorkflowEngine(context);
        this.commandRouter = new CommandRouter(context);
        this.memoryStore = new MemoryStore(context);
    }

    public void processInput(String input) {
        memoryStore.addToHistory("User: " + input);

        String[] commandCheck = input.toLowerCase().trim().split("\\s+");
        if (commandRouter.isCommand(commandCheck[0])) {
            commandRouter.executeCommand(input);
            callback.onResponse("Executing command: " + input);
        } else {
            workflowEngine.processQuery(input, new WorkflowEngine.WorkflowCallback() {
                @Override
                public void onComplete(String result) {
                    memoryStore.addToHistory("Jarvis: " + result);
                    callback.onResponse(result);
                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                }
            });
        }
    }
}
