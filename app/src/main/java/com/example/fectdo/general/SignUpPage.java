package com.example.fectdo.general;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fectdo.R;

public class SignUpPage extends AppCompatActivity {

    EditText username,password,confirmPassword,phoneNumber,emailAddress;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        username = (EditText) findViewById(R.id.usernameSignUpPage);
        password = (EditText) findViewById(R.id.passwordSignUpPage);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        phoneNumber = (EditText) findViewById(R.id.username);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        signUp = (Button) findViewById(R.id.signupBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySignUp();
            }
        });
    }

    public void trySignUp(){
    }
}