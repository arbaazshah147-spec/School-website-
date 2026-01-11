import 'package:flutter_tts/flutter_tts.dart';
import 'package:speech_to_text/speech_to_text.dart';
import 'package:speech_to_text/speech_recognition_result.dart';

class SpeechEngine {
  final FlutterTts _tts = FlutterTts();
  final SpeechToText _stt = SpeechToText();
  bool _isInitialized = false;
  final Function(String) onResult;
  final Function(String) onPartialResult;
  final Function() onListening;

  SpeechEngine({required this.onResult, required this.onPartialResult, required this.onListening});

  Future<void> init() async {
    _isInitialized = await _stt.initialize();
  }

  void speak(String text, {double pitch = 1.0, double rate = 1.0}) {
    _tts.setPitch(pitch);
    _tts.setSpeechRate(rate);
    _tts.speak(text);
  }

  void startListening() {
    if (!_isInitialized) return;
    onListening();
    _stt.listen(
      onResult: (SpeechRecognitionResult result) {
        if (result.finalResult) {
          onResult(result.recognizedWords);
        } else {
          onPartialResult(result.recognizedWords);
        }
      },
    );
  }

  void stopListening() {
    _stt.stop();
  }

  void dispose() {
    _tts.stop();
    _stt.cancel();
  }
}
