package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fectdo.InternalStorageManager.TextFileManager;
import com.example.fectdo.R;
import com.example.fectdo.Soalan.CourseManager;
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
import java.util.List;

public class AddCourse extends AppCompatActivity implements AddQuizForm.OnDataPassListener{
    final static String COURSE_PATH = "COURSE_PATH_REF", USER_ID = "USER_ID", NEW_COURSE = "NEW_COURSE";
    CollectionReference courseCollectionReference, topicCollectionReference;
    EditText editTitle;
    Button doneButton, cancelButton, addTopic, deleteTopic;
    LinearLayout topicLayout;
    List<String> topicList;

    List<View> viewList;
    List<QuizManager> quizManagerList;
    AddQuizForm fragment;
    int currentQuizIndex;

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
        currentQuizIndex = 0;

        addTopic = findViewById(R.id.btnAddTopic);
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTopicForm();
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
                removeTopicForm();
            }
        });

        doneButton = findViewById(R.id.btnDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getBooleanExtra(NEW_COURSE, true)){
                    addCourse();
                    saveToFile();
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

        if(courseTitle.equals("")){
            editTitle.setError("This field cannot be empty");
            showToast("Please enter course title");
            return;
        }

        if(viewList.size() == 0){
            showToast("Please add at least a topic to create a course");
            return;
        }

        //add topic name & video link to quizManagerList
        for(int i=0 ; i<viewList.size() ; i++){
            View view = viewList.get(i);

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

            quizManagerList.get(i).setTopicName(topicName.getText().toString());
            quizManagerList.get(i).setVideoLink(videoLink.getText().toString());
        }

        for(int i=0 ; i<quizManagerList.size() ; i++){
            TopicModel topicModel = new TopicModel();
            quizManagerList.get(i).loadChoice();
            topicModel.setTopicName(quizManagerList.get(i).getTopicName());
            topicModel.setVideoLink(
                    YouTubeLinkConverter.convertToEmbedLink(quizManagerList.get(i).getVideoLink())
            );
            topicModel.setQuestion(quizManagerList.get(i).getQuestion());
            topicModel.setCorrectAnswer(quizManagerList.get(i).getCorrectAnswer());
            topicModel.setChoiceA(quizManagerList.get(i).getChoiceA());
            topicModel.setChoiceB(quizManagerList.get(i).getChoiceB());
            topicModel.setChoiceC(quizManagerList.get(i).getChoiceC());
            topicModel.setChoiceD(quizManagerList.get(i).getChoiceD());

            saveToFireStore(courseTitle, topicModel);
        }
    }

    private void saveToFireStore(String courseTitle, TopicModel topicModel){
        int totalTopic = quizManagerList.size();

        topicCollectionReference.add(topicModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentReference documentReference = task.getResult();
                topicList.add(documentReference.getId());

                if(topicList.size() == totalTopic){
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
        showToast(courseTitle + " course is successfully added");
    }

    void addTopicForm(){
        View view = getLayoutInflater().inflate(R.layout.layout_add_topic_form, null);
        int index = viewList.size();


        deleteTopic.setEnabled(true);
        viewList.add(view);

        quizManagerList.add(new QuizManager());

        TextView topicNumber = view.findViewById(R.id.topicNumberText);
        topicNumber.setText("Topic " +viewList.size());

        view.findViewById(R.id.btnAddQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuizIndex = index;
                disableActivity();
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
            }
        });

        topicLayout.addView(view);
    }

    void removeTopicForm(){
        topicLayout.removeView(viewList.remove(viewList.size()-1));
        quizManagerList.remove(quizManagerList.size() - 1);
        if(viewList.size() == 0){
            deleteTopic.setEnabled(false);
        }
    }

    private void showToast(String message){
        AndroidUtil.showToast(getApplicationContext(), message);
    }

    private void disableActivity(){
        doneButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        addTopic.setVisibility(View.GONE);
        deleteTopic.setVisibility(View.GONE);
        editTitle.setVisibility(View.GONE);
        findViewById(R.id.cardViewRoot).setVisibility(View.GONE);
        findViewById(R.id.text1).setVisibility(View.GONE);
        findViewById(R.id.text2).setVisibility(View.GONE);
    }

    public void enableActivity(){
        doneButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        addTopic.setVisibility(View.VISIBLE);
        deleteTopic.setVisibility(View.VISIBLE);
        editTitle.setVisibility(View.VISIBLE);
        findViewById(R.id.cardViewRoot).setVisibility(View.VISIBLE);
        findViewById(R.id.text1).setVisibility(View.VISIBLE);
        findViewById(R.id.text2).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(currentFragment.getContext());
            builder.setTitle("Leaving page")
                    .setMessage("Are you sure want to discard without saving?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        enableActivity();
                        getSupportFragmentManager().popBackStack();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        return;
                    })
                    .setCancelable(true);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onDataPass(QuizManager quizManager) {
        quizManagerList.set(currentQuizIndex, quizManager);
    }

    // Example method to update a TextView with quiz data
    private void saveToFile() {
        TextFileManager.deleteFile(getApplicationContext(), "quizList");
        for(int i=0 ; i<quizManagerList.size() ; i++){
            TextFileManager.saveToFile(getApplicationContext(), "quizList", quizManagerList.get(i));
        }
    }
}