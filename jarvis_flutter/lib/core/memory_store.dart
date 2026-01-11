import 'package:shared_preferences/shared_preferences.dart';

class MemoryStore {
  late SharedPreferences _prefs;

  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  Future<void> saveData(String key, String value) async {
    await _prefs.setString(key, value);
  }

  String getData(String key, String defaultValue) {
    return _prefs.getString(key) ?? defaultValue;
  }
}
