import 'dart:math';
import 'package:jarvis_flutter/features/outfit.dart';

class WardrobeManager {
  final List<Outfit> _outfits = [
    Outfit("Casual", "T-Shirt and Jeans", "sneakers"),
    Outfit("Formal", "Suit and Tie", "dress_shoes"),
    Outfit("Sporty", "Tracksuit", "running_shoes"),
    Outfit("Business Casual", "Polo Shirt and Chinos", "loafers"),
    Outfit("Winter Wear", "Jacket and Beanie", "boots"),
    Outfit("Summer Vibe", "Shorts and Vest", "sandals"),
  ];

  List<Outfit> getDailySuggestions() {
    final suggestions = <Outfit>[];
    final random = Random(DateTime.now().day);

    if (_outfits.length < 3) {
      return _outfits;
    }

    while (suggestions.length < 3) {
      final suggestion = _outfits[random.nextInt(_outfits.length)];
      if (!suggestions.contains(suggestion)) {
        suggestions.add(suggestion);
      }
    }
    return suggestions;
  }
}
