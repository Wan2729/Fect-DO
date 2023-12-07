package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fectdo.R;
import com.example.fectdo.general.SignUpPhoneNumber;
import com.example.fectdo.general.SignUpUserrname;
import com.example.fectdo.utils.FirebaseUtil;

public class Enroll extends AppCompatActivity {

    ImageView logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        logoutBtn = findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.logOut();
                Intent intent = new Intent(Enroll.this, SignUpPhoneNumber.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        final ImageView NextpagePhysics = findViewById(R.id.btnPhysics);
        final ImageView NextpageChemistry = findViewById(R.id.btnChem);
        final ImageView NextpageMathematic = findViewById(R.id.btnMath);
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
}