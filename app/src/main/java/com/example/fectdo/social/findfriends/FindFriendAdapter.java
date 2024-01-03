package com.example.fectdo.social.findfriends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.fectdo.R;
import com.example.fectdo.utils.Constants;
import com.example.fectdo.utils.NodeNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.FindFriendViewHolder> {

    private Context context;
    private List<FindFriendModel> findFriendModelList;

    private DatabaseReference friendRequestDatabase;
    private FirebaseUser currentUser;
    private String userId;

    public FindFriendAdapter(Context context, List<FindFriendModel> findFriendModelList) {
        this.context = context;
        this.findFriendModelList = findFriendModelList;
    }

    @NonNull
    @Override
    public FindFriendAdapter.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_friends,parent,false);
        return new FindFriendViewHolder(view);
    }

    public void onBindViewHolder(@NonNull FindFriendAdapter.FindFriendViewHolder holder, int position) {
        FindFriendModel friendModel = findFriendModelList.get(position);

        holder.tvFullName.setText(friendModel.getUserName());
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(Constants.IMAGE_FOLDER + "/" + friendModel.getPhotoName());
        Log.d("FindFriendAdapter", "PhotoName: " + friendModel.getPhotoName());
        Log.d("FindFriendAdapter", "StorageReference path: " + fileRef.getPath());

        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .into(holder.ivProfile);
        });

        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUEST);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(friendModel.isRequestSent()){
            holder.btnSendRequest.setVisibility(View.GONE);
            holder.btnCancelRequest.setVisibility(View.VISIBLE);
        }
        else{
            holder.btnSendRequest.setVisibility(View.VISIBLE);
            holder.btnCancelRequest.setVisibility(View.GONE);
        }

        holder.btnSendRequest.setOnClickListener(v -> {
            holder.btnSendRequest.setVisibility(View.GONE);
            holder.pbRequest.setVisibility(View.VISIBLE);

            userId = friendModel.getUserID();

            friendRequestDatabase.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE)
                    .setValue(Constants.REQUEST_STATUS_SENT).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            friendRequestDatabase.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE)
                                    .setValue(Constants.REQUEST_STATUS_RECEIVED).addOnCompleteListener(innerTask -> {
                                        if (innerTask.isSuccessful()) {
                                            Toast.makeText(context, "Request Successfully", Toast.LENGTH_SHORT).show();
                                            holder.btnCancelRequest.setVisibility(View.VISIBLE);
                                            holder.btnSendRequest.setVisibility(View.GONE);
                                            holder.pbRequest.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(context, "Failed to send request: " +task.getException(), Toast.LENGTH_SHORT).show();
                                            holder.btnCancelRequest.setVisibility(View.GONE);
                                            holder.btnSendRequest.setVisibility(View.VISIBLE);
                                            holder.pbRequest.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Failed to send request: " + task.getException(), Toast.LENGTH_SHORT).show();
                            holder.btnCancelRequest.setVisibility(View.GONE);
                            holder.btnSendRequest.setVisibility(View.VISIBLE);
                            holder.pbRequest.setVisibility(View.GONE);
                        }
                    });
        });


        holder.btnCancelRequest.setOnClickListener(v -> {
            holder.btnCancelRequest.setVisibility(View.GONE);
            holder.pbRequest.setVisibility(View.VISIBLE);

            userId = friendModel.getUserID();

            friendRequestDatabase.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE)
                    .setValue(null).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            friendRequestDatabase.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE)
                                    .setValue(null).addOnCompleteListener(innerTask -> {
                                        if (innerTask.isSuccessful()) {
                                            Toast.makeText(context, "Cancel Successfully", Toast.LENGTH_SHORT).show();
                                            holder.btnCancelRequest.setVisibility(View.GONE);
                                            holder.btnSendRequest.setVisibility(View.VISIBLE);
                                            holder.pbRequest.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(context, "Failed to cancel request: " +task.getException(), Toast.LENGTH_SHORT).show();
                                            holder.btnCancelRequest.setVisibility(View.VISIBLE);
                                            holder.btnSendRequest.setVisibility(View.GONE);
                                            holder.pbRequest.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Failed to cancel request: " + task.getException(), Toast.LENGTH_SHORT).show();
                            holder.btnCancelRequest.setVisibility(View.VISIBLE);
                            holder.btnSendRequest.setVisibility(View.GONE);
                            holder.pbRequest.setVisibility(View.GONE);
                        }
                    });
        });
    }


    @Override
    public int getItemCount() {
        return findFriendModelList.size();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvFullName;
        private Button btnSendRequest,btnCancelRequest;
        private ProgressBar pbRequest;


        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
            btnCancelRequest = itemView.findViewById(R.id.btnCancelRequest);
            pbRequest = itemView.findViewById(R.id.pbRequest);

        }
    }
}
