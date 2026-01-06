# Jarvis - AI Assistant + Automation Companion

## App Overview

Jarvis is a production-safe Android application designed to act as a personal AI assistant and automation companion. It is built with a focus on performance on low-end devices and is developed entirely in Java. The application provides a seamless voice and text-based interface for interacting with various AI models and device features.

## Features

- **Multi-AI Integration**: Jarvis intelligently routes user queries to different AI models (Gemini, ChatGPT, Grok) based on the intent of the query, ensuring the best possible response.
- **Customizable Identity**: Users can personalize their experience by setting their username, renaming the AI, and defining a custom wake word and welcome message.
- **Extensible Command System**: A powerful custom command builder allows users to create their own voice commands to open apps, send messages, control media, and more.
- **Feature-Rich Modules**: The app includes a variety of built-in features, such as a task manager, notes manager, wardrobe assistant, fitness coach, and a floating HUD for easy access.
- **Voice and Text Interaction**: Jarvis supports both voice and text input, with a custom wake word simulation and human-like Text-to-Speech output in English and Hindi.
- **Dark Futuristic UI**: The user interface is designed with a dark, futuristic theme that is both aesthetically pleasing and easy on the eyes.

## API Setup

To use the AI features of Jarvis, you will need to obtain API keys from the respective providers and add them to the app's settings screen.

1.  **Gemini API Key**:
    - Go to the [Google AI Studio](https://aistudio.google.com/app/apikey) and create an API key.
    - Open the Jarvis app, navigate to **Settings -> API Management**, and paste the key into the "Gemini API Key" field.

2.  **ChatGPT API Key**:
    - Go to the [OpenAI Platform](https://platform.openai.com/account/api-keys) and create a new secret key.
    - In the Jarvis app's settings, paste the key into the "ChatGPT API Key" field.

3.  **Grok API Key**:
    - Access to the Grok API is currently limited. If you have access, you can find your API key in your X.ai developer portal.
    - Paste the key into the "Grok API Key" field in the app's settings.

**Note**: All API keys are stored securely in the app's private SharedPreferences.

## Permissions

Jarvis requires the following permissions to function correctly:

-   `INTERNET`: To connect to the AI services.
-   `RECORD_AUDIO`: For voice input.
-   `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`: For location-based predictions and suggestions.
-   `CAMERA`: For the (simulated) camera search feature.
-   `SYSTEM_ALERT_WINDOW`: To display the floating HUD. This permission must be granted manually by the user in the device's settings.
-   `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE`: For the downloader and notes features.
-   `FOREGROUND_SERVICE`: To run the floating HUD service.

## Limitations (Non-Root)

As a non-root application, Jarvis has certain limitations:

-   **System Actions**: Jarvis cannot perform actions that require root access, such as turning GPS on/off, enabling/disabling mobile data, or force-closing other apps.
-   **Accessibility Services**: Voice commands for "Back", "Home", and "Recents" are simulated. For full functionality, an Accessibility Service would be required, which is a more advanced implementation not included in this version.
-   **Wake Word**: The wake word detection is software-based and relies on the app's microphone being active. It will not work when the app is in the background or the screen is off, unlike hardware-level wake word detection.
-   **App Integration**: Integration with third-party apps (e.g., for ordering) is simulated. Real integration would require official APIs from those services.
