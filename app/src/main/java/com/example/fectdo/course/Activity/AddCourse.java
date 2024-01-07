package com.example.fectdo.course.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fectdo.InternalStorageManager.TextFileManager;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    String currentTopicName;
    Uri imageUri;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCourse.this);
                builder.setTitle("Leaving page")
                        .setMessage("Are you confirm to save this change? \n" +
                                "You cannot edit this afterward")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            addCourse();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            return;
                        })
                        .setCancelable(true);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        cancelButton = findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

                    if(imageUri != null){
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        // Create a unique filename for the image
                        String fileName = "images/" + UUID.randomUUID().toString();

                        // Create a reference to the image in Firestore Storage
                        StorageReference imageRef = storageRef.child(fileName);

                        // Upload the image to Firestore Storage
                        UploadTask uploadTask = imageRef.putFile(imageUri);

                        // Listen for the success or failure of the upload task
                        uploadTask.addOnCompleteListener(doUpload -> {
                            if (doUpload.isSuccessful()) {
                                // Image uploaded successfully
                                // You can get the download URL if needed
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Now you can save this URL to Firestore or do whatever you need
                                    CourseModel course = new CourseModel(courseTitle);
                                    course.setCreatorID(getIntent().getStringExtra(USER_ID));
                                    course.setTopics(topicList);
                                    course.setImageUrl(imageUrl);
                                    courseCollectionReference.add(course).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            DocumentReference document = task.getResult();
                                            document.update("documentID", document.getId());
                                        }
                                    });
                                });
                            } else {
                                // Handle the failure
                                Exception exception = doUpload.getException();
                                // Handle the exception accordingly
                            }
                        });
                    }
                    else{

                    }
                    showToast(courseTitle + " course is successfully added");
                }
            }
        });
        showToast(courseTitle + " course is successfully added");
        finish();
    }

    void addTopicForm(){
        View view = getLayoutInflater().inflate(R.layout.layout_add_topic_form, null);
        int index = viewList.size();

        deleteTopic.setEnabled(true);
        quizManagerList.add(new QuizManager());
        viewList.add(view);

        TextView topicNumber = view.findViewById(R.id.topicNumberText);
        topicNumber.setText("Topic " +viewList.size());

        TextView topicName = view.findViewById(R.id.topicNameInput);

        view.findViewById(R.id.btnAddQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuizIndex = index;
                currentTopicName = topicName.getText().toString();
                disableActivity();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit();
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
                        popFragment();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        return;
                    })
                    .setCancelable(true);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Leaving page")
                    .setMessage("Are you sure want to discard without saving?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        return;
                    })
                    .setCancelable(true).show();

//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
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

    public void pickImage(View view){
        //check either user has permission to access file or not
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageButton showImage = findViewById(R.id.uploadImageButton);
            Glide.with(this)
                    .load(imageUri)
                    .into(showImage);
        }
    }

    public String getTopicName(){
        return currentTopicName;
    }

    public void popFragment(){
        getSupportFragmentManager().popBackStack();
    }
}