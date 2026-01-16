package com.jarvis.app.model;

public class CustomCommand {
    private String trigger;
    private String actionType;
    private String actionValue;
    private String argumentPlaceholder;

    public CustomCommand(String trigger, String actionType, String actionValue) {
        this.trigger = trigger;
        this.actionType = actionType;
        if (actionValue.contains("{arg}")) {
            this.actionValue = actionValue.replace("{arg}", "").trim();
            this.argumentPlaceholder = "{arg}";
        } else {
            this.actionValue = actionValue;
        }
    }

    public String getTrigger() {
        return trigger;
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionValue() {
        return actionValue;
    }

    public boolean hasArgument() {
        return argumentPlaceholder != null;
    }
}
