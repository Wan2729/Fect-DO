package com.example.fectdo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.R;
import com.example.fectdo.models.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostFeedAdapter extends RecyclerView.Adapter<PostFeedAdapter.SocialPostFeedViewHolder> {

    List<PostModel> listPost;
    Context context;

    public PostFeedAdapter(List<PostModel> listPost, Context context) {
        this.listPost = listPost;
        this.context = context;
    }

    @NonNull
    @Override
    public SocialPostFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_row,parent,false);
        return new SocialPostFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostFeedViewHolder holder, int position) {
        PostModel postModel = listPost.get(position);
        holder.bind(postModel);
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    static class SocialPostFeedViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTV,postTV,likeCount,timeStamp,levelTextView;


        public SocialPostFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.usernameTextView);
            postTV = itemView.findViewById(R.id.postTextView);
            likeCount = itemView.findViewById(R.id.numberOfLikesTextView);
            timeStamp = itemView.findViewById(R.id.timeStampTextView);
            levelTextView = itemView.findViewById(R.id.levelTextView);
        }

        public void bind(PostModel post){
            usernameTV.setText(post.getUsername());
            postTV.setText(post.getPostDescription());
            likeCount.setText(Integer.toString(post.getLike())+" Likes");

            String timeStampString = post.getTimestamp().replace(" ","");
            Log.d("TIMESTAMP CHECK", "bind: "+timeStampString);
            String year = timeStampString.substring(0,4);
            String month = "";
            String day = "";
            String hour = "";
            String minute = "";
            int i = 4;
            while (!Character.isDigit(timeStampString.charAt(i))){
                month += timeStampString.charAt(i);
                i++;
            }
            day = timeStampString.substring(i,i+2);
            hour = timeStampString.substring(i+2,i+2+2);
            minute = timeStampString.substring(i+4,i+4+2);
            timeStampString = hour+minute+"H "+day+" "+month+" "+year;
            timeStamp.setText(timeStampString);

            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(post.getUserId());
            userRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot ds = task.getResult();
                                if(ds.exists()){
                                    Long longNullTest = ds.getLong("level");
                                    if(longNullTest==null){
                                        userRef.update("level",0);
                                        levelTextView.setText("0");
                                    } else {
                                        int level = longNullTest.intValue();
                                        levelTextView.setText(Integer.toString(level));
                                    }
                                } else {
                                    levelTextView.setText("0");
                                    Log.d("PR EXIST", "onComplete: Ds not exist");
                                }
                            } else {
                                levelTextView.setText("0");
                                Log.d("PR TASK", "onComplete: Task is not succeed");
                            }
                        }
                    });
        }
    }
}
