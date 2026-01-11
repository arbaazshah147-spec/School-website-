import 'dart:convert';
import 'package:jarvis_flutter/utils/api_client.dart';

class GeminiClient {
  final ApiClient _apiClient;
  final String _apiKey;

  GeminiClient(this._apiKey, {ApiClient? apiClient}) : _apiClient = apiClient ?? ApiClient();

  Future<String> generateContent(String prompt) async {
    final url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$_apiKey";
    final body = {
      "contents": [
        {
          "parts": [
            {"text": prompt}
          ]
        }
      ]
    };

    try {
      final response = await _apiClient.post(url, body);
      if (response.statusCode == 200) {
        final decodedResponse = jsonDecode(response.body);
        return decodedResponse['candidates'][0]['content']['parts'][0]['text'];
      } else {
        return "Error: ${response.statusCode}";
      }
    } catch (e) {
      return "Error: $e";
    }
  }
}
