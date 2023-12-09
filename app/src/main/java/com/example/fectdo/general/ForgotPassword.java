package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fectdo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button forgotPasswordBtn;
    EditText emailInput;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        emailInput = findViewById(R.id.emailInput);

        mAuth = FirebaseAuth.getInstance();

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString();

                if (email.isEmpty()) {
                    emailInput.setError("Please put an email.");
                    return;
                }

                forgotPassword(email);
            }
        });
    }

    void forgotPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPassword.this, "Password Send to Email.", Toast.LENGTH_SHORT).show();
                        changeForgotPasswordToLoginBtnAfter2Seconds();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Reset password failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void changeForgotPasswordToLoginBtnAfter2Seconds(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                forgotPasswordBtn.setText("Go To Login Page.");
                forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ForgotPassword.this,LoginEmailPassword.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }
        },2000);
    }
}