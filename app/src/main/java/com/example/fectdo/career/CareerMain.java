package com.example.fectdo.career;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.fectdo.R;
import com.example.fectdo.HomePage;
import com.example.fectdo.career.job_career.JobActivity;
import com.example.fectdo.career.scholarship.Scolarhship;
import com.example.fectdo.career.study.SpmAtauStpm;
import com.example.fectdo.utils.Navigation;

public class CareerMain extends AppCompatActivity {
    private Navigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_main);

        navigation = new Navigation(this);

        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);
    }
    public void goToScholarship(View view){
        Intent intent = new Intent(CareerMain.this, Scolarhship.class);
        startActivity(intent);
    }

    public void goToStudy(View view){
        Intent intent = new Intent(CareerMain.this, SpmAtauStpm.class);
        startActivity(intent);

    }

    public void goToJob(View view){
        Intent intent = new Intent(CareerMain.this, JobActivity.class);
        startActivity(intent);

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
        Intent intent = new Intent(CareerMain.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}