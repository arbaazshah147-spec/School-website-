package com.jarvis.app.model;

public class CustomCommand {
    private String name;
    private String trigger;
    private String actionType;
    private String actionValue;

    public CustomCommand(String name, String trigger, String actionType, String actionValue) {
        this.name = name;
        this.trigger = trigger;
        this.actionType = actionType;
        this.actionValue = actionValue;
    }

    public String getName() {
        return name;
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
}
