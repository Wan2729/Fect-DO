package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.R;

public class VideoChemPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_chem_page);

        final Button Examchem = findViewById(R.id.ExamButtonChem);
        Examchem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                LetsgoExam();
            }
        });
    }

    void LetsgoExam(){
        Intent ExamChem=new Intent(this, ExamMainPage.class);
        startActivity(ExamChem);
    }
}