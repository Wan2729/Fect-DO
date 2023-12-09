package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.example.fectdo.R;
import com.example.fectdo.utils.FirebaseUtil;

public class VideoChemPage extends AppCompatActivity {
String video;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadInitialImage();

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
        Intent intent = new Intent(VideoChemPage.this, Enroll.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void chapter1ChemClick(View view){
        WebView webView = findViewById(R.id.videoViewChem);
        video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/qRa74ODgUlA?si=SVG7rW67cA7X4RKj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter2ChemClick(View view){
        WebView webView = findViewById(R.id.videoViewChem);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/2kMVWoxcp38?si=E1rrT80JP0qJ4lpq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter3ChemClick(View view){
        WebView webView = findViewById(R.id.videoViewChem);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/o6gJ0f10oVE?si=2p3GUShGVnLMuokH\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }


}