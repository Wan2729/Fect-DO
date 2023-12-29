package com.example.fectdo.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fectdo.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainFeedActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    MyPostFragment myPostFragment;
    UploadPostFragment uploadPostFragment;
    PostFeedFragment postFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        myPostFragment = new MyPostFragment();
        uploadPostFragment = new UploadPostFragment();
        postFeedFragment = new PostFeedFragment();
        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_post){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragment,postFeedFragment).commit();
                }
                if(item.getItemId()==R.id.menu_upload){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragment,uploadPostFragment).commit();
                }
                if (item.getItemId()==R.id.menu_my_post){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutFragment,myPostFragment).commit();
                }
                return true;
            }
        });

        bottomNav.setSelectedItemId(R.id.menu_my_post);
    }
}