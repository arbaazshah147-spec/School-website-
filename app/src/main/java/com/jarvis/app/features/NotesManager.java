package com.jarvis.app.features;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class NotesManager {
    private Context context;

    public NotesManager(Context context) {
        this.context = context;
    }

    public void createNote(String... args) {
        // The command is expected to be in the format: "make note title [title words] [content words]"
        // The word "title" is the delimiter between the command and the note's title.
        // The content is assumed to start with the first number found after the title.

        String fullCommand = String.join(" ", args);
        String title;
        String content;

        // A more robust parsing logic.
        // We look for " title " and split based on that.
        int titleIndex = fullCommand.toLowerCase().indexOf("title ");
        if(titleIndex == -1 || args.length < 2) {
            Toast.makeText(context, "Invalid note format. Use 'make note title [your title] [your content]'", Toast.LENGTH_LONG).show();
            return;
        }

        String titleAndContent = fullCommand.substring(titleIndex + "title ".length());
        String[] parts = titleAndContent.split("\\s+");

        int contentStartIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches("\\d+.*")) { // Find the first word that starts with a number
                contentStartIndex = i;
                break;
            }
        }

        if (contentStartIndex != -1 && contentStartIndex > 0) {
            title = Arrays.stream(parts, 0, contentStartIndex).collect(Collectors.joining(" "));
            content = Arrays.stream(parts, contentStartIndex, parts.length).collect(Collectors.joining(" "));
        } else if (contentStartIndex == 0) {
            // This case means a number was found right at the start, implying no title.
             Toast.makeText(context, "Please provide a title before the note content.", Toast.LENGTH_SHORT).show();
             return;
        }
        else {
            // No numbers found, assume the last word is the beginning of the content.
            if (parts.length > 1) {
                title = Arrays.stream(parts, 0, parts.length - 1).collect(Collectors.joining(" "));
                content = parts[parts.length-1];
            } else {
                 Toast.makeText(context, "Note content cannot be empty.", Toast.LENGTH_SHORT).show();
                 return;
            }
        }

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(context, "Note title or content cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File notesDir = new File(context.getFilesDir(), "notes");
            if (!notesDir.exists()) {
                notesDir.mkdirs();
            }
            File noteFile = new File(notesDir, title + ".txt");
            FileOutputStream fos = new FileOutputStream(noteFile);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(context, "Note '" + title + "' saved.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving note.", Toast.LENGTH_SHORT).show();
        }
    }
}
