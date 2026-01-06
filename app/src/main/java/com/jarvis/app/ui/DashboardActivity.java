package com.jarvis.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jarvis.app.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // This activity is currently a simple display of cards.
        // In a real app, each card would be clickable and lead to a
        // dedicated screen for that feature, or display live data.
    }
}
