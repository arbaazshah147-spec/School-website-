package com.jarvis.app.features;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class Downloader {

    private final Context context;

    public Downloader(Context context) {
        this.context = context;
    }

    public void downloadFile(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading file...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true); // Allow download over mobile data
        request.setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(context, "Download started.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Unable to access Download Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Simulates placing an order on a platform like Amazon, Zomato, or Meesho.
     * @param platform The platform to order from.
     * @param item The item to order.
     * @return A confirmation message.
     */
    public String simulateOrder(String platform, String item) {
        return "Simulating an order for '" + item + "' on " + platform + ". In a real app, this would integrate with the respective service's API or use an intent to open the app.";
    }
}
