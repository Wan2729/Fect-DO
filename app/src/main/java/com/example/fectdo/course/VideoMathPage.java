package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.R;

public class VideoMathPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_math_page);


        final Button Exammath = findViewById(R.id.ExamButtonMath);
        Exammath.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                LetsgoExam();
            }
        });
    }

    void LetsgoExam(){
        Intent ExamMath=new Intent(this, ExamMainPage.class);
        startActivity(ExamMath);
    }
}