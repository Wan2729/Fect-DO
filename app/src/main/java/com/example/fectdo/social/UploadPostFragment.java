package com.example.fectdo.social;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class UploadPostFragment extends Fragment {

    FirebaseAuth auth;
    Button uploadButton;
    EditText postDescriptionInput;
    ProgressBar progressBar;
    View view;

    public UploadPostFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_post, container, false);

        uploadButton = view.findViewById(R.id.uploadButton);
        postDescriptionInput = view.findViewById(R.id.postInput);
        auth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);

        uploadButton.setOnClickListener(v->{
            String postDescription = postDescriptionInput.getText().toString();
            if(postDescription.length()<1){
                postDescriptionInput.setError("Please input something.");
                return;
            }
            uploadPostToDatabase(postDescription);
        });

        return view;
    }

    void uploadPostToDatabase(String postDesc ){
        progressBar.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.GONE);

        String userId = auth.getUid();
        final String[] username = new String[1];

        long timeInMili = System.currentTimeMillis();
        Date currentDate = new Date(timeInMili);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMMddHHmm", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
        String timestamp = dateFormat.format(currentDate);

        String postId = userId+"_"+timestamp;

        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot ds = task.getResult();
                            if(ds.exists()){
                                username[0] = ds.getString("username");
                                PostModel postModel = new PostModel(userId,postId,username[0],timestamp,postDesc,0);

                                //Add to database
                                FirebaseDatabase.getInstance().getReference("Posts")
                                        .child(auth.getUid().toString())
                                        .child(postId)
                                        .setValue(postModel)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(view.getContext(), "Data added to database.", Toast.LENGTH_SHORT).show();
                                                postDescriptionInput.setText("");
                                                progressBar.setVisibility(View.GONE);
                                                uploadButton.setVisibility(View.VISIBLE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Failed add data to database.", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                uploadButton.setVisibility(View.VISIBLE);
                                            }
                                        });


                            } else {
                                Toast.makeText(view.getContext(), "Document not exist.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Error getting database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}