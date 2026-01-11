import 'package:flutter/material.dart';
import 'package:jarvis_flutter/ui/dashboard_card.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Dashboard'),
      ),
      body: GridView.count(
        crossAxisCount: 2,
        padding: const EdgeInsets.all(16.0),
        children: [
          DashboardCard(
            title: 'Tasks',
            icon: Icons.check_circle_outline,
            onTap: () {
              // Navigate to Tasks Screen
            },
          ),
          DashboardCard(
            title: 'Notes',
            icon: Icons.note_alt_outlined,
            onTap: () {
              // Navigate to Notes Screen
            },
          ),
          DashboardCard(
            title: 'Wardrobe',
            icon: Icons.styler_outlined,
            onTap: () {
              // Navigate to Wardrobe Screen
            },
          ),
          DashboardCard(
            title: 'Fitness',
            icon: Icons.fitness_center_outlined,
            onTap: () {
              // Navigate to Fitness Screen
            },
          ),
          DashboardCard(
            title: 'Camera Search',
            icon: Icons.camera_alt_outlined,
            onTap: () {
              // Navigate to Camera Search Screen
            },
          ),
          DashboardCard(
            title: 'Downloads',
            icon: Icons.download_outlined,
            onTap: () {
              // Navigate to Downloads Screen
            },
          ),
          DashboardCard(
            title: 'Location',
            icon: Icons.location_on_outlined,
            onTap: () {
              // Navigate to Location Screen
            },
          ),
          DashboardCard(
            title: 'Settings',
            icon: Icons.settings_outlined,
            onTap: () {
              // Navigate to Settings Screen
            },
          ),
        ],
      ),
    );
  }
}
