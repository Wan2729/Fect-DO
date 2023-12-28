package com.example.fectdo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fectdo.R;
import com.example.fectdo.utils.Navigation;

public class ExamMainPage extends AppCompatActivity implements View.OnClickListener {

    Button math;
    private Navigation navigation;
    boolean dahJawab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_main_page);

        math = findViewById(R.id.btnMath);
        math.setOnClickListener(this);

        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == math.getId()){
            Intent intent = new Intent(ExamMainPage.this, Exam.class);
            startActivity(intent);
        }
    }

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
        Intent intent = new Intent(ExamMainPage.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}