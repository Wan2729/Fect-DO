package com.example.fectdo.utils;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.fectdo.R;
import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.edit.ProfileActivity;
import com.example.fectdo.edit.SettingActivity;
import com.example.fectdo.general.LoginEmailPassword;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation {
    private AppCompatActivity activity;

    public Navigation(AppCompatActivity activity) {
        this.activity = activity;
    }


    public void setToolbarAndBottomNavigation(int toolbarId, int bottomNavId) {
        // Set toolbar
        Toolbar toolbar = activity.findViewById(toolbarId);
        activity.setSupportActionBar(toolbar);

        // Set bottom navigation
        BottomNavigationView bottomNavigationView = activity.findViewById(bottomNavId);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    // Handle item selection
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            navigateTo(HomePage.class);
                            return true;
                        case R.id.navigation_setting:
                            navigateTo(SettingActivity.class);
                            return true;
                        case R.id.navigation_profile:
                            navigateTo(ProfileActivity.class);
                            return true;
                        default:
                            return false;
                    }
                }
        );
    }

    public void handleLogout() {
        FirebaseUtil.logOut();
        Intent intent = new Intent(activity, LoginEmailPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }


    private void navigateTo(Class<?> destinationClass) {
        Intent intent = new Intent(activity, destinationClass);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }


}
