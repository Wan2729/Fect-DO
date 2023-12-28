package com.example.fectdo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.example.fectdo.TAG.TAG;
import com.example.fectdo.adapter.MyCourseAdapter;
import com.example.fectdo.course.UploadActivity;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.R;
import com.example.fectdo.social.MainFeedActivity;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.example.fectdo.utils.Navigation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    final String COURSE_KEY = "course";
    ImageView uploadButton;
    Button searchButton, manageCourse;

    //Database
    CollectionReference courseCollectionRef;
    DocumentReference userDocumentRef;
    CourseModel course;

    //For recycler view
    RecyclerView recyclerView;
    MyCourseAdapter myCourseAdapter;
    LinearLayoutManager layoutManager;
    List<CourseModel> myCourseList;
    ImageView socialButton;

    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        courseCollectionRef = FirebaseFirestore.getInstance().collection(COURSE_KEY);
        userDocumentRef = FirebaseUtil.currentUserDetails();

        uploadButton = findViewById(R.id.btnUpload);
        socialButton = findViewById(R.id.btnSosial);

        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), AddCourse.class);
                intent.putExtra(TAG.COURSE_PATH, courseCollectionRef.getPath());
                intent.putExtra(TAG.USER_ID, FirebaseUtil.currentUserId());
                intent.putExtra(TAG.NEW_COURSE, true);
                startActivity(intent);
            }
        });


        //setup recyclerView
        recyclerView = findViewById(R.id.rvMyCourse);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        myCourseAdapter = new MyCourseAdapter(getApplicationContext());

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

        socialButton.setOnClickListener(v->{
            Intent intent = new Intent(HomePage.this, MainFeedActivity.class);
            startActivity(intent);
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        courseCollectionRef.addSnapshotListener(HomePage.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(HomePage.this, error.toString());
                    return;
                }

                if(queryDocumentSnapshots != null){
                    myCourseList = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        course = documentSnapshot.toObject(CourseModel.class);
                        course.setDocumentID(documentSnapshot.getId());

                        myCourseList.add(course);
                    }
                    myCourseAdapter.setCourseList(myCourseList);
                    recyclerView.setAdapter(myCourseAdapter);
                }
                else{
                    AndroidUtil.showToast(HomePage.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });

    }

    void letupload(){
        Intent upload=new Intent(this, UploadActivity.class);
        startActivity(upload);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                navigation.handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    void gotoCourse(){

    }

    public void goToCareer(View view){
        Intent intent = new Intent(this, CareerMain.class);
        startActivity(intent);
    }
}