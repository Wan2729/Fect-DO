package com.example.fectdo.course.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.fectdo.course.Adapter.CourseListAdapter;
import com.example.fectdo.course.Adapter.CourseListManagerAdapter;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;
import com.example.fectdo.utils.AndroidUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CourseListPage extends AppCompatActivity {
    CollectionReference courseCollectionRef;
    CourseModel course;
    String userID;

    CourseListAdapter courseAdapter;
    RecyclerView recyclerView;
    List<CourseModel> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Intent intent = getIntent();
        courseCollectionRef = FirebaseFirestore.getInstance().collection(intent.getStringExtra("COURSE_COLLECTION_REFERENCE"));
        userID = getIntent().getStringExtra("USER_ID").toString();

        recyclerView = findViewById(R.id.rvCourseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        courseAdapter = new CourseListAdapter(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        courseCollectionRef.whereNotEqualTo("creatorID", userID)
                .addSnapshotListener(CourseListPage.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(CourseListPage.this, error.toString());
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
                    AndroidUtil.showToast(CourseListPage.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });
    }
}