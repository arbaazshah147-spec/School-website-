import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiClient {
  final http.Client _client;

  ApiClient({http.Client? client}) : _client = client ?? http.Client();

  Future<http.Response> post(String url, Map<String, dynamic> body, {String? apiKey}) async {
    final headers = {
      'Content-Type': 'application/json',
    };
    if (apiKey != null) {
      headers['Authorization'] = 'Bearer $apiKey';
    }

    return await _client.post(
      Uri.parse(url),
      headers: headers,
      body: jsonEncode(body),
    );
  }
}
