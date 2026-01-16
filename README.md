# Jarvis - Your Personal AI Assistant

Jarvis is a mobile-first AI assistant designed to provide a seamless voice-to-voice experience on Android devices. It's built to be fast, smart, and emotionally aware, with a focus on customization and automation.

## Features

*   **Conversational Chat:** A voice-first chat interface that allows for continuous conversation with Jarvis.
*   **Dashboard Modules:** A dashboard that displays useful information, such as your tasks, notes, wardrobe suggestions, and fitness tips.
*   **Multi-AI Workflow:** Jarvis uses a multi-AI workflow to route your requests to the best model for the job.
    *   **Gemini:** for planning and factual queries.
    *   **ChatGPT:** for personalization and memory.
    *   **Grok:** for analysis and summarization.
*   **Automation & Control:** Jarvis can perform a variety of tasks, such as opening apps, making phone calls, sending messages, and playing YouTube videos.
*   **Floating HUD:** An optional overlay that displays news, calendar events, and other useful information.
*   **Customization:** You can customize Jarvis's name, your name, the wake word, and the welcome message. You can also create custom commands to perform specific actions.

## Setup

1.  **Clone the repository:**
    ```
    git clone https://github.com/your-username/jarvis-android.git
    ```
2.  **Open the project in Android Studio.**
3.  **Get your API keys:**
    *   [Gemini](https://aistudio.google.com/app/apikey)
    *   [ChatGPT](https://platform.openai.com/api-keys)
    *   [Grok](https://console.groq.com/keys)
4.  **Add your API keys to the app:**
    *   Run the app and go to the **Settings** screen.
    *   Enter your API keys in the appropriate fields.
    *   Click the **Test** button to validate your keys.
    *   Click the **Save** button to save your settings.

## Supported Commands

### Direct Commands

*   **`open [app name]`:** Opens the specified app.
*   **`call [contact name or number]`:** Makes a phone call to the specified contact.
*   **`send text to [phone number] message [message]`:** Sends a text message to the specified phone number.
*   **`send whatsapp to [phone number] message [message]`:** Sends a WhatsApp message to the specified phone number.
*   **`play [query]`:** Searches for and plays a video on YouTube.
*   **`search for [query]`:** Performs a web search.

### Custom Commands

You can create custom commands in the **Settings** screen. Custom commands can be used to perform any of the supported actions.

## Building the App

To build the app, you can use the following Gradle command:

```
./gradlew assembleDebug
```

This will create an APK file in the `app/build/outputs/apk/debug` directory.
