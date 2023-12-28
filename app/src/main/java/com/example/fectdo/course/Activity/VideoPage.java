package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
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

import com.example.fectdo.R;
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

TextView title;
WebView webView;
LinearLayout topicLayout;

private Navigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);

        final Button Examchem = findViewById(R.id.ExamButtonChem);

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
//        loadInitialImage();


    }

    @Override
    protected void onStart() {
        super.onStart();

        courseDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CourseModel course = documentSnapshot.toObject(CourseModel.class);
                title.setText(course.getCourseName());

                if(course.getTopics() != null){
                    for(String topicID : course.getTopics()){
                        getTopic(topicID);
                    }

                    for(TopicModel topicDetails : topicList){
                        loadTopics(topicDetails);
                    }
                }
                else{
                    showToast("There is no topic in this course");
                }
            }
        });
    }

    void getTopic(String topicID){
        DocumentReference topicRef = FirebaseFirestore.getInstance().collection("topics").document(topicID);
        topicLayout.removeAllViews();

        topicRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        topicList.add(documentSnapshot.toObject(TopicModel.class));
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

    void loadTopics(TopicModel topicDetails){
        View view = getLayoutInflater().inflate(R.layout.topic_list_card, null);

        Button button = view.findViewById(R.id.btnTopicName);
        button.setText(topicDetails.getTopicName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideo(topicDetails.getVideoLink());
            }
        });

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
}