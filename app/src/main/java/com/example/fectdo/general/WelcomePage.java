package com.example.fectdo.general;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fectdo.R;
import com.example.fectdo.course.Enroll;
import com.example.fectdo.utils.FirebaseUtil;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener {
    Button signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        signUpButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        if(FirebaseUtil.isLoggedIn()){
            startActivity(new Intent(WelcomePage.this, Enroll.class));
        }else {
            startActivity(new Intent(WelcomePage.this,SignUpPhoneNumber.class));
        }
        finish();
    }

    public void goToLogInPage(){
        Intent intent = new Intent(WelcomePage.this, LoginPage.class);
        WelcomePage.this.startActivity(intent);
    }

    public void goToSignUpPage(){
        Intent intent = new Intent(this, SignUpPhoneNumber.class);
        WelcomePage.this.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginButton){
            goToLogInPage();
        }
        else if(view.getId() == R.id.signupButton){
            goToSignUpPage();
        }
    }
}