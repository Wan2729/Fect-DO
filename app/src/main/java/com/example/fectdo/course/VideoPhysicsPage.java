package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.R;

public class VideoPhysicsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_physics_page);



        final Button Examphy = findViewById(R.id.ExamButtonPhy);
        Examphy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                LetsgoExam();
            }
        });
    }

    void LetsgoExam(){
        Intent ExamPhy=new Intent(this, ExamMainPage.class);
        startActivity(ExamPhy);
    }
}