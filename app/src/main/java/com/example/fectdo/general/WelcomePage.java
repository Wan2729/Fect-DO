package com.example.fectdo.general;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fectdo.R;
import com.example.fectdo.course.Enroll;
import com.example.fectdo.utils.FirebaseUtil;

public class WelcomePage extends AppCompatActivity {
    Button signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(new Intent(WelcomePage.this, Enroll.class));
                } else {
                    startActivity(new Intent(WelcomePage.this, SignUpPhoneNumber.class));
                }
                finish();
            }
        }, 1100);
    }

//    public void goToLogInPage() {
//        Intent intent = new Intent(WelcomePage.this, LoginPage.class);
//        WelcomePage.this.startActivity(intent);
//    }
//
//    public void goToSignUpPage() {
//        Intent intent = new Intent(this, SignUpPhoneNumber.class);
//        WelcomePage.this.startActivity(intent);
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.loginButton) {
//            goToLogInPage();
//        } else if (view.getId() == R.id.signupButton) {
//            goToSignUpPage();
//        }
//    }
}