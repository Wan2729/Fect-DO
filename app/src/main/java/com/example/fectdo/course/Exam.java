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
import com.example.fectdo.Soalan.Soalan;
import com.example.fectdo.models.QuestionModel;
import com.example.fectdo.utils.AndroidUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Exam extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore database;
    CollectionReference questions;
    QuestionModel questionModel;

    PengurusSoalan senaraiSoalan;
    TextView ruanganSoalan;
    Button jawapanA, jawapanB, jawapanC, jawapanD, nextBtn;
    int currentQuestion, marks;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        senaraiSoalan = new PengurusSoalan();
        senaraiSoalan.setSoalanLalai();

        database = FirebaseFirestore.getInstance();
        questions = database.collection("questions");

        currentQuestion = 0;
        marks = 0;

        ruanganSoalan = findViewById(R.id.soalan);
        jawapanA = findViewById(R.id.btnAnswerA);
        jawapanB = findViewById(R.id.btnAnswerB);
        jawapanC = findViewById(R.id.btnAnswerC);
        jawapanD = findViewById(R.id.btnAnswerD);
        nextBtn = findViewById(R.id.btnSubmit);

        jawapanA.setOnClickListener(this);
        jawapanB.setOnClickListener(this);
        jawapanC.setOnClickListener(this);
        jawapanD.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

//        loadQuestionFromFirebase();
        loadNewQuestion();
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        jawapanA.setBackgroundColor(getResources().getColor(R.color.yale_blue));
        jawapanB.setBackgroundColor(getResources().getColor(R.color.yale_blue));
        jawapanC.setBackgroundColor(getResources().getColor(R.color.yale_blue));
        jawapanD.setBackgroundColor(getResources().getColor(R.color.yale_blue));

        Button btn = (Button) v;

        if(btn.getId() == nextBtn.getId()){
            if(choice.equals(senaraiSoalan.getJawapan(currentQuestion)))
                marks++;

            if(currentQuestion < senaraiSoalan.getJumlahSoalan() - 1){
                currentQuestion++;
                loadNewQuestion();
            }
            else
                finishQuiz();
        }
        else{
            choice = btn.getText().toString();
            btn.setBackgroundColor(getResources().getColor(R.color.blue_soft));
        }
    }

    private void loadNewQuestion(){
//        String[] questionList = questionModel.getAnswerList(currentQuestion);

        ruanganSoalan.setText(senaraiSoalan.getSenaraiSoalan(currentQuestion));
        jawapanA.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[0]);
        jawapanB.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[1]);
        jawapanC.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[2]);
        jawapanD.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[3]);
    }

    void loadQuestionFromFirebase(){
        questions.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {String m="";
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    questionModel = documentSnapshot.toObject(QuestionModel.class);
                    m = documentSnapshot.get("totalQuestions").toString();
                }
                AndroidUtil.showToast(Exam.this, m);
            }
        });

    }

    void finishQuiz(){
        new AlertDialog.Builder(this)
                .setTitle("Markah anda")
                .setMessage("Tahniah anda berjaya mendapat " + marks +" daripada " +senaraiSoalan.getJumlahSoalan())
                .setPositiveButton("Selesai", ((dialog, which) -> endQuiz()))
                .setCancelable(false)
                .show();
    }

    void endQuiz(){
        finish();
    }
}