package com.example.fectdo.course;

import androidx.annotation.NonNull;
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

import com.example.fectdo.course.Activity.ExamMainPage;
import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.edit.ProfileActivity;
import com.example.fectdo.R;
import com.example.fectdo.edit.SettingActivity;
import com.example.fectdo.utils.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VideoMathPage extends AppCompatActivity {
String video;
private Navigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_math_page);


        final Button Exammath = findViewById(R.id.ExamButtonMath);
        Exammath.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                LetsgoExam();
            }
        });
        loadInitialImage();

        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);


    }

    void LetsgoExam(){
        Intent ExamMath=new Intent(this, ExamMainPage.class);
        startActivity(ExamMath);
    }
    private void loadInitialImage() {
        WebView webView = findViewById(R.id.videoViewMath);

        String imageUrl = "https://m.media-amazon.com/images/I/71726tlwvUL._AC_SL1000_.jpg";

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
        Intent intent = new Intent(VideoMathPage.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void chapter1MathClick(View view){
        WebView webView = findViewById(R.id.videoViewMath);
        video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/2D6mtRpUyQs?si=LDwSv9AsS3LQtApG\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter2MathClick(View view){
        WebView webView = findViewById(R.id.videoViewMath);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-8ha8zemljc?si=umf-VTGOEN8wCqJH\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void chapter3MathClick(View view){
        WebView webView = findViewById(R.id.videoViewMath);
        video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/x7oyHEc9Ba8?si=_K2ylQcZLLnJfaII\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video,"text/html","utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }
}