package com.example.fectdo.course;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fectdo.CourseDataBase.CourseModel;
import com.example.fectdo.R;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageCourse extends AppCompatActivity {
    CollectionReference courseCollectionRef;
    DocumentReference userReference;
    CourseModel course;
    String userID;

    CourseListManagerAdapter courseAdapter;
    RecyclerView recyclerView;
    List<CourseModel> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);
        userID = getIntent().getStringExtra("USER_ID").toString();
        courseCollectionRef = FirebaseFirestore.getInstance().collection(getIntent().getStringExtra("COURSE_COLLECTION_REFERENCE"));

        recyclerView = findViewById(R.id.rvCourseListManage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        courseAdapter = new CourseListManagerAdapter(getApplicationContext());

        userReference = FirebaseUtil.currentUserDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();

        courseCollectionRef.whereEqualTo("creatorID", userID)
                .addSnapshotListener(ManageCourse.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(ManageCourse.this, error.toString());
                    return;
                }

                if(queryDocumentSnapshots != null){
                    courseList = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        course = documentSnapshot.toObject(CourseModel.class);

                        courseList.add(course);
                    }
                    courseAdapter.setCourseList(courseList);
                    recyclerView.setAdapter(courseAdapter);
                }
                else{
                    AndroidUtil.showToast(ManageCourse.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });
    }

    void addCourse(){

    }
}