package com.example.fectdo.course.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.fectdo.TAG.TAG;
import com.example.fectdo.adapter.CourseListAdapter;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
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
    SearchView searchView;

    CourseListAdapter courseAdapter;
    RecyclerView recyclerView;
    List<CourseModel> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        Intent intent = getIntent();
        courseCollectionRef = FirebaseUtil.getCollection("course");
        userID = getIntent().getStringExtra("USER_ID").toString();

        recyclerView = findViewById(R.id.rvCourseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        courseAdapter = new CourseListAdapter(getApplicationContext());

        searchView = findViewById(R.id.svSearchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(newText);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadData("");
    }

    void loadData(String textTyped){
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
                                course.setDocumentID(documentSnapshot.getId());

                                int textTypeLength = textTyped.length();
                                if(course.getCourseName().toLowerCase().substring(0, textTypeLength)
                                        .equals(textTyped.toLowerCase())){
                                    courseList.add(course);
                                }
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