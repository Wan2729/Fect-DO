package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
QuizManager quizManager, examManager;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPage.this);
                builder.setTitle("Leaving page")
                        .setMessage("Are you confirm to save this change? \n" +
                                "You cannot edit this afterward")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            letsgoExam();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            return;
                        })
                        .setCancelable(true);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                gotoQuiz();
            }
        });
        loadInitialImage();
        disableQuizButton();
    }

    @Override
    protected void onStart() {
        super.onStart();

        examManager = new QuizManager();
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
                        TopicModel topic = documentSnapshot.toObject(TopicModel.class);
                        examManager.addQuestions(topic.getQuestion());
                        examManager.addCorrectAnswer(topic.getCorrectAnswer());
                        examManager.addChoiceA(topic.getChoiceA());
                        examManager.addChoiceB(topic.getChoiceB());
                        examManager.addChoiceC(topic.getChoiceC());
                        examManager.addChoiceD(topic.getChoiceD());
                        addCard(topic);
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
        intent.putStringArrayListExtra("questionList", (ArrayList<String>) quizManager.getQuestion());
        intent.putStringArrayListExtra("correctAnswer", (ArrayList<String>) quizManager.getCorrectAnswer());
        intent.putStringArrayListExtra("choiceA", (ArrayList<String>) quizManager.getChoiceA());
        intent.putStringArrayListExtra("choiceB", (ArrayList<String>) quizManager.getChoiceB());
        intent.putStringArrayListExtra("choiceC", (ArrayList<String>) quizManager.getChoiceC());
        intent.putStringArrayListExtra("choiceD", (ArrayList<String>) quizManager.getChoiceD());
        startActivity(intent);
    }

    void letsgoExam(){
        Intent intent=new Intent(this, Exam.class);
        intent.putStringArrayListExtra("questionList", (ArrayList<String>) examManager.getQuestion());
        intent.putStringArrayListExtra("correctAnswer", (ArrayList<String>) examManager.getCorrectAnswer());
        intent.putStringArrayListExtra("choiceA", (ArrayList<String>) examManager.getChoiceA());
        intent.putStringArrayListExtra("choiceB", (ArrayList<String>) examManager.getChoiceB());
        intent.putStringArrayListExtra("choiceC", (ArrayList<String>) examManager.getChoiceC());
        intent.putStringArrayListExtra("choiceD", (ArrayList<String>) examManager.getChoiceD());
        startActivity(intent);
    }

    void addCard(TopicModel topicDetails){
        View view = getLayoutInflater().inflate(R.layout.layout_topiclist_button, null);

        Button button = view.findViewById(R.id.btnTopicName);
        button.setText(topicDetails.getTopicName());
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

    private void loadInitialImage() {
        WebView webView = findViewById(R.id.videoViewChem);

        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/fect-do-174ff.appspot.com/o/images%2FScreenshot%202024-01-11%20031401.png?alt=media&token=4321b313-b7a2-46f4-b2d7-951bc5268f3c";

        // Load and display the image in WebView with fitxy behavior
        String imageHtml = "<html><head><style>img { max-width: 100%; height: auto; }</style></head><body style=\"margin: 0; padding: 0;\"><img width=\"100%\" height=\"100%\" src=\"" + imageUrl + "\"></body></html>";

        webView.loadDataWithBaseURL(null, imageHtml, "text/html", "UTF-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
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