package com.example.fectdo.social.request;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fectdo.R;
import com.example.fectdo.social.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView rvRequest;
    private RequestAdapter adapter;
    private List<RequestModel> requestModelList;
    private TextView tvEmptyRequestList;
    private DatabaseReference databaseReferenceRequest;
    private FirebaseFirestore databaseReferenceUser;
    private FirebaseUser currentUser;
    private View progressBar;

    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        tvEmptyRequestList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceUser = FirebaseFirestore.getInstance();
        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference().child("FRIEND_REQUEST").child(currentUser.getUid());

        databaseReferenceRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestModelList.clear();
                progressBar.setVisibility(View.GONE);

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.exists()){
                        String requestType = ds.child("REQUEST_TYPE").getValue().toString();
                        if(requestType.equals(Constants.REQUEST_STATUS_RECEIVED)){

                            String userId = ds.getKey();

                            databaseReferenceUser.collection("users")
                                    .document(userId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        // Handle the result
                                        if (documentSnapshot.exists()) {

                                            String userName = documentSnapshot.getString("username");
                                            String photoName = "";

                                            if(documentSnapshot.getString("fileUrl") != null || documentSnapshot.getString("fileUrl") != ""){
                                                photoName = documentSnapshot.getString("fileUrl");
                                            }

                                            RequestModel requestModel = new RequestModel(userId,userName,photoName);
                                            requestModelList.add(requestModel);
                                            adapter.notifyDataSetChanged();
                                            tvEmptyRequestList.setVisibility(View.GONE);


                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getActivity(), "Failed to fetch friends requests:", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failures
                                        Toast.makeText(getActivity(), "Failed to fetch user details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Failed to fetch friends requests: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
