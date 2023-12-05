package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fectdo.R;
import com.example.fectdo.Soalan.PengurusSoalan;

public class Exam extends AppCompatActivity implements View.OnClickListener {
    PengurusSoalan senaraiSoalan;
    TextView ruanganSoalan;
    Button jawapanA, jawapanB, jawapanC, jawapanD, seterusnya;
    int soalanSemasa, markah;
    String pilihanJawapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        senaraiSoalan = new PengurusSoalan();
        senaraiSoalan.setSoalanLalai();
        soalanSemasa = 0;
        markah = 0;

        ruanganSoalan = findViewById(R.id.soalan);
        jawapanA = findViewById(R.id.btnAnswerA);
        jawapanB = findViewById(R.id.btnAnswerB);
        jawapanC = findViewById(R.id.btnAnswerC);
        jawapanD = findViewById(R.id.btnAnswerD);
        seterusnya = findViewById(R.id.btnSubmit);

        jawapanA.setOnClickListener(this);
        jawapanB.setOnClickListener(this);
        jawapanC.setOnClickListener(this);
        jawapanD.setOnClickListener(this);
        seterusnya.setOnClickListener(this);

        loadNewQuestion();
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        jawapanA.setBackgroundColor(com.google.android.material.R.color.design_default_color_primary_variant);
        jawapanB.setBackgroundColor(com.google.android.material.R.color.design_default_color_primary_variant);
        jawapanC.setBackgroundColor(com.google.android.material.R.color.design_default_color_primary_variant);
        jawapanD.setBackgroundColor(com.google.android.material.R.color.design_default_color_primary_variant);

        Button btn = (Button) v;

        if(btn.getId() == seterusnya.getId()){
            if(pilihanJawapan.equals(senaraiSoalan.getJawapan(soalanSemasa)))
                markah++;

            if(soalanSemasa < senaraiSoalan.getJumlahSoalan()-1){
                soalanSemasa++;
                loadNewQuestion();
            }
            else
                finishQuiz();
        }
        else{
            pilihanJawapan = btn.getText().toString();
            btn.setBackgroundColor(Color.MAGENTA);
        }
    }

    private void loadNewQuestion(){
        ruanganSoalan.setText(senaraiSoalan.getSenaraiSoalan(soalanSemasa));
        jawapanA.setText(senaraiSoalan.getSenaraiJawapan(soalanSemasa)[0]);
        jawapanB.setText(senaraiSoalan.getSenaraiJawapan(soalanSemasa)[1]);
        jawapanC.setText(senaraiSoalan.getSenaraiJawapan(soalanSemasa)[2]);
        jawapanD.setText(senaraiSoalan.getSenaraiJawapan(soalanSemasa)[3]);
    }

    void finishQuiz(){
        new AlertDialog.Builder(this)
                .setTitle("مرکه اندا")
                .setMessage("مرکه اندا اياله " +markah +" درڤد " +senaraiSoalan.getJumlahSoalan())
                .setPositiveButton("سلساي", ((dialog, which) -> endQuiz()))
                .setCancelable(false)
                .show();
    }

    void endQuiz(){
        finish();
    }
}