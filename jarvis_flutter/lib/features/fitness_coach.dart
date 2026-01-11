import 'dart:math';

class FitnessCoach {
  final List<String> _workouts = [
    "Push-ups: 3 sets of 12",
    "Squats: 3 sets of 15",
    "Plank: 3 sets of 60 seconds",
    "Jumping Jacks: 3 sets of 30",
    "Lunges: 3 sets of 10 per leg",
  ];

  String getWorkoutOfTheDay() {
    final random = Random(DateTime.now().day);
    return _workouts[random.nextInt(_workouts.length)];
  }
}
