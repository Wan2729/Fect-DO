package com.example.fectdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExamMainPage extends AppCompatActivity implements View.OnClickListener {

    Button math;
    boolean dahJawab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_main_page);

        math = findViewById(R.id.btnMath);
        math.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == math.getId()){
            Intent intent = new Intent(ExamMainPage.this, Exam.class);
            startActivity(intent);
        }
    }
}