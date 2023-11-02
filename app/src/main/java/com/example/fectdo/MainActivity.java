package com.example.fectdo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ImageView signUpButton;
    ImageView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpButton = (ImageView)findViewById(R.id.signUpButton);
        loginButton = (ImageView) findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpPage();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogInPage();
            }
        });
    }

    public void goToLogInPage(){
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToSignUpPage(){
        Intent intent = new Intent(this, SignUpPage.class);
        MainActivity.this.startActivity(intent);
    }
}