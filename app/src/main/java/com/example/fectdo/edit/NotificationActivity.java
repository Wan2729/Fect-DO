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

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Button Nextpage = findViewById(R.id.profilsettingbutton);
        Nextpage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                profilepage();
            }
        });

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
                                Intent homeIntent = new Intent(NotificationActivity.this, HomePage.class);
                                startActivity(homeIntent);
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.navigation_setting:
                                // Intent for Setting
                                Intent settingIntent = new Intent(NotificationActivity.this, SettingActivity.class);
                                startActivity(settingIntent);
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.navigation_profile:
                                // Intent for Profile
                                Intent profileIntent = new Intent(NotificationActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                overridePendingTransition(0,0);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }

    void profilepage(){
        Intent SettingActivity=new Intent(this,SettingActivity.class);
        startActivity(SettingActivity);
    }
}