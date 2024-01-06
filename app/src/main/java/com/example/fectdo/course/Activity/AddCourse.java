package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fectdo.R;
import com.example.fectdo.Soalan.QuizManager;
import com.example.fectdo.course.Fragment.AddQuizForm;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.models.TopicModel;
import com.example.fectdo.utils.AndroidUtil;
import com.example.fectdo.utils.FirebaseUtil;
import com.example.fectdo.utils.YouTubeLinkConverter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourse extends AppCompatActivity {
    final static String COURSE_PATH = "COURSE_PATH_REF", USER_ID = "USER_ID", NEW_COURSE = "NEW_COURSE";
    CollectionReference courseCollectionReference, topicCollectionReference;
    EditText editTitle;
    Button doneButton, cancelButton, addTopic, deleteTopic;
    LinearLayout topicLayout;
    List<String> topicList;

    List<View> viewList;
    List<QuizManager> quizManagerList;
    AddQuizForm fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseCollectionReference = FirebaseUtil.getCollection(getIntent().getStringExtra(COURSE_PATH));
        topicCollectionReference = FirebaseUtil.getCollection("topics");

        editTitle = findViewById(R.id.etCourseTitle);
        topicLayout = findViewById(R.id.linearLayout);
        viewList = new ArrayList<>();
        quizManagerList = new ArrayList<>();
        fragment = new AddQuizForm();

        addTopic = findViewById(R.id.btnAddTopic);
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTopicCard();
            }
        });

        deleteTopic = findViewById(R.id.btnDeleteTopic);
        if(viewList.size() == 0){
            deleteTopic.setEnabled(false);
        }
        else{
            deleteTopic.setEnabled(true);
        }
        deleteTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTopicCard();
            }
        });

        doneButton = findViewById(R.id.btnDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra(NEW_COURSE, true)){
                    addCourse();
                }
            }
        });

        cancelButton = findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addCourse(){
        String courseTitle = editTitle.getText().toString();
        topicList = new ArrayList<>();
        Map<String, String> topicDetailsList= new HashMap<>();

        if(courseTitle.equals("")){
            editTitle.setError("This field cannot be empty");
            showToast("Please enter course title");
            return;
        }

        if(viewList.size() == 0){
            showToast("Please add at least a topic to create a course");
            return;
        }

        for(View view : viewList){
            EditText topicName = view.findViewById(R.id.topicNameInput);
            EditText videoLink = view.findViewById(R.id.videoLinkInput);

            if(topicName.getText().toString().equals("")){
                topicName.setError("Please enter a topic here or delete it");
                return;
            }
            if(videoLink.getText().toString().equals("")){
                videoLink.setError("Please enter a video link here");
                return;
            }

            topicDetailsList.put(topicName.getText().toString(), videoLink.getText().toString());
        }

        for(Map.Entry<String, String> entry : topicDetailsList.entrySet()){
            TopicModel topic = new TopicModel();
            topic.setTopicName(entry.getKey());
            topic.setVideoLink(YouTubeLinkConverter.convertToEmbedLink(entry.getValue()));
            topic.setTopicNum(topicList.size()+1);

            topicCollectionReference.add(topic).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentReference documentReference = task.getResult();
                    topicList.add(documentReference.getId());

                    if(topicList.size() == 0){
                        showToast("Please add at least a topic to create a course");
                    }

                    if(topicList.size() == topicDetailsList.size()){
                        CourseModel course = new CourseModel(courseTitle);
                        course.setCreatorID(getIntent().getStringExtra(USER_ID));
                        course.setTopics(topicList);
                        courseCollectionReference.add(course).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                DocumentReference document = task.getResult();
                                document.update("documentID", document.getId());
                            }
                        });
                        showToast(courseTitle + " course is successfully added");
                    }
                }
            });
        }

        finish();
    }

    void addTopicCard(){
        deleteTopic.setEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.layout_add_topic_form, null);
        viewList.add(view);
        quizManagerList.add(new QuizManager());

        TextView topicIndex = view.findViewById(R.id.textView);
        topicIndex.setText("Topic " +viewList.size());

        view.findViewById(R.id.btnAddQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });

        topicLayout.addView(view);
    }

    void removeTopicCard(){
        topicLayout.removeView(viewList.remove(viewList.size()-1));
        quizManagerList.remove(quizManagerList.size() - 1);
        if(viewList.size() == 0){
            deleteTopic.setEnabled(false);
        }
    }

    private void showToast(String message){
        AndroidUtil.showToast(getApplicationContext(), message);
    }
}