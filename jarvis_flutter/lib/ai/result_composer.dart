class ResultComposer {
  String composeResult({
    String? geminiResult,
    String? chatgptResult,
    String? grokResult,
  }) {
    final buffer = StringBuffer();

    if (geminiResult != null && geminiResult.isNotEmpty) {
      buffer.writeln("Planning: $geminiResult\n");
    }
    if (chatgptResult != null && chatgptResult.isNotEmpty) {
      buffer.writeln("Personalization: $chatgptResult\n");
    }
    if (grokResult != null && grokResult.isNotEmpty) {
      buffer.writeln("Analysis: $grokResult");
    }

    if (buffer.isEmpty) {
      return "I was unable to process that request.";
    }

    return buffer.toString().trim();
  }
}
