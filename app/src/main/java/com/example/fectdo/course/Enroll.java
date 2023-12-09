package com.example.fectdo.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fectdo.CourseDataBase.Course;
import com.example.fectdo.R;
import com.example.fectdo.general.LoginEmailPassword;
import com.example.fectdo.general.SignUpPhoneNumber;
import com.example.fectdo.general.SignUpUsernameEmailPassword;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Enroll extends AppCompatActivity {

    Course course;
    ImageView logoutBtn;
    Button dummyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        logoutBtn = findViewById(R.id.logoutBtn);
        dummyButton = findViewById(R.id.dummyButton);

        dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.logOut();
                Intent intent = new Intent(Enroll.this, LoginEmailPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


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

    void fetchData(){
        DocumentReference coursesDocument = FirebaseFirestore.getInstance().collection("courses").document("F57w7xjaXULZ5kmoK2XU");

        coursesDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    course = task.getResult().toObject(Course.class);
                    Toast.makeText(Enroll.this,course.getCourse_name(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void addCourse(){
        Course testCourse = new Course("Azim");
        FirebaseFirestore.getInstance().collection("courses").add(testCourse).addOnCompleteListener((task -> {
            if(task.isSuccessful()){
                AndroidUtil.showToast(this,"Success");
            }
        }));
    }
}