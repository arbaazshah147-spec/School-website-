import 'dart:convert';
import 'package:jarvis_flutter/utils/api_client.dart';
import 'package:jarvis_flutter/utils/constants.dart';

class ChatGPTClient {
  final ApiClient _apiClient;
  final String _apiKey;

  ChatGPTClient(this._apiKey, {ApiClient? apiClient}) : _apiClient = apiClient ?? ApiClient();

  Future<String> generateCompletion(String prompt) async {
    final body = {
      "model": "gpt-3.5-turbo",
      "messages": [
        {"role": "user", "content": prompt}
      ]
    };

    try {
      final response = await _apiClient.post(Constants.chatgptApiUrl, body, apiKey: _apiKey);
      if (response.statusCode == 200) {
        final decodedResponse = jsonDecode(response.body);
        return decodedResponse['choices'][0]['message']['content'];
      } else {
        return "Error: ${response.statusCode}";
      }
    } catch (e) {
      return "Error: $e";
    }
  }
}
