package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fectdo.HomePage;
import com.example.fectdo.R;
import com.example.fectdo.Soalan.QuizManager;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.models.TopicModel;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.example.fectdo.utils.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VideoPage extends AppCompatActivity {
String videoLink;
List<TopicModel> topicList;
DocumentReference courseDetails;
Button quizButton, Examchem;

TextView title;
WebView webView;
LinearLayout topicLayout;
QuizManager quizManager;

private Navigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);

        Examchem = findViewById(R.id.examButton);
        quizButton = findViewById(R.id.quizButton);

        quizManager = new QuizManager();
        topicList = new ArrayList<>();
        title = findViewById(R.id.tvCourseTitle);
        topicLayout = findViewById(R.id.linearLayout);
        webView = findViewById(R.id.videoViewChem);
        String documentID = getIntent().getStringExtra("COURSE_DOCUMENT_REF");
        courseDetails = FirebaseUtil.getCollection("course").document(documentID);

        Examchem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
//                LetsgoExam();
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                gotoQuiz();
            }
        });
//        loadInitialImage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        disableQuizButton();

        courseDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CourseModel course = documentSnapshot.toObject(CourseModel.class);
                title.setText(course.getCourseName());

                topicLayout.removeAllViews();
                if(course.getTopics() != null){
                    for(String topicID : course.getTopics()){
                        loadTopic(topicID);
                    }
                }
                else{
                    showToast("There is no topic in this course");
                }
            }
        });
    }

    void loadTopic(String topicID){
        DocumentReference topicRef = FirebaseFirestore.getInstance().collection("topics").document(topicID);

        topicRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
//                        topicList.add(documentSnapshot.toObject(TopicModel.class));
                        addCard(documentSnapshot.toObject(TopicModel.class));
                    }
                    else{
                        showToast("There is no topic in this course");
                    }
                }
                else{
                    showToast("Failed with exception: " +task.getException());
                }
            }
        });
    }

    void gotoQuiz(){
        Intent intent = new Intent(getApplicationContext(), Exam.class);
//        intent.putExtra("questionList", quizManager.getQuestion().toArray());
//        intent.putExtra("correctAnswer", quizManager.getCorrectAnswer().toArray());
//        intent.putExtra("choiceA", quizManager.getChoiceA().toArray());
//        intent.putExtra("choiceB", quizManager.getChoiceB().toArray());
//        intent.putExtra("choiceC", quizManager.getChoiceC().toArray());
//        intent.putExtra("choiceD", quizManager.getChoiceD().toArray());
        intent.putStringArrayListExtra("questionList", (ArrayList<String>) quizManager.getQuestion());
        intent.putStringArrayListExtra("correctAnswer", (ArrayList<String>) quizManager.getCorrectAnswer());
        intent.putStringArrayListExtra("choiceA", (ArrayList<String>) quizManager.getChoiceA());
        intent.putStringArrayListExtra("choiceB", (ArrayList<String>) quizManager.getChoiceB());
        intent.putStringArrayListExtra("choiceC", (ArrayList<String>) quizManager.getChoiceC());
        intent.putStringArrayListExtra("choiceD", (ArrayList<String>) quizManager.getChoiceD());
        startActivity(intent);
    }

    void addCard(TopicModel topicDetails){
        View view = getLayoutInflater().inflate(R.layout.layout_topiclist_button, null);

        Button button = view.findViewById(R.id.btnTopicName);
        button.setText(topicDetails.getTopicName());
        Log.d("Test sini, delete nanti", topicDetails.getTopicName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableQuizButton();
                loadVideo(topicDetails.getVideoLink());
                quizManager.setTopicName(topicDetails.getTopicName());
                quizManager.setQuestion(topicDetails.getQuestion());
                quizManager.setCorrectAnswer(topicDetails.getCorrectAnswer());
                quizManager.setChoiceA(topicDetails.getChoiceA());
                quizManager.setChoiceB(topicDetails.getChoiceB());
                quizManager.setChoiceC(topicDetails.getChoiceC());
                quizManager.setChoiceD(topicDetails.getChoiceD());
            }
        });

//        topicLayout.removeAllViews();
        topicLayout.addView(view);
    }

    void loadVideo(String embedLink){
        videoLink = "<iframe width=\"100%\" height=\"100%\" src=\"" +embedLink +"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(videoLink,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    void LetsgoExam(){
        Intent ExamChem=new Intent(this, ExamMainPage.class);
        startActivity(ExamChem);
    }

    private void loadInitialImage() {
        WebView webView = findViewById(R.id.videoViewChem);

        String imageUrl = "https://img.freepik.com/free-vector/hand-drawn-colorful-science-education-background_23-2148489231.jpg?w=1380&t=st=1702145884~exp=1702146484~hmac=593b895309c4887dae9791a3b79bc134b247851749d94c12165bd2f5cf3abdbe";

        String imageHtml = "<html><body style=\"margin: 0; padding: 0;\"><img width=\"100%\" height=\"100%\" src=\"" + imageUrl + "\"></body></html>";

        webView.loadData(imageHtml, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
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
        Intent intent = new Intent(VideoPage.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void chapter1ChemClick(View view){
        WebView webView = findViewById(R.id.videoViewChem);
        videoLink = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/qRa74ODgUlA?si=SVG7rW67cA7X4RKj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(videoLink,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void showToast(String message){
        AndroidUtil.showToast(getApplicationContext(), message);
    }

    private void disableQuizButton(){
        quizButton.setEnabled(false);
        Examchem.setEnabled(false);
    }

    private void enableQuizButton(){
        quizButton.setEnabled(true);
        Examchem.setEnabled(true);
    }
}