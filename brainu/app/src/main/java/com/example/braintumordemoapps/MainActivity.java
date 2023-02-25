package com.example.braintumordemoapps;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        //default selected item
        bottomNavigationView.setSelectedItemId(R.id.detection_item);

        // Set the item selected listener to open the activity_main.xml file
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.detection_item:
                        DetectionFragment detectionFragment = new DetectionFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, detectionFragment).commit();
                        return true;
                    case R.id.about_us_item:
                        AboutUsFragment aboutUsFragment = new AboutUsFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, aboutUsFragment).commit();
                        return true;
                    case R.id.references_item:
                        ReferencesFragment referencesFragment = new ReferencesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, referencesFragment).commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
