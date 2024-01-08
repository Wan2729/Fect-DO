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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.adapter.PostFeedAdapter;
import com.example.fectdo.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

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
    ProgressBar progressBar;
    View view;
    FirebaseAuth mAuth;

    public PostFeedFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post_feed, container, false);
        firstArrive = true;

        recyclerView = view.findViewById(R.id.recycleView);
        reloadButton = view.findViewById(R.id.refreshButton);
        progressBar = view.findViewById(R.id.progressBar);

        dbRef = FirebaseDatabase.getInstance().getReference("Posts");

        postList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postFeedAdapter = new PostFeedAdapter(postList,getContext());
        recyclerView.setAdapter(postFeedAdapter);

        mAuth = FirebaseAuth.getInstance();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemDoubleTap(int position) {
                PostModel likedPost = postList.get(position);
                String postId = likedPost.getPostId();
                DatabaseReference childLikedPost = FirebaseDatabase.getInstance().getReference("LikedPost").child(postId);
                if (mAuth.getUid()!=null) {
                    childLikedPost.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean postLiked = false;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String value = ds.getKey();
                                Log.d("SEE USERNAME",value.toString());
                                if (value.equals(mAuth.getUid())) {
                                    postLiked = true;
                                    break;
                                }
                            }
                            if (!postLiked) {
                                likedPost.setLike(likedPost.getLike()+1);
                                FirebaseDatabase.getInstance().getReference("Posts").child(likedPost.getUserId()).child(likedPost.getPostId()).setValue(likedPost);
                                childLikedPost.child(mAuth.getUid()).setValue("");
                                postFeedAdapter.notifyItemChanged(position);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        },recyclerView));

        if(firstArrive) {
            updateRecyclerView();
        }

        reloadButton.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            reloadButton.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Error getting database.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                reloadButton.setVisibility(View.VISIBLE);
            }
        });
        firstArrive = false;
    }
}