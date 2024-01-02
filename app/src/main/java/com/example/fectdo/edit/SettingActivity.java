package com.example.fectdo.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.R;
import com.example.fectdo.course.Activity.HomePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Setup Bototm Naviagtion View
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.navigation_profile){
                    Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (item.getItemId()==R.id.navigation_home){
                    Intent intent = new Intent(SettingActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_setting);

        // Set up a listener for BottomNavigationView item clicks
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        // Handle item selection
//                        switch (item.getItemId()) {
//                            case R.id.navigation_home:
//                                // Intent for Home
//                                Intent homeIntent = new Intent(SettingActivity.this, HomePage.class);
//                                startActivity(homeIntent);
//                                overridePendingTransition(0,0);
//                                return true;
//                            case R.id.navigation_setting:
//                                // Intent for Setting
//                                Intent settingIntent = new Intent(SettingActivity.this, SettingActivity.class);
//                                startActivity(settingIntent);
//                                overridePendingTransition(0,0);
//                                return true;
//                            case R.id.navigation_profile:
//                                // Intent for Profile
//                                Intent profileIntent = new Intent(SettingActivity.this, ProfileActivity.class);
//                                startActivity(profileIntent);
//                                overridePendingTransition(0,0);
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                }
//        );
    }
}