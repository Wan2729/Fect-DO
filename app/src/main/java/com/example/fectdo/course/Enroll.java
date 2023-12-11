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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fectdo.CourseDataBase.Course;
import com.example.fectdo.ProfileActivity;
import com.example.fectdo.R;
import com.example.fectdo.SettingActivity;
import com.example.fectdo.general.LoginEmailPassword;
import com.example.fectdo.general.SignUpPhoneNumber;
import com.example.fectdo.general.SignUpUsernameEmailPassword;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Enroll extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        final ImageView NextpagePhysics = findViewById(R.id.btnPhysics);
        final ImageView NextpageChemistry = findViewById(R.id.btnChem);
        final ImageView NextpageMathematic = findViewById(R.id.btnMath);
        final ImageView LetsUpload = findViewById(R.id.btnUpload);
        NextpagePhysics.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                nextpagephy();
            }
        });
        NextpageChemistry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                nextpagechem();
            }
        });
        NextpageMathematic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                nextpagemath();
            }
        });
        LetsUpload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                letupload();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle item selection
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                // Intent for Home
                                Intent homeIntent = new Intent(Enroll.this, Enroll.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_setting:
                                // Intent for Setting
                                Intent settingIntent = new Intent(Enroll.this, SettingActivity.class);
                                startActivity(settingIntent);
                                return true;
                            case R.id.navigation_profile:
                                // Intent for Profile
                                Intent profileIntent = new Intent(Enroll.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );
    }

    void nextpagephy(){
        Intent Physic=new Intent(this, VideoPhysicsPage.class);
        startActivity(Physic);
    }
    void nextpagechem(){
        Intent Chemistry=new Intent(this, VideoChemPage.class);
        startActivity(Chemistry);
    }
    void nextpagemath(){
        Intent Mathematic=new Intent(this, VideoMathPage.class);
        startActivity(Mathematic);
    }
    void letupload(){
        Intent upload=new Intent(this, UploadActivity.class);
        startActivity(upload);

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
        FirebaseUtil.logOut();
        Intent intent = new Intent(Enroll.this, LoginEmailPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}