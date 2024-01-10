package com.example.fectdo.career.job_career;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;

import com.example.fectdo.R;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.career.scholarship.Scolarhship;
import com.example.fectdo.utils.Navigation;

public class JobActivity extends AppCompatActivity {
    private Navigation navigation;
    private WebView mywebView;
    private Spinner spinner;
    private Button btnSubmitJob;
    private String link = "res/drawable/wallpaper_job.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        navigation = new Navigation(this);
        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
        mywebView=(WebView) findViewById(R.id.jobWeb);
        spinner = findViewById(R.id.spinner);
        btnSubmitJob = findViewById(R.id.btnSubmitJob);

        WebSettings webSettings = mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load initial image using loadDataWithBaseURL
        String initialHtml = "<html><head><style>body {margin: 0; padding: 0;} img {width: 100%; height: auto;} </style></head><body><img src=\"file:///android_res/drawable/wallpaper_job.jpg\"/></body></html>";
        mywebView.loadDataWithBaseURL("fake://not/needed", initialHtml, "text/html", "utf-8", null);

        mywebView.setWebViewClient(new mywebClient());
    }

    public void changeWebsite(View view){
        if(spinner.getSelectedItem() != null){
            switch (spinner.getSelectedItem().toString()){
                case "Job Street":link="https://www.jobstreet.com.my/jobs";
                    break;
                case "Jora":link="https://my.jora.com/";
                    break;
                case "MyFutureJobs":link="https://candidates.myfuturejobs.gov.my/search-jobs/description?jobId=98691089b4e0417a8fc2f33f8987db51";
                    break;
                case "Indeed":link="https://malaysia.indeed.com/";
                    break;

            }
        }

        mywebView.setWebViewClient(new mywebClient());
        mywebView.loadUrl(link);

    }

    public class mywebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
    @Override
    public void onBackPressed() {
        if (mywebView.canGoBack()) {
            mywebView.goBack();
        } else {
            super.onBackPressed();
        }
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
        Intent intent = new Intent(JobActivity.this, CareerMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}