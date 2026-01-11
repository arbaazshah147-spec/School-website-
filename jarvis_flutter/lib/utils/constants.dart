class Constants {
  // API Endpoints
  static const String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
  static const String chatgptApiUrl = "https://api.openai.com/v1/chat/completions";
  static const String grokApiUrl = "https://api.x.ai/v1/grok"; // Placeholder

  // SharedPreferences Keys
  static const String prefsName = "JarvisPrefs";
  static const String keyAiName = "ai_name";
  static const String keyUsername = "username";
  static const String keyWakeWord = "wake_word";
  static const String keyWelcomeResponse = "welcome_response";
  static const String keyGeminiApiKey = "gemini_api_key";
  static const String keyChatgptApiKey = "chatgpt_api_key";
  static const String keyGrokApiKey = "grok_api_key";
  static const String keyPersonalInfo = "personal_info";
  static const String keyCustomCommands = "custom_commands";
}
