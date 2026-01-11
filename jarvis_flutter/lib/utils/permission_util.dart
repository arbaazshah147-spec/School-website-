import 'package:permission_handler/permission_handler.dart';

class PermissionUtil {
  static Future<bool> requestPermissions(List<Permission> permissions) async {
    Map<Permission, PermissionStatus> statuses = await permissions.request();
    return statuses.values.every((status) => status.isGranted);
  }

  static Future<bool> checkPermissions(List<Permission> permissions) async {
    for (var permission in permissions) {
      if (await permission.isDenied) {
        return false;
      }
    }
    return true;
  }
}
