package com.example.fectdo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Enroll_Physics extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_physics);


        ListView listview = findViewById(R.id.ListViewPhysics);
        String choiceList [] = {"Chapter 1","Chapter 2","Chapter 3","Chapter 4","Chapter 5","Chapter 6",};


        ArrayAdapter adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,choiceList
        );

        listview.setAdapter(adapter);
    }



}