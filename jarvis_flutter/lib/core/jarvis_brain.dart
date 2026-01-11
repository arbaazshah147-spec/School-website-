import 'package:jarvis_flutter/core/command_router.dart';
import 'package:jarvis_flutter/core/emotion_state_manager.dart';
import 'package:jarvis_flutter/core/memory_store.dart';
import 'package:jarvis_flutter/core/speech_engine.dart';
import 'package:jarvis_flutter/ai/intent_filter.dart';
import 'package:jarvis_flutter/ai/workflow_engine.dart';

class JarvisBrain {
  final MemoryStore _memoryStore;
  final SpeechEngine _speechEngine;
  final EmotionStateManager _emotionStateManager;
  final CommandRouter _commandRouter;
  final IntentFilter _intentFilter;
  final WorkflowEngine _workflowEngine;

  final Function(String) onResponse;
  final Function(String) onPartialResponse;
  final Function(bool) onListening;

  JarvisBrain({
    required this.onResponse,
    required this.onPartialResponse,
    required this.onListening,
  })  : _memoryStore = MemoryStore(),
        _emotionStateManager = EmotionStateManager(),
        _commandRouter = CommandRouter(_memoryStore),
        _intentFilter = IntentFilter(),
        _workflowEngine = WorkflowEngine(_memoryStore),
        _speechEngine = SpeechEngine(
          onResult: (result) {}, // Will be set in init
          onPartialResult: (partialResult) {}, // Will be set in init
          onListening: () {}, // Will be set in init
        ) {
    _speechEngine.onResult = _onSpeechResult;
    _speechEngine.onPartialResult = onPartialResponse;
    _speechEngine.onListening = () => onListening(true);
  }

  Future<void> init() async {
    await _memoryStore.init();
    await _speechEngine.init();
  }

  void _onSpeechResult(String result) {
    onListening(false);
    _processInput(result);
  }

  void _processInput(String input) {
    _emotionStateManager.updateEmotion(input);

    if (_isCommand(input)) {
      _commandRouter.executeCommand(input);
    } else {
      final intent = _intentFilter.classifyIntent(input);
      _workflowEngine.processWorkflow(input, intent, (response) {
        final emotionalResponse = _emotionStateManager.getEmotionalResponse(response);
        onResponse(emotionalResponse);
        _speak(emotionalResponse);
      });
    }
  }

  bool _isCommand(String input) {
    final lowerInput = input.toLowerCase();
    return lowerInput.startsWith("open") || lowerInput.contains("go to");
  }

  void startListening() {
    _speechEngine.startListening();
  }

  void stopListening() {
    _speechEngine.stopListening();
  }

  void _speak(String text) {
    final emotion = _emotionStateManager.currentEmotion;
    double pitch = 1.0;
    double rate = 1.0;

    if (emotion == Emotion.happy || emotion == Emotion.excited) {
      pitch = 1.2;
      rate = 1.1;
    } else if (emotion == Emotion.sad) {
      pitch = 0.8;
      rate = 0.9;
    }

    _speechEngine.speak(text, pitch: pitch, rate: rate);
  }

  void dispose() {
    _speechEngine.dispose();
  }
}
