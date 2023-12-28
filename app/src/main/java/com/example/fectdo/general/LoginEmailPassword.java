package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.activity.HomePage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailPassword extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    Button logInButton;
    FirebaseAuth mAuth;
    TextView gotoSignUpBtn;
    TextView gotoForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_password);

        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        logInButton = findViewById(R.id.logInBtn);
        gotoSignUpBtn = findViewById(R.id.goToSignUpTV);
        gotoForgotPassword = findViewById(R.id.goToForgotPassword);

        mAuth = FirebaseAuth.getInstance();

        gotoSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailPassword.this,SignUpUsernameEmailPassword.class));
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(email.isEmpty()){
                    emailInput.setError("Please input your Email");
                    return;
                }
                if(password.length()<8){
                    passwordInput.setError("Password must be at least 8 characters long");
                    return;
                }

                logIn(email,password,mAuth);
            }
        });

        gotoForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToForgotPassword();
            }
        });
    }

    void logIn(String email,String password,FirebaseAuth mAuth) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LoginEmailPassword.this, HomePage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginEmailPassword.this, "Login Failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    void goToForgotPassword(){
        startActivity(new Intent(LoginEmailPassword.this, ForgotPassword.class));
    }
}