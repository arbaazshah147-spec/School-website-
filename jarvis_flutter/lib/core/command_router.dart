import 'dart:convert';
import 'package:jarvis_flutter/core/memory_store.dart';
import 'package:jarvis_flutter/utils/constants.dart';
import 'package:url_launcher/url_launcher.dart';

class CommandRouter {
  final MemoryStore _memoryStore;
  Map<String, CustomCommand> _customCommands = {};

  CommandRouter(this._memoryStore) {
    _loadCustomCommands();
  }

  void _loadCustomCommands() {
    try {
      final commandsJson = _memoryStore.getData(Constants.keyCustomCommands, '{}');
      final commandsMap = jsonDecode(commandsJson) as Map<String, dynamic>;
      _customCommands = commandsMap.map((key, value) => MapEntry(key, CustomCommand.fromJson(value)));
    } on FormatException {
      // If the JSON is malformed, start with an empty map
      _customCommands = {};
    }
  }

  void executeCommand(String command) {
    final lowerCaseCommand = command.toLowerCase();

    for (final customCommand in _customCommands.values) {
      if (lowerCaseCommand.contains(customCommand.triggerPhrase.toLowerCase())) {
        _performAction(customCommand.actionType, customCommand.actionValue);
        return;
      }
    }

    if (lowerCaseCommand.contains("open")) {
      final appName = lowerCaseCommand.replaceFirst("open", "").trim();
      _openApp(appName);
    } else if (lowerCaseCommand.contains("go to settings")) {
      // This will be handled by the UI
    }
  }

  Future<void> _performAction(String actionType, String actionValue) async {
    switch (actionType.toUpperCase()) {
      case "OPEN_APP":
        _openApp(actionValue);
        break;
      case "OPEN_URL":
        final uri = Uri.parse(actionValue);
        if (await canLaunchUrl(uri)) {
          await launchUrl(uri);
        }
        break;
    }
  }

  Future<void> _openApp(String appName) async {
    // This is a simplified implementation. A more robust solution would
    // use platform channels to get a list of installed apps.
    if (appName.contains("chrome")) {
      await launchUrl(Uri.parse("https://google.com"));
    }
  }

  void addCustomCommand(String name, String trigger, String actionType, String actionValue) {
    _customCommands[name] = CustomCommand(name, trigger, actionType, actionValue);
    final commandsJson = jsonEncode(_customCommands.map((key, value) => MapEntry(key, value.toJson())));
    _memoryStore.saveData(Constants.keyCustomCommands, commandsJson);
  }
}

class CustomCommand {
  final String name;
  final String triggerPhrase;
  final String actionType;
  final String actionValue;

  CustomCommand(this.name, this.triggerPhrase, this.actionType, this.actionValue);

  factory CustomCommand.fromJson(Map<String, dynamic> json) {
    return CustomCommand(
      json['name'],
      json['triggerPhrase'],
      json['actionType'],
      json['actionValue'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'triggerPhrase': triggerPhrase,
      'actionType': actionType,
      'actionValue': actionValue,
    };
  }
}
