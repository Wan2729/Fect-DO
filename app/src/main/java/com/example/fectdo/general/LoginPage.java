package com.example.fectdo.general;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fectdo.Enroll;
import com.example.fectdo.R;
import com.example.fectdo.course.ExamMainPage;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    Button logIn;
    EditText usernameLoginPage, passwordLogInPage;

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginBtn){
            tryLogIn();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        logIn = findViewById(R.id.loginBtn);
        usernameLoginPage = findViewById(R.id.usernameET);
        passwordLogInPage = findViewById(R.id.passwordET);

        logIn.setOnClickListener(this);
    }

    public void tryLogIn(){
        //check dah ada account ke belum
        boolean canLogin = false;

        if(canLogin){
            //go to next page
        }
        else{
            //kalu tkde account, yo akan kluarkan mesej ni
            Toast.makeText(this, "You email/username does not exist", Toast.LENGTH_LONG);
        }
    }

    public void goToNextPage(View view){
        Intent intent = new Intent(this, Enroll.class);
        startActivity(intent);
    }
}