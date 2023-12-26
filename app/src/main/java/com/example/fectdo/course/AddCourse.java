package com.example.fectdo.course;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fectdo.R;
public class AddCourse extends Fragment {
    EditText editTitle;
    Button doneButton;

    public AddCourse() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        editTitle = view.findViewById(R.id.etCourseTitle);

        doneButton = view.findViewById(R.id.btnDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}