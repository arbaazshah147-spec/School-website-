# Jarvis - Android AI Assistant

Jarvis is a mobile-first Android AI assistant application designed to be a complete, all-in-one solution for managing your daily life. It features a multi-AI backend, a futuristic, dark-themed UI, and a wide range of features, all optimized for low-end Android devices.

## Features

*   **Multi-AI Workflow:** Jarvis uses a multi-AI chaining system to provide the best possible response to your queries. It routes planning and factual questions to Gemini, personalization to ChatGPT, and analysis to Grok.
*   **Voice and Text Input:** Interact with Jarvis using either voice or text. The app features a custom wake word and continuous session mode for a seamless experience.
*   **Floating HUD:** When granted the overlay permission, Jarvis can display a floating HUD with real-time information, such as news, calendar events, and stock prices.
*   **Comprehensive Feature Set:** Jarvis includes a wide range of features to help you manage your life, including:
    *   **Task and Schedule Manager:** Keep track of your to-do lists and appointments.
    *   **Notes Manager:** Take notes with your voice, with robust parsing for titles and content.
    *   **Wardrobe Manager:** Get three daily outfit suggestions, complete with visual representations.
    *   **Fitness Coach:** Get a daily workout to stay in shape.
    *   **Camera Search:** (Simulated) Identify objects in your environment using your camera.
    *   **Downloader:** Download files and apps with a simple voice command.
    *   **Share Manager:** Share text, photos, videos, and audio with other apps.
    *   **Location Predictor:** Get location-based suggestions for restaurants, traffic, and more.
*   **Customizable:** Jarvis is highly customizable. You can change the AI's name, your username, the wake word, and the welcome message. You can also add your own custom commands to perform any action you want.

## Setup

1.  **Clone the repository:**
    ```
    git clone https://github.com/your-username/jarvis.git
    ```
2.  **Open in Android Studio:** Open the project in Android Studio or your preferred IDE.
3.  **Get API Keys:** To use the multi-AI features, you will need to obtain API keys from the following services:
    *   **Google AI Studio:** For the Gemini API key.
    *   **OpenAI:** For the ChatGPT API key.
    *   **X.ai:** For the Grok API key (when available).
4.  **Add API Keys:** Open the **Settings** screen in the app and add your API keys to the appropriate fields.
5.  **Build and Run:** Build the project and run it on your Android device or emulator.

## Permissions

Jarvis requires the following permissions to function correctly:

*   **INTERNET:** To connect to the AI APIs.
*   **RECORD_AUDIO:** To use the voice input feature.
*   **SYSTEM_ALERT_WINDOW:** To display the floating HUD.
*   **ACCESS_FINE_LOCATION:** To provide location-based suggestions.
*   **READ_EXTERNAL_STORAGE / WRITE_EXTERNAL_STORAGE:** To download files and access media for sharing.
*   **CAMERA:** To use the camera search feature.

## Limitations

*   **Simulated Features:** Some features, such as the camera search and location predictor, are currently simulated and do not have full functionality.
*   **Grok API:** The Grok API is not yet publicly available, so the Grok client is a placeholder.
*   **Basic UI:** The UI is functional but basic. A more polished and animated UI will be implemented in a future release.
*   **File-Based Storage:** The Task Manager and Notes Manager use a simple file-based storage system. While this is an improvement over in-memory storage, a more robust database solution will be implemented in a future release.

## Contributing

Contributions are welcome! Please feel free to open an issue or submit a pull request.
