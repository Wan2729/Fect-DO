package com.example.fectdo.course;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class CreatedCourseList extends AppCompatActivity {
    CollectionReference courseCollectionRef;
    DocumentReference userReference;
    CourseModel course;
    LinearLayout courseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        courseLayout = findViewById(R.id.courseListLayout);
        userReference = FirebaseUtil.currentUserDetails();

    }

    @Override
    protected void onStart() {
        super.onStart();

        courseLayout.removeAllViews();
        courseCollectionRef.addSnapshotListener(CreatedCourseList.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    AndroidUtil.showToast(CreatedCourseList.this, error.toString());
                    return;
                }

                if(queryDocumentSnapshots != null){
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        course = documentSnapshot.toObject(CourseModel.class);

                        addCard();
                    }
                }
                else{
                    AndroidUtil.showToast(CreatedCourseList.this, "onEvent: queryDocumentSnapshots is null");
                }
            }
        });
    }

    void addCard(){
        View courseCardView = getLayoutInflater().inflate(R.layout.course_list_edit_card, null);

        TextView courseName = courseCardView.findViewById(R.id.tvCourseName);
        courseName.setText(course.getCourseName());

        ImageView icon = courseCardView.findViewById(R.id.ivCourseIcon);

        Button courseButton = courseCardView.findViewById(R.id.btnEdit);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseButton.setText("Enrolled");
                courseButton.setEnabled(false);

            }
        });

        courseLayout.addView(courseCardView);
    }
}