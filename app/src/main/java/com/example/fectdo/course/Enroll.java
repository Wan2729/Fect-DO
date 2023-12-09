package com.example.fectdo.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fectdo.R;
import com.example.fectdo.general.LoginEmailPassword;
import com.example.fectdo.general.SignUpPhoneNumber;
import com.example.fectdo.general.SignUpUsernameEmailPassword;
import com.example.fectdo.general.SignUpUserrname;
import com.example.fectdo.utils.FirebaseUtil;

public class Enroll extends AppCompatActivity {

    ImageView logoutBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleLogout() {
        FirebaseUtil.logOut();
        Intent intent = new Intent(Enroll.this, LoginEmailPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    void nextpagephy(){
        Intent Physic=new Intent(this, VideoPhysicsPage.class);
        startActivity(Physic);
    }
    void nextpagechem(){
        Intent Chemistry=new Intent(this, VideoChemPage.class);
        startActivity(Chemistry);
    }
    void nextpagemath(){
        Intent Mathematic=new Intent(this, VideoMathPage.class);
        startActivity(Mathematic);
    }
    void letupload(){
        Intent upload=new Intent(this, UploadActivity.class);
        startActivity(upload);
    }
}