package com.example.fectdo.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.ProfileActivity;
import com.example.fectdo.R;
import com.example.fectdo.SettingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExamMainPage extends AppCompatActivity implements View.OnClickListener {

    Button math;
    boolean dahJawab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_main_page);

        math = findViewById(R.id.btnMath);
        math.setOnClickListener(this);

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
                                Intent homeIntent = new Intent(ExamMainPage.this, Enroll.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_setting:
                                // Intent for Setting
                                Intent settingIntent = new Intent(ExamMainPage.this, SettingActivity.class);
                                startActivity(settingIntent);
                                return true;
                            case R.id.navigation_profile:
                                // Intent for Profile
                                Intent profileIntent = new Intent(ExamMainPage.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == math.getId()){
            Intent intent = new Intent(ExamMainPage.this, Exam.class);
            startActivity(intent);
        }
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
        Intent intent = new Intent(ExamMainPage.this, Enroll.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}