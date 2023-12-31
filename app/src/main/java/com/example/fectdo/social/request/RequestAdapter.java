package com.example.fectdo.social.request;

import android.content.Context;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<RequestModel> requestModelList;
    private Context context;
    private DatabaseReference databaseReferenceRequests,databaseReferenceChats;
    private FirebaseUser currentUser;

    public RequestAdapter(Context context, List<RequestModel> requestModelList) {
        this.context = context;
        this.requestModelList = requestModelList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_layout, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RequestModel requestModel = requestModelList.get(position);

        holder.tvFullName.setText(requestModel.getUserName());
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(Constants.IMAGE_FOLDER + "/" + requestModel.getUserId()+".jpg");

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(holder.ivProfile);
            }
        });

        databaseReferenceRequests = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUEST);
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(NodeNames.CHATS);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbDecision.setVisibility(View.VISIBLE);
                holder.btnDenyRequest.setVisibility(View.GONE);
                holder.btnAcceptRequest.setVisibility(View.GONE);
                String userId = requestModel.getUserId();
                databaseReferenceChats.child(currentUser.getUid()).child(userId).child(NodeNames.TIME_STAMP).setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReferenceChats.child(userId).child(currentUser.getUid()).child(NodeNames.TIME_STAMP).setValue(ServerValue.TIMESTAMP)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        databaseReferenceRequests.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE).setValue(Constants.REQUEST_STATUS_ACCEPTED)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful()){
                                                                                    databaseReferenceRequests.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE).setValue(Constants.REQUEST_STATUS_ACCEPTED)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if(task.isSuccessful()){
                                                                                                        holder.pbDecision.setVisibility(View.GONE);
                                                                                                        holder.btnDenyRequest.setVisibility(View.GONE);
                                                                                                        holder.btnAcceptRequest.setVisibility(View.VISIBLE);

                                                                                                    }
                                                                                                    else{
                                                                                                        handleException(holder,task.getException());
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                                else{
                                                                                    handleException(holder,task.getException());
                                                                                }
                                                                            }
                                                                        });

                                                    }
                                                    else{
                                                        handleException(holder,task.getException());
                                                    }
                                                }
                                            });


                        }
                        else{
                           handleException(holder,task.getException());

                        }
                    }
                });

            }
        });


        holder.btnDenyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbDecision.setVisibility(View.VISIBLE);
                holder.btnDenyRequest.setVisibility(View.GONE);
                holder.btnAcceptRequest.setVisibility(View.GONE);

                String userId = requestModel.getUserId();
                databaseReferenceRequests.child(currentUser.getUid()).child(userId).child(NodeNames.REQUEST_TYPE).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseReferenceRequests.child(userId).child(currentUser.getUid()).child(NodeNames.REQUEST_TYPE).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        holder.pbDecision.setVisibility(View.GONE);
                                        holder.btnDenyRequest.setVisibility(View.VISIBLE);
                                        holder.btnAcceptRequest.setVisibility(View.VISIBLE);

                                    }
                                    else{
                                        Toast.makeText(context, "Failed to deny request", Toast.LENGTH_SHORT).show();
                                        holder.pbDecision.setVisibility(View.GONE);
                                        holder.btnDenyRequest.setVisibility(View.VISIBLE);
                                        holder.btnAcceptRequest.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(context, "Failed to deny request", Toast.LENGTH_SHORT).show();
                            holder.pbDecision.setVisibility(View.GONE);
                            holder.btnDenyRequest.setVisibility(View.VISIBLE);
                            holder.btnAcceptRequest.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });
    }

    public void handleException(RequestViewHolder holder,Exception exception){
        Toast.makeText(context, "Failed to accept request: "+exception, Toast.LENGTH_SHORT).show();
        holder.pbDecision.setVisibility(View.GONE);
        holder.btnDenyRequest.setVisibility(View.VISIBLE);
        holder.btnAcceptRequest.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFullName;
        private ImageView ivProfile;
        private Button btnAcceptRequest, btnDenyRequest;
        private ProgressBar pbDecision;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFullName = itemView.findViewById(R.id.tvFullName);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            btnAcceptRequest = itemView.findViewById(R.id.btnAcceptRequest);
            btnDenyRequest = itemView.findViewById(R.id.btnDenyRequest);
            pbDecision = itemView.findViewById(R.id.pbDecision);
        }
    }
}
