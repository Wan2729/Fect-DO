package com.example.fectdo;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
    }

    public void goToLogInPage(){
        Intent intent = new Intent(WelcomePage.this, LoginPage.class);
        WelcomePage.this.startActivity(intent);
    }

    public void goToSignUpPage(){
        Intent intent = new Intent(this, SignUpPage.class);
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