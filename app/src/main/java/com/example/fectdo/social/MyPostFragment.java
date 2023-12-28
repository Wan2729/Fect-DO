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
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.adapter.PostFeedAdapter;
import com.example.fectdo.models.PostModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPostFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    public MyPostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = view.findViewById(R.id.recycleView);

        mAuth = FirebaseAuth.getInstance();

        List<PostModel> postModels = new ArrayList<>();
        PostFeedAdapter postFeedAdapter = new PostFeedAdapter(postModels,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postFeedAdapter);

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
                Toast.makeText(getContext(), "Error getting database.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}