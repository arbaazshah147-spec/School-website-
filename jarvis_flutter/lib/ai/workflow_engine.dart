import 'package:jarvis_flutter/ai/chatgpt_client.dart';
import 'package:jarvis_flutter/ai/gemini_client.dart';
import 'package:jarvis_flutter/ai/grok_client.dart';
import 'package:jarvis_flutter/ai/intent_filter.dart';
import 'package:jarvis_flutter/ai/result_composer.dart';
import 'package:jarvis_flutter/core/memory_store.dart';
import 'package:jarvis_flutter/utils/constants.dart';

class WorkflowEngine {
  final GeminiClient _geminiClient;
  final ChatGPTClient _chatGPTClient;
  final GrokClient _grokClient;
  final ResultComposer _resultComposer;

  WorkflowEngine(MemoryStore memoryStore)
      : _geminiClient = GeminiClient(memoryStore.getData(Constants.keyGeminiApiKey, '')),
        _chatGPTClient = ChatGPTClient(memoryStore.getData(Constants.keyChatgptApiKey, '')),
        _grokClient = GrokClient(memoryStore.getData(Constants.keyGrokApiKey, '')),
        _resultComposer = ResultComposer();

  void processWorkflow(String input, IntentType intent, Function(String) onResult) async {
    String? geminiResult;
    String? chatgptResult;
    String? grokResult;

    if (intent == IntentType.plan || intent == IntentType.unknown) {
      geminiResult = await _geminiClient.generateContent(input);
    }
    if (intent == IntentType.personal || intent == IntentType.unknown) {
      chatgptResult = await _chatGPTClient.generateCompletion(input);
    }
    if (intent == IntentType.finance || intent == IntentType.work || intent == IntentType.unknown) {
      grokResult = await _grokClient.generateSummary(input);
    }

    final result = _resultComposer.composeResult(
      geminiResult: geminiResult,
      chatgptResult: chatgptResult,
      grokResult: grokResult,
    );

    onResult(result);
  }
}
