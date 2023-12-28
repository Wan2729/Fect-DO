package com.example.fectdo.general;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fectdo.R;
import com.example.fectdo.activity.HomePage;
import com.example.fectdo.utils.FirebaseUtil;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(new Intent(WelcomePage.this, HomePage.class));
                } else {
                    startActivity(new Intent(WelcomePage.this, LoginEmailPassword.class));
                }
                finish();
            }
        }, 1100);
    }
}