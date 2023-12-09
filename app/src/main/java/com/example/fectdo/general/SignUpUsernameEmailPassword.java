package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.course.Enroll;
import com.example.fectdo.models.UserModel;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SignUpUsernameEmailPassword extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailInput;
    EditText passwordInput;
    EditText reEnterPasswordInput;
    Button signUpBtn;
    EditText usernameInput;
    UserModel userModel;
    CollectionReference userDatabase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_username_email_password);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        reEnterPasswordInput = findViewById(R.id.reEnterPasswordInput);
        signUpBtn = findViewById(R.id.signUpBtn);
        usernameInput = findViewById(R.id.usernameInput);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseUtil.getCollection("users");

        signUpBtn.setOnClickListener((v) -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String reEnterPassword = reEnterPasswordInput.getText().toString();

            if (username.length() < 8) {
                usernameInput.setError("Username must be at least 8 characters long.");
                return;
            }

            if (username.contains(" ")) {
                usernameInput.setError("Username cannot contains space.");
                return;
            }

            if (email.isEmpty()) {
                emailInput.setError("Please put an Email.");
                return;
            }

            if (password.length() < 8) {
                passwordInput.setError("Password must be at least 8 characters long.");
                return;
            }

            if (!reEnterPassword.equals(password)) {
                reEnterPasswordInput.setError("Password is not the same.");
                return;
            }

            signUpBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            usernameIsTaken(username,isTaken -> {
                if (isTaken){
                    signUpBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    usernameInput.setError("This username is not available");
                } else {
                    signUp(username, email, password, mAuth);
                }
            });
        });
    }

    void signUp(String username, String email, String password, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_LONG).show();



                if(userModel!=null){
                    userModel.setUsername(username);
                } else {
                    userModel = new UserModel(null,username, Timestamp.now(),email);
                }

                FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignUpUsernameEmailPassword.this, Enroll.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    interface UsernameCheckCallback {
        void onUsernameChecked(boolean isTaken);
    }

    void usernameIsTaken(String username, UsernameCheckCallback callback) {
        userDatabase.whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    boolean isTaken = false;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                isTaken = true;
                                break;
                            }
                        }
                    } else {
                        Log.d("SignUP", "Error getting documents: ", task.getException());
                    }
                    callback.onUsernameChecked(isTaken);
                });
    }
}