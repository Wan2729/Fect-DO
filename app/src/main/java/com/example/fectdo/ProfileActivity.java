package com.example.fectdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fectdo.course.Enroll;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        // Set up a listener for BottomNavigationView item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle item selection
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                // Intent for Home
                                Intent homeIntent = new Intent(ProfileActivity.this, Enroll.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_setting:
                                // Intent for Setting
                                Intent settingIntent = new Intent(ProfileActivity.this, SettingActivity.class);
                                startActivity(settingIntent);
                                return true;
                            case R.id.navigation_profile:
                                // Intent for Profile
                                Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }
}