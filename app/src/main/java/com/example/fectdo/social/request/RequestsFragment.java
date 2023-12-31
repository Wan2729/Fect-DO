package com.example.fectdo.social.request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.R;
import com.example.fectdo.utils.Constants;
import com.example.fectdo.utils.NodeNames;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView rvRequest;
    private RequestAdapter adapter;
    private List<RequestModel> requestModelList;
    private TextView tvEmptyRequestList;
    private DatabaseReference databaseReferenceRequest, databaseReferenceUsers;
    private FirebaseUser currentUser;
    private View progressBar;

    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvRequest = view.findViewById(R.id.rvRequests);
        tvEmptyRequestList = view.findViewById(R.id.tvEmptyRequestsList);
        progressBar = view.findViewById(R.id.progressBar);

        rvRequest.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestModelList = new ArrayList<>();
        adapter = new RequestAdapter(getActivity(), requestModelList);
        rvRequest.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);
        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUEST).child(currentUser.getUid());

        tvEmptyRequestList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

// In RequestsFragment.java
// ...
        databaseReferenceRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                requestModelList.clear();
                Log.d("RequestsFragment", "DataSnapshot: " + snapshot.getValue());

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("RequestsFragment", "Child: " + ds.getKey());
                    if (ds.exists()) {
                        String requestType = ds.child(NodeNames.REQUEST_TYPE).getValue().toString();
                        Log.d("RequestsFragment", "RequestType: " + requestType);

                        if (requestType != null && requestType.equals(Constants.REQUEST_STATUS_RECEIVED)) {
                            String userId = ds.getKey();
                            Log.d("RequestsFragment", "Received friend request from user ID: " + userId);

                            databaseReferenceUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Log.d("RequestsFragment", "DataSnapshot: " + snapshot.getValue());
                                        String userName = snapshot.child(NodeNames.NAME).getValue().toString();

                                        String photoName = "";
                                        if (snapshot.child(NodeNames.PHOTO).getValue() != null) {
                                            photoName = snapshot.child(NodeNames.PHOTO).getValue().toString();
                                        }

                                        Log.d("RequestsFragment", "User name: " + userName + ", Photo name: " + photoName);

                                        RequestModel requestModel = new RequestModel(userId, userName, photoName);
                                        requestModelList.add(requestModel);
                                        adapter.notifyDataSetChanged();
                                        tvEmptyRequestList.setVisibility(View.GONE);

                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Failed to fetch friends requests: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failed to fetch friends requests: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
// ...

    }
}
