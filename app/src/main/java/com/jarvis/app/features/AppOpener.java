package com.jarvis.app.features;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

public class AppOpener {
    private Context context;

    public AppOpener(Context context) {
        this.context = context;
    }

    public void openApp(String... appNameParts) {
        String appName = String.join(" ", appNameParts).toLowerCase();
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : allApps) {
            String label = ri.loadLabel(manager).toString().toLowerCase();
            if (label.contains(appName)) {
                Intent launchIntent = manager.getLaunchIntentForPackage(ri.activityInfo.packageName);
                if (launchIntent != null) {
                    context.startActivity(launchIntent);
                    return;
                }
            }
        }
        // If no app is found, inform the user.
        Toast.makeText(context, "Could not find an app named '" + appName + "'", Toast.LENGTH_SHORT).show();
    }
}
