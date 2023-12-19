package com.example.fectdo.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.fectdo.CourseDataBase.CourseModel;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.edit.ProfileActivity;
import com.example.fectdo.R;
import com.example.fectdo.edit.SettingActivity;
import com.example.fectdo.general.LoginEmailPassword;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Enroll extends AppCompatActivity {
    final String COURSE_KEY = "course";
    ImageView uploadButton;
    CollectionReference courseCollectionRef;
    CourseModel course;
    LinearLayout courseLayout;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        courseLayout = findViewById(R.id.courseLayout);
        courseCollectionRef = FirebaseFirestore.getInstance().collection(COURSE_KEY);

        uploadButton = findViewById(R.id.btnUpload);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                letupload();
            }
        });

        searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enroll.this, CourseList.class);
                startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();

        courseLayout.removeAllViews();
        courseCollectionRef.addSnapshotListener(Enroll.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(Enroll.this, error.toString());
                    return;
                }

                if(queryDocumentSnapshots != null){
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        course = documentSnapshot.toObject(CourseModel.class);

                        addCard(course);
                    }
                }
                else{
                    AndroidUtil.showToast(Enroll.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });
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

    public void goToCareer(View view){
        Intent intent = new Intent(this, CareerMain.class);
        startActivity(intent);
    }

    void gotoCourse(){

    }

    void addCard(CourseModel course){
        View courseCardView = getLayoutInflater().inflate(R.layout.course_card, null);

        TextView courseName = courseCardView.findViewById(R.id.tvCourseName);
        courseName.setText(course.getCourseName());

        ImageButton courseButton = courseCardView.findViewById(R.id.ivCourseIcon);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enroll.this, VideoChemPage.class);
                startActivity(intent);
            }
        });

        courseLayout.addView(courseCardView);
    }
}