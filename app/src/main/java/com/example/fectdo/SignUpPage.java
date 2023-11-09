package com.example.fectdo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        signUp = (Button) findViewById(R.id.logIn);
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