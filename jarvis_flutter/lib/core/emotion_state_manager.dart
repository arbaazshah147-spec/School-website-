enum Emotion {
  neutral,
  happy,
  sad,
  angry,
  excited,
}

class EmotionStateManager {
  Emotion _currentEmotion = Emotion.neutral;

  final Map<String, Emotion> _keywordEmotionMap = {
    "happy": Emotion.happy,
    "excited": Emotion.excited,
    "sad": Emotion.sad,
    "angry": Emotion.angry,
    "great": Emotion.happy,
    "awesome": Emotion.excited,
    "terrible": Emotion.sad,
    "frustrated": Emotion.angry,
  };

  Emotion get currentEmotion => _currentEmotion;

  void updateEmotion(String userInput) {
    final lowerCaseInput = userInput.toLowerCase();
    for (final entry in _keywordEmotionMap.entries) {
      if (lowerCaseInput.contains(entry.key)) {
        _currentEmotion = entry.value;
        return;
      }
    }
    _currentEmotion = Emotion.neutral;
  }

  String getEmotionalResponse(String baseResponse) {
    switch (_currentEmotion) {
      case Emotion.happy:
        return "That's great to hear! $baseResponse";
      case Emotion.sad:
        return "I'm sorry to hear that. $baseResponse";
      case Emotion.angry:
        return "I understand your frustration. $baseResponse";
      case Emotion.excited:
        return "Awesome! $baseResponse";
      default:
        return baseResponse;
    }
  }
}
