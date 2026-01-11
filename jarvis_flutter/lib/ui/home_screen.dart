import 'package:flutter/material.dart';
import 'package:jarvis_flutter/core/jarvis_brain.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with SingleTickerProviderStateMixin {
  late JarvisBrain _jarvisBrain;
  String _responseText = "Welcome to Jarvis";
  String _partialResponseText = "";
  bool _isListening = false;
  late AnimationController _animationController;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1000),
    )..repeat(reverse: true);

    _jarvisBrain = JarvisBrain(
      onResponse: (response) {
        setState(() {
          _responseText = response;
          _partialResponseText = "";
        });
      },
      onPartialResponse: (partialResponse) {
        setState(() {
          _partialResponseText = partialResponse;
        });
      },
      onListening: (isListening) {
        setState(() {
          _isListening = isListening;
        });
      },
    );
    _jarvisBrain.init();
  }

  @override
  void dispose() {
    _jarvisBrain.dispose();
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              _isListening ? _partialResponseText : _responseText,
              style: Theme.of(context).textTheme.headlineMedium,
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 50),
            GestureDetector(
              onTap: () {
                if (_isListening) {
                  _jarvisBrain.stopListening();
                } else {
                  _jarvisBrain.startListening();
                }
              },
              child: AnimatedBuilder(
                animation: _animationController,
                builder: (context, child) {
                  return Transform.scale(
                    scale: _isListening ? 1.0 + _animationController.value * 0.2 : 1.0,
                    child: Container(
                      width: 150,
                      height: 150,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: Theme.of(context).colorScheme.secondary,
                      ),
                      child: const Icon(
                        Icons.mic,
                        color: Colors.white,
                        size: 75,
                      ),
                    ),
                  );
                },
              ),
            ),
            const SizedBox(height: 50),
            ElevatedButton(
              onPressed: () {
                // Navigate to Dashboard
              },
              child: const Text('Dashboard'),
            ),
          ],
        ),
      ),
    );
  }
}
