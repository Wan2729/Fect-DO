package com.example.fectdo.course.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fectdo.R;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.utils.AndroidUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCourse extends Fragment {
    final static String COURSE_PATH = "COURSE_PATH_REF", USER_ID = "USER_DOC_PATH_REF", NEW_COURSE = "NEW_COURSE";
    CollectionReference courseCollectionReference;
    EditText editTitle;
    Button doneButton, cancelButton;

    public static AddCourse newInstance(String coursePath, String userID, boolean newCourse){
        AddCourse fragment = new AddCourse();
        Bundle bundle = new Bundle();
        bundle.putString(COURSE_PATH, coursePath);
        bundle.putString(USER_ID, userID);
        bundle.putBoolean(NEW_COURSE, newCourse);
        fragment.setArguments(bundle);

        return fragment;
    }

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
        courseCollectionReference = FirebaseFirestore.getInstance().collection(getArguments().getString(COURSE_PATH));

        editTitle = view.findViewById(R.id.etCourseTitle);

        doneButton = view.findViewById(R.id.btnDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getBoolean(NEW_COURSE)){
                    addCourse();
                }
                save();
            }
        });

        cancelButton = view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endFragment();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void save(){
    }

    private void addCourse(){
        CourseModel course = new CourseModel(editTitle.getText().toString());
        course.setCreatorID(getArguments().getString(USER_ID));

        courseCollectionReference.add(course);
        String message = editTitle.getText().toString() + " course is successfully added";
        AndroidUtil.showToast(getContext(), message);
        endFragment();
    }

    private void endFragment(){
        getFragmentManager().popBackStack();
    }
}