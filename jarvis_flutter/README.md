# Jarvis - Flutter AI Assistant

This is a Flutter implementation of the Jarvis AI assistant application. It is a mobile-first, cross-platform application designed to be a complete, all-in-one solution for managing your daily life. It features a multi-AI backend, a futuristic, dark-themed UI, and a wide range of features.

## Features

*   **Multi-AI Workflow:** Jarvis uses a multi-AI chaining system to provide the best possible response to your queries. It routes planning and factual questions to Gemini, personalization to ChatGPT, and analysis to Grok.
*   **Voice and Text Input:** Interact with Jarvis using either voice or text.
*   **Comprehensive Feature Set:** Jarvis includes a wide range of features to help you manage your life, including:
    *   **Task and Schedule Manager:** Keep track of your to-do lists and appointments.
    *   **Notes Manager:** Take notes with your voice, with robust parsing for titles and content.
    *   **Wardrobe Manager:** Get three daily outfit suggestions.
    *   **Fitness Coach:** Get a daily workout to stay in shape.
    *   **Camera Search:** (Simulated) Identify objects in your environment using your camera.
    *   **Downloader:** (Simulated) Download files and apps with a simple voice command.
    *   **Share Manager:** Share text and media with other apps.
    *   **Location Predictor:** Get location-based suggestions.
*   **Customizable:** Jarvis is highly customizable. You can change the AI's name, your username, the wake word, and the welcome message. You can also add your own custom commands.

## Setup

1.  **Clone the repository:**
    ```
    git clone https://github.com/your-username/jarvis-flutter.git
    ```
2.  **Get Flutter dependencies:**
    ```
    flutter pub get
    ```
3.  **Get API Keys:** To use the multi-AI features, you will need to obtain API keys from the following services:
    *   **Google AI Studio:** For the Gemini API key.
    *   **OpenAI:** For the ChatGPT API key.
    *   **X.ai:** For the Grok API key (when available).
4.  **Add API Keys:** Open the **Settings** screen in the app and add your API keys to the appropriate fields.
5.  **Run the app:**
    ```
    flutter run
    ```

## Limitations

*   **Untested Code:** This code has been generated in an environment without a Flutter SDK, and is therefore untested. It may contain bugs or errors.
*   **Simulated Features:** Some features, such as the camera search and downloader, are currently simulated and do not have full functionality.
*   **Grok API:** The Grok API is not yet publicly available, so the Grok client is a placeholder.

## Contributing

Contributions are welcome! Please feel free to open an issue or submit a pull request.
