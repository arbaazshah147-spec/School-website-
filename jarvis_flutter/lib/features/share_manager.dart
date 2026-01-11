import 'package:share_plus/share_plus.dart';

class ShareManager {
  void shareText(String text) {
    Share.share(text);
  }

  void shareMedia(String filePath) {
    Share.shareXFiles([XFile(filePath)]);
  }
}
