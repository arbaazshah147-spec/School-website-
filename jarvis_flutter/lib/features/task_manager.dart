import 'dart:convert';
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:jarvis_flutter/features/task.dart';

class TaskManager {
  static const String _fileName = 'tasks.json';
  List<Task> _tasks = [];

  Future<String> get _localPath async {
    final directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  Future<File> get _localFile async {
    final path = await _localPath;
    return File('$path/$_fileName');
  }

  Future<void> loadTasks() async {
    try {
      final file = await _localFile;
      if (!await file.exists()) {
        _tasks = [];
        return;
      }
      final contents = await file.readAsString();
      final List<dynamic> json = jsonDecode(contents);
      _tasks = json.map((item) => Task.fromJson(item)).toList();

      if (_tasks.isNotEmpty) {
        Task.setNextId(_tasks.map((task) => task.id).reduce((a, b) => a > b ? a : b) + 1);
      }
    } catch (e) {
      _tasks = [];
    }
  }

  Future<void> _saveTasks() async {
    final file = await _localFile;
    final json = jsonEncode(_tasks.map((task) => task.toJson()).toList());
    await file.writeAsString(json);
  }

  void addTask(String description) {
    _tasks.add(Task(description));
    _saveTasks();
  }

  void deleteTask(int taskId) {
    _tasks.removeWhere((task) => task.id == taskId);
    _saveTasks();
  }

  void markTaskAsCompleted(int taskId) {
    for (final task in _tasks) {
      if (task.id == taskId) {
        task.isCompleted = true;
        _saveTasks();
        return;
      }
    }
  }

  List<Task> getTasks() {
    return _tasks;
  }
}
