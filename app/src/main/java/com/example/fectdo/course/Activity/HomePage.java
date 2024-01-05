package com.example.fectdo.course.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.TAG.TAG;
import com.example.fectdo.adapter.MyCourseAdapter;
import com.example.fectdo.course.UploadActivity;
import com.example.fectdo.edit.ProfileActivity;
import com.example.fectdo.edit.SettingActivity;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.R;
import com.example.fectdo.social.MainFeedActivity;
import com.example.fectdo.social.SocialActivity;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.example.fectdo.utils.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomePage extends AppCompatActivity {
    final String COURSE_KEY = "course";
    ImageView uploadButton;
    ImageButton manageCourse;
    Button searchButton;
    ImageButton socialButton, btnCommunity;
    BottomNavigationView bottomNavigationView;

    // Database
    CollectionReference courseCollectionRef;
    DocumentReference userDocumentRef;
    CourseModel course;

    // For recycler view
    RecyclerView recyclerView;
    MyCourseAdapter myCourseAdapter;
    LinearLayoutManager layoutManager;
    List<CourseModel> myCourseList;

    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        courseCollectionRef = FirebaseFirestore.getInstance().collection(COURSE_KEY);
        userDocumentRef = FirebaseUtil.currentUserDetails();
        socialButton = findViewById(R.id.btnSosial);

        uploadButton = findViewById(R.id.btnUpload);

        navigation = new Navigation(this);

        // Set up Toolbar and Bottom Navigation
        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);

        // Set up Bottom Navigation View
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_setting:
                        startActivity(new Intent(HomePage.this, SettingActivity.class));
                        finish();
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(HomePage.this, ProfileActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });

        Log.d("Authentication", "User ID: " + currentUser.getUid());
        Log.d("Authentication", "Display Name: " + currentUser.getDisplayName());
        Log.d("Authentication", "Email: " + currentUser.getEmail());
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddCourse.class);
                intent.putExtra(TAG.COURSE_PATH, courseCollectionRef.getPath());
                intent.putExtra(TAG.USER_ID, FirebaseUtil.currentUserId());
                intent.putExtra(TAG.NEW_COURSE, true);
                startActivity(intent);
            }
        });

        // Setup recyclerView
        recyclerView = findViewById(R.id.rvMyCourse);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        myCourseAdapter = new MyCourseAdapter(getApplicationContext());
        btnCommunity = findViewById(R.id.btnCommunity);

        searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CourseListPage.class);
                intent.putExtra("COURSE_COLLECTION_REFERENCE", courseCollectionRef.getPath());
                intent.putExtra("USER_ID", FirebaseUtil.currentUserId());
                startActivity(intent);
            }
        });

        manageCourse = findViewById(R.id.btnManageCourse);
        manageCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ManageCoursePage.class);
                intent.putExtra("COURSE_COLLECTION_REFERENCE", courseCollectionRef.getPath());
                intent.putExtra("USER_ID", FirebaseUtil.currentUserId());
                startActivity(intent);
            }
        });

        socialButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MainFeedActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CollectionReference enrollment = FirebaseUtil.getCollection("enrollment");

        enrollment.whereEqualTo("userID", FirebaseUtil.currentUserId())
                .addSnapshotListener(HomePage.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            AndroidUtil.showToast(HomePage.this, error.toString());
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            myCourseList = new ArrayList<>();
                            int totalTasks = queryDocumentSnapshots.size();
                            AtomicInteger completedTasks = new AtomicInteger(0);

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String courseID = documentSnapshot.getString("courseID");
                                FirebaseUtil.getDocumentById("course", courseID, CourseModel.class)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                course = task.getResult();

                                                if (course != null) {
                                                    myCourseList.add(course);
                                                }

                                                // Check if all tasks are completed
                                                if (completedTasks.incrementAndGet() == totalTasks) {
                                                    myCourseAdapter.setCourseList(myCourseList);
                                                    recyclerView.setAdapter(myCourseAdapter);
                                                }
                                            }
                                        });
                            }
                            myCourseAdapter.setCourseList(myCourseList);
                            recyclerView.setAdapter(myCourseAdapter);
                        } else {
                            AndroidUtil.showToast(HomePage.this, "onEvent: queryDocumentSnapshots is null");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                navigation.handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    public void goToCareer(View view) {
        Intent intent = new Intent(this, CareerMain.class);
        startActivity(intent);
    }

    public void goToCommunity(View view) {
        startActivity(new Intent(HomePage.this, SocialActivity.class));
    }
}
