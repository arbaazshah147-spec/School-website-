enum IntentType {
  finance,
  work,
  personal,
  plan,
  unknown,
}

class IntentFilter {
  IntentType classifyIntent(String userInput) {
    final lowerCaseInput = userInput.toLowerCase();

    if (lowerCaseInput.contains("stock") ||
        lowerCaseInput.contains("market") ||
        lowerCaseInput.contains("price")) {
      return IntentType.finance;
    } else if (lowerCaseInput.contains("schedule") ||
        lowerCaseInput.contains("meeting") ||
        lowerCaseInput.contains("email")) {
      return IntentType.work;
    } else if (lowerCaseInput.contains("how are you") ||
        lowerCaseInput.contains("tell me a joke")) {
      return IntentType.personal;
    } else if (lowerCaseInput.contains("plan") ||
        lowerCaseInput.contains("what should i do")) {
      return IntentType.plan;
    }

    return IntentType.unknown;
  }
}
