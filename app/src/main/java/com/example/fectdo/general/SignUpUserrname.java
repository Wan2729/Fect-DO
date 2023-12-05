package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fectdo.R;
import com.example.fectdo.course.Enroll;
import com.example.fectdo.course.Exam;
import com.example.fectdo.models.UserModel;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class SignUpUserrname extends AppCompatActivity {

    EditText username;
    Button letMiInBtn;
    String phoneNUmber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_userrname);

        username = findViewById(R.id.username);
        letMiInBtn = findViewById(R.id.letMiInBtn);

        phoneNUmber = getIntent().getExtras().getString("phone");
        getUsername();

        letMiInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername();
            }
        });
    }

    void setUsername(){
        String username = this.username.getText().toString();
        if(username.isEmpty() || username.length()<5){
            this.username.setError("Username must be at least 5 characters long.");
            return;
        }

        if(userModel!=null){
            userModel.setUsername(username);
        } else {
            userModel = new UserModel(phoneNUmber,username, Timestamp.now());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignUpUserrname.this, Enroll.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    void getUsername() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    userModel = task.getResult().toObject(UserModel.class);
                    if(userModel!=null){
                        username.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
}