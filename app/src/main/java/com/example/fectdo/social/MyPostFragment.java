package com.example.fectdo.social;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.adapter.PostFeedAdapter;
import com.example.fectdo.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPostFragment extends Fragment {

    TextView descriptionTextView,usernameTextView;
    String username,description;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    View view;
    public MyPostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = view.findViewById(R.id.recycleView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        descriptionTextView = view.findViewById(R.id.descriptiontextView);

        mAuth = FirebaseAuth.getInstance();

        setUpUsernameAndDescription();

        List<PostModel> postModels = new ArrayList<>();
        PostFeedAdapter postFeedAdapter = new PostFeedAdapter(postModels,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postFeedAdapter);

        if(mAuth==null){
            return null;
        }

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(mAuth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("MY POST CHECK", "onDataChange: PASS HERE");
                postModels.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    PostModel post = ds.getValue(PostModel.class);
                    postModels.add(post);
                }
                Collections.reverse(postModels);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Error getting database.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setUpUsernameAndDescription(){
        if(mAuth==null){
            return;
        }
        DocumentReference dataRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(mAuth.getUid());
        dataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists()) {
                        username = ds.getString("username");
                        description = ds.getString("description");
                        usernameTextView.setText(username);
                        descriptionTextView.setText(description);
                    } else {
                        Toast.makeText(view.getContext(), "Document Snapshot not exist.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Failed getting database.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}