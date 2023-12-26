package com.example.fectdo.course.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.course.Adapter.CourseListManagerAdapter;
import com.example.fectdo.course.Fragment.AddCourse;
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

public class ManageCoursePage extends AppCompatActivity {
    CollectionReference courseCollectionRef;
    CourseModel course;
    String userID;

    CourseListManagerAdapter courseAdapter;
    RecyclerView recyclerView;
    List<CourseModel> courseList;

    Button addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_course_list);
        userID = getIntent().getStringExtra("USER_ID").toString();
        courseCollectionRef = FirebaseFirestore.getInstance().collection(getIntent().getStringExtra("COURSE_COLLECTION_REFERENCE"));

        recyclerView = findViewById(R.id.rvCourseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        courseAdapter = new CourseListManagerAdapter(getApplicationContext());

        addCourse = findViewById(R.id.btnAddCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse.setEnabled(false);
                addCourse.setVisibility(View.INVISIBLE);
                startFragment(AddCourse.newInstance(courseCollectionRef.getPath(), userID, true));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        courseCollectionRef.whereEqualTo("creatorID", userID)
                .addSnapshotListener(ManageCoursePage.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(ManageCoursePage.this, error.toString());
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
                    AndroidUtil.showToast(ManageCoursePage.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });
    }

    private void startFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            addCourse.setEnabled(true);
            addCourse.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}