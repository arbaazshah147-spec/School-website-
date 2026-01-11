import 'package:flutter/material.dart';
import 'package:jarvis_flutter/ui/home_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Jarvis',
      theme: ThemeData(
        brightness: Brightness.dark,
        primaryColor: const Color(0xFF0A0A1A),
        scaffoldBackgroundColor: const Color(0xFF0A0A1A),
        cardColor: const Color(0xFF1A1A2A),
        textTheme: const TextTheme(
          bodyLarge: TextStyle(color: Colors.white),
          bodyMedium: TextStyle(color: Colors.white70),
        ),
        colorScheme: const ColorScheme.dark(
          primary: Color(0xFF0A0A1A),
          secondary: Color(0xFF00BFFF),
        ),
      ),
      home: const HomeScreen(),
    );
  }
}
