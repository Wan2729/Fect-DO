package com.example.fectdo.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.HomePage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SettingActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

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

        SwitchCompat Switch1= findViewById(R.id.messageswitch);
        SwitchCompat Switch2= findViewById(R.id.allnotiswitch);
        SwitchCompat Switch3= findViewById(R.id.feedswitch);
        SwitchCompat Switch4= findViewById(R.id.newcourseswitch);

        // Use a unique key for your switch state
        String switchStateKey1 = "switchState1";
        String switchStateKey2 = "switchState2";
        String switchStateKey3 = "switchState3";
        String switchStateKey4 = "switchState4";

        // Get the SharedPreferences object
        SharedPreferences sharedPreferences  = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = getPreferences(Context.MODE_PRIVATE);

        // Save the switch state when it changes
        Switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(switchStateKey1, isChecked);
            editor.apply();
        });

        Switch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean(switchStateKey2, isChecked);
            editor.apply();
        });

        Switch3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putBoolean(switchStateKey3, isChecked);
            editor.apply();
        });

        Switch4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences3.edit();
            editor.putBoolean(switchStateKey4, isChecked);
            editor.apply();
        });

        // Retrieve the switch state when you need it (e.g., in onCreate)
        boolean savedSwitchState1 = sharedPreferences.getBoolean(switchStateKey1, false);
        Switch1.setChecked(savedSwitchState1);

        // Retrieve the switch state when you need it (e.g., in onCreate)
        boolean savedSwitchState2 = sharedPreferences.getBoolean(switchStateKey2, false);
        Switch2.setChecked(savedSwitchState2);

        // Retrieve the switch state when you need it (e.g., in onCreate)
        boolean savedSwitchState3 = sharedPreferences.getBoolean(switchStateKey3, false);
        Switch3.setChecked(savedSwitchState3);

        // Retrieve the switch state when you need it (e.g., in onCreate)
        boolean savedSwitchState4 = sharedPreferences.getBoolean(switchStateKey4, false);
        Switch4.setChecked(savedSwitchState4);



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

    public void showPopup(View v){
        PopupMenu showpopup = new PopupMenu(this, v);
        showpopup.setOnMenuItemClickListener(this);
        showpopup.inflate(R.menu.popup_menu);
        showpopup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.nonoti:
                Toast.makeText(this, "No notification set", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.everyday:
                Toast.makeText(this, "Everyday notification set", Toast.LENGTH_SHORT).show();;
                return true;
            case R.id.every3:
                Toast.makeText(this, "3 day notification set", Toast.LENGTH_SHORT).show();;
                return true;
            case R.id.everyweek:
                Toast.makeText(this, "Every week notification set", Toast.LENGTH_SHORT).show();;
                return true;
            default:
                return false;
        }
    }
}