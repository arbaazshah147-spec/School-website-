import 'package:flutter/material.dart';
import 'package:jarvis_flutter/core/memory_store.dart';
import 'package:jarvis_flutter/utils/constants.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key});

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  final _formKey = GlobalKey<FormState>();
  final _memoryStore = MemoryStore();
  final Map<String, TextEditingController> _controllers = {};

  @override
  void initState() {
    super.initState();
    _initMemoryStoreAndControllers();
  }

  void _initMemoryStoreAndControllers() async {
      await _memoryStore.init();
      _loadSettings();
  }

  void _loadSettings() {
    setState(() {
      _controllers[Constants.keyAiName] = TextEditingController(text: _memoryStore.getData(Constants.keyAiName, 'Jarvis'));
      _controllers[Constants.keyUsername] = TextEditingController(text: _memoryStore.getData(Constants.keyUsername, ''));
      _controllers[Constants.keyWakeWord] = TextEditingController(text: _memoryStore.getData(Constants.keyWakeWord, 'Hey Jarvis'));
      _controllers[Constants.keyWelcomeResponse] = TextEditingController(text: _memoryStore.getData(Constants.keyWelcomeResponse, 'Hello, how can I help?'));
      _controllers[Constants.keyGeminiApiKey] = TextEditingController(text: _memoryStore.getData(Constants.keyGeminiApiKey, ''));
      _controllers[Constants.keyChatgptApiKey] = TextEditingController(text: _memoryStore.getData(Constants.keyChatgptApiKey, ''));
      _controllers[Constants.keyGrokApiKey] = TextEditingController(text: _memoryStore.getData(Constants.keyGrokApiKey, ''));
      _controllers[Constants.keyPersonalInfo] = TextEditingController(text: _memoryStore.getData(Constants.keyPersonalInfo, ''));
    });
  }

  void _saveSettings() {
    if (_formKey.currentState!.validate()) {
      _controllers.forEach((key, controller) {
        _memoryStore.saveData(key, controller.text);
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Settings Saved')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
      ),
      body: _controllers.isEmpty ? Center(child: CircularProgressIndicator()) : Form(
        key: _formKey,
        child: ListView(
          padding: const EdgeInsets.all(16.0),
          children: [
            _buildTextField(Constants.keyAiName, 'AI Name'),
            _buildTextField(Constants.keyUsername, 'Your Name'),
            _buildTextField(Constants.keyWakeWord, 'Wake Word'),
            _buildTextField(Constants.keyWelcomeResponse, 'Welcome Response'),
            const Divider(),
            _buildTextField(Constants.keyGeminiApiKey, 'Gemini API Key'),
            _buildTextField(Constants.keyChatgptApiKey, 'ChatGPT API Key'),
            _buildTextField(Constants.keyGrokApiKey, 'Grok API Key'),
            const Divider(),
            _buildTextField(Constants.keyPersonalInfo, 'Personal Info', maxLines: 3),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _saveSettings,
              child: const Text('Save Settings'),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildTextField(String key, String label, {int maxLines = 1}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: TextFormField(
        controller: _controllers[key],
        decoration: InputDecoration(
          labelText: label,
          border: const OutlineInputBorder(),
        ),
        maxLines: maxLines,
      ),
    );
  }
}
