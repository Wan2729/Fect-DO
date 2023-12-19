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

public class VideoPhysicsPage extends AppCompatActivity {

    String video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_physics_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load an image initially
        loadInitialImage();

        final Button Examphy = findViewById(R.id.ExamButtonPhy);
        Examphy.setOnClickListener(view -> LetsgoExam());
    }

    private void loadInitialImage() {
        WebView webView = findViewById(R.id.videoViPhy);

        String imageUrl = "https://www.ukm.my/siswazahfst/wp-content/uploads/2023/03/Physics.jpg";

        String imageHtml = "<html><body style=\"margin: 0; padding: 0;\"><img width=\"100%\" height=\"100%\" src=\"" + imageUrl + "\"></body></html>";

        webView.loadData(imageHtml, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }


    void LetsgoExam(){
        Intent ExamPhy=new Intent(this, ExamMainPage.class);
        startActivity(ExamPhy);
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
        Intent intent = new Intent(VideoPhysicsPage.this, Enroll.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void chapter1Phy(View view){
        WebView webView = findViewById(R.id.videoViPhy);
        video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/F3ekgbhKsxA?si=9PbiQtCKjW4P20En\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter2Phy(View view){
        WebView webView = findViewById(R.id.videoViPhy);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/S8BpYidYlcI?si=95-8OfJJV3gCMkCo\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter3Phy(View view){
        WebView webView = findViewById(R.id.videoViPhy);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/dPIRktXWXUU?si=1SVC2ut0cpqPsBwu\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }
}