package com.example.fectdo.course.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fectdo.R;
import com.example.fectdo.Soalan.PengurusSoalan;
import com.example.fectdo.Soalan.QuizManager;
import com.example.fectdo.models.QuestionModel;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.Navigation;
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
    QuizManager quizManager;
    TextView ruanganSoalan;
    Button jawapanA, jawapanB, jawapanC, jawapanD, nextBtn;
    int currentQuestion, marks;
    String choice;
    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        senaraiSoalan = new PengurusSoalan();
        senaraiSoalan.setSoalanLalai();

        database = FirebaseFirestore.getInstance();
        questions = database.collection("questions");

        quizManager = new QuizManager();
        quizManager.setQuestion((List<String>) getIntent().getStringArrayListExtra("questionList"));
        quizManager.setCorrectAnswer((List<String>) getIntent().getStringArrayListExtra("correctAnswer"));
        quizManager.setChoiceA((List<String>) getIntent().getStringArrayListExtra("choiceA"));
        quizManager.setChoiceB((List<String>) getIntent().getStringArrayListExtra("choiceB"));
        quizManager.setChoiceC((List<String>) getIntent().getStringArrayListExtra("choiceC"));
        quizManager.setChoiceD((List<String>) getIntent().getStringArrayListExtra("choiceD"));

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
        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        jawapanA.setEnabled(true);
        jawapanB.setEnabled(true);
        jawapanC.setEnabled(true);
        jawapanD.setEnabled(true);

        Button btn = (Button) v;

        if(btn.getId() == nextBtn.getId()){
//            if(choice.equals(senaraiSoalan.getJawapan(currentQuestion)))
//                marks++;
            if(choice.equals(quizManager.getCorrectAnswer(currentQuestion)))
                marks++;

            if(currentQuestion < quizManager.getSize() - 1){
                currentQuestion++;
                loadNewQuestion();
            }
            else
                finishQuiz();
        }
        else{
            choice = btn.getText().toString();
            btn.setEnabled(false);
        }

    }

    private void loadNewQuestion(){
//        String[] questionList = questionModel.getAnswerList(currentQuestion);

//        ruanganSoalan.setText(senaraiSoalan.getSenaraiSoalan(currentQuestion));
//        jawapanA.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[0]);
//        jawapanB.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[1]);
//        jawapanC.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[2]);
//        jawapanD.setText(senaraiSoalan.getSenaraiJawapan(currentQuestion)[3]);

        ruanganSoalan.setText(quizManager.getQuestion(currentQuestion));
        jawapanA.setText(quizManager.getChoiceA().get(currentQuestion));
        jawapanB.setText(quizManager.getChoiceB().get(currentQuestion));
        jawapanC.setText(quizManager.getChoiceC().get(currentQuestion));
        jawapanD.setText(quizManager.getChoiceD().get(currentQuestion));
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
                .setMessage("Tahniah anda berjaya mendapat " + marks +" daripada " +quizManager.getSize())
                .setPositiveButton("Selesai", ((dialog, which) -> endQuiz()))
                .setCancelable(false)
                .show();
    }

    void endQuiz(){
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleLogout() {
        Intent intent = new Intent(Exam.this, ExamMainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}