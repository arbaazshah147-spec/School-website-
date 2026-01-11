import 'dart:convert';
import 'package:jarvis_flutter/utils/api_client.dart';
import 'package:jarvis_flutter/utils/constants.dart';

class GrokClient {
  final ApiClient _apiClient;
  final String _apiKey;

  GrokClient(this._apiKey, {ApiClient? apiClient}) : _apiClient = apiClient ?? ApiClient();

  Future<String> generateSummary(String prompt) async {
    // NOTE: This is a placeholder implementation as the Grok API is not public.
    final body = {
      "prompt": prompt,
      "model": "grok-1",
    };

    try {
      final response = await _apiClient.post(Constants.grokApiUrl, body, apiKey: _apiKey);
      if (response.statusCode == 200) {
        final decodedResponse = jsonDecode(response.body);
        return decodedResponse['summary'];
      } else {
        return "Error: ${response.statusCode}";
      }
    } catch (e) {
      return "Error: $e";
    }
  }
}
