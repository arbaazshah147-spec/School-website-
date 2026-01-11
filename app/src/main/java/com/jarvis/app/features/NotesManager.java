package com.jarvis.app.features;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotesManager {

    private final Map<String, String> notes;

    public NotesManager() {
        this.notes = new HashMap<>();
    }

    public void createNote(String voiceInput) {
        String title = parseTitle(voiceInput);
        String content = parseContent(voiceInput);

        if (title != null && content != null) {
            notes.put(title, content);
        }
    }

    private String parseTitle(String input) {
        Pattern pattern = Pattern.compile("title (.*?)(,|numbers|content)");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Note"; // Default title
    }

    private String parseContent(String input) {
        Pattern pattern = Pattern.compile("(numbers|content) (.*)");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return input; // Use full input as content if no keyword found
    }

    public Map<String, String> getNotes() {
        return notes;
    }
}
