package com.jarvis.app.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.jarvis.app.R;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        GridView gridView = findViewById(R.id.dashboard_grid);

        // This is a simple placeholder. A custom adapter would be used
        // in a real application to create the card-based layout.
        ArrayList<String> dashboardItems = new ArrayList<>();
        dashboardItems.add("Tasks");
        dashboardItems.add("Notes");
        dashboardItems.add("Wardrobe");
        dashboardItems.add("Fitness");
        dashboardItems.add("Camera Search");
        dashboardItems.add("Downloads");
        dashboardItems.add("Location");
        dashboardItems.add("Settings");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dashboardItems);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String item = dashboardItems.get(position);
            if(item.equals("Settings")){
                // Start SettingsActivity
            }
            // Handle other item clicks
        });
    }
}
