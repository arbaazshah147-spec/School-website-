package com.jarvis.app.features;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotesManager {

    private static final String NOTES_FILE = "notes.json";
    private final Context context;
    private Map<String, String> notes;

    public NotesManager(Context context) {
        this.context = context;
        loadNotes();
    }

    private void loadNotes() {
        try {
            File file = new File(context.getFilesDir(), NOTES_FILE);
            if (!file.exists()) {
                this.notes = new HashMap<>();
                return;
            }
            FileReader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            this.notes = new Gson().fromJson(reader, type);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            this.notes = new HashMap<>(); // In case of error, start fresh
        }
    }

    private void saveNotes() {
        try {
            File file = new File(context.getFilesDir(), NOTES_FILE);
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(this.notes, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNote(String voiceInput) {
        String title = parseTitle(voiceInput);
        String content = parseContent(voiceInput);

        if (title != null && content != null) {
            notes.put(title, content);
            saveNotes();
        }
    }

    public void deleteNote(String title) {
        if (notes.containsKey(title)) {
            notes.remove(title);
            saveNotes();
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
