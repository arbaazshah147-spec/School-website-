import 'dart:convert';
import 'dart:io';
import 'package:path_provider/path_provider.dart';

class NotesManager {
  static const String _fileName = 'notes.json';
  Map<String, String> _notes = {};

  Future<String> get _localPath async {
    final directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  Future<File> get _localFile async {
    final path = await _localPath;
    return File('$path/$_fileName');
  }

  Future<void> loadNotes() async {
    try {
      final file = await _localFile;
      if (!await file.exists()) {
        _notes = {};
        return;
      }
      final contents = await file.readAsString();
      _notes = Map<String, String>.from(jsonDecode(contents));
    } catch (e) {
      _notes = {};
    }
  }

  Future<void> _saveNotes() async {
    final file = await _localFile;
    final json = jsonEncode(_notes);
    await file.writeAsString(json);
  }

  void createNote(String voiceInput) {
    final title = _parseTitle(voiceInput);
    final content = _parseContent(voiceInput);

    if (content.isNotEmpty) {
      _notes[title] = content;
      _saveNotes();
    }
  }

  void deleteNote(String title) {
    if (_notes.containsKey(title)) {
      _notes.remove(title);
      _saveNotes();
    }
  }

  String _parseTitle(String input) {
    final match = RegExp(r"title (.*?)(,|numbers|content)").firstMatch(input.toLowerCase());
    return match?.group(1)?.trim() ?? "Note";
  }

  String _parseContent(String input) {
    final match = RegExp(r"(numbers|content) (.*)").firstMatch(input.toLowerCase());
    return match?.group(2)?.trim() ?? input;
  }

  Map<String, String> getNotes() {
    return _notes;
  }
}
