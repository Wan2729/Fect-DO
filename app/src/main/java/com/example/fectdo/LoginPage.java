package com.example.fectdo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LoginPage extends AppCompatActivity {

   Button logIn;
    EditText usernameLoginPage;
    EditText passwordLogInPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        logIn = (Button) findViewById(R.id.buttonLogin);
        usernameLoginPage = (EditText) findViewById(R.id.etUsername);
        passwordLogInPage = (EditText) findViewById(R.id.etPassword);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogIn();
            }
        });
    }

    public void tryLogIn(){

    }
}