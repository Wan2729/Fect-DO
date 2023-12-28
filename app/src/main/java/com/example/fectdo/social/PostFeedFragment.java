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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.adapter.PostFeedAdapter;
import com.example.fectdo.models.PostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PostFeedFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView reloadButton;
    boolean firstArrive;
    List<PostModel> postList;
    PostFeedAdapter postFeedAdapter;
    DatabaseReference dbRef;

    public PostFeedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_feed, container, false);

        firstArrive = true;

        recyclerView = view.findViewById(R.id.recycleView);
        reloadButton = view.findViewById(R.id.refreshButton);

        dbRef = FirebaseDatabase.getInstance().getReference("Posts");

        postList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postFeedAdapter = new PostFeedAdapter(postList,getContext());
        recyclerView.setAdapter(postFeedAdapter);

        if(firstArrive) {
            updateRecyclerView();
        }

        reloadButton.setOnClickListener(v->{
            updateRecyclerView();
        });

        return view;
    }

    public void updateRecyclerView(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot postSnapshot : userSnapshot.getChildren()) {
                        PostModel post = postSnapshot.getValue(PostModel.class);
                        Log.d("POST MODEL CHECK", post.getTimestamp()+"" );
                        postList.add(post);
                    }
                }
                Collections.sort(postList, (post1, post2) -> {
                    if (post1 != null && post2 != null) {
                        // Assuming timestamp is a String in the format "yyyy-MM-dd HH:mm:ss"
                        return post2.getTimestamp().compareTo(post1.getTimestamp());
                    }
                    return 0;
                });
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error getting database.", Toast.LENGTH_SHORT).show();
            }
        });
        firstArrive = false;
    }
}