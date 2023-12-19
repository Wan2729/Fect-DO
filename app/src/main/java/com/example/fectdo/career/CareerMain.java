package com.example.fectdo.career;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fectdo.R;
import com.example.fectdo.course.Enroll;
import com.example.fectdo.edit.ProfileActivity;
import com.example.fectdo.edit.SettingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CareerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                                Intent homeIntent = new Intent(CareerMain.this, Enroll.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_setting:
                                // Intent for Setting
                                Intent settingIntent = new Intent(CareerMain.this, SettingActivity.class);
                                startActivity(settingIntent);
                                return true;
                            case R.id.navigation_profile:
                                // Intent for Profile
                                Intent profileIntent = new Intent(CareerMain.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleLogout() {
        Intent intent = new Intent(CareerMain.this, Enroll.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}