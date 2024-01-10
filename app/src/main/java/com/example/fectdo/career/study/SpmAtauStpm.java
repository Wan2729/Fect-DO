package com.example.fectdo.career.study;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fectdo.R;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.utils.Extras;
import com.example.fectdo.utils.Navigation;

public class SpmAtauStpm extends AppCompatActivity {
    private Navigation navigation;
    private WebView mywebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spm_atau_stpm);

        navigation = new Navigation(this);
        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
        mywebView=(WebView) findViewById(R.id.webview);
        mywebView.setWebViewClient(new mywebClient());
        mywebView.loadUrl("https://bit.ly/41UW2xq");
        WebSettings webSettings=mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

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
        Intent intent = new Intent(SpmAtauStpm.this, CareerMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
