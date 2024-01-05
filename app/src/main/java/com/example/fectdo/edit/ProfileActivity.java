package com.example.fectdo.edit;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.fectdo.R;
import com.example.fectdo.career.CareerMain;
import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.general.LoginEmailPassword;
import com.example.fectdo.general.SignUpUsernameEmailPassword;
import com.example.fectdo.general.WelcomePage;
import com.example.fectdo.utils.Navigation;
import com.example.fectdo.utils.NodeNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity {

    private Navigation navigation;
    private EditText etEmail,etName;
    private TextInputEditText etBio;
    private ImageView ivProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference fileStorage;
    private Uri localFileUri, serverFileUri;
    private FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Make sure this is correct
        navigation = new Navigation(this);
        navigation.setToolbarAndBottomNavigation(R.id.toolbar, R.id.nav_view);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        ivProfile = findViewById(R.id.ivProfile);
        fileStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if(firebaseUser != null){

            etName.setText(firebaseUser.getDisplayName());
            etEmail.setText(firebaseUser.getEmail());
            serverFileUri = firebaseUser.getPhotoUrl();

            if(serverFileUri != null){
                Glide.with(this)
                        .load(serverFileUri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(ivProfile);

            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(firebaseUser.getUid());

            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String bio = documentSnapshot.getString("description");
                    etBio.setText(bio);
                }
            }).addOnFailureListener(e -> {
                etBio.setText("");
            });
        }

    }

    public void btnLogoutClick(View view){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginEmailPassword.class);
        startActivity(intent);
    }

    public void saveButton(View view){
        if(etName.getText().toString().trim().equals("")){
            etName.setError("Enter name");
        }
        else{
            if(localFileUri != null){
                updateNameAndPhoto();
            }
            if(etName.getText() != null && etEmail != null){
                updateOnlyName();
            }
        }
    }

    private void updateNameAndPhoto() {
        String strFileName = firebaseUser.getUid() + ".jpg";
        final StorageReference fileRef = fileStorage.child("images/" + strFileName);

        fileRef.putFile(localFileUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    serverFileUri = uri;

                    updateProfileData(etName.getText().toString().trim());
                    updateFirestore(etName.getText().toString(), etEmail.getText().toString(), etBio.getText().toString());
                });
            } else {
                Log.e("FirebaseStorage", "Failed to upload file", task.getException());
                Toast.makeText(ProfileActivity.this, "Failed to upload file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOnlyName() {
        updateProfileData(etName.getText().toString().trim());
        updateFirestore(etName.getText().toString(), etEmail.getText().toString(), etBio.getText().toString());
    }

    private void updateProfileData(String username) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(serverFileUri)
                .build();

        firebaseUser.updateProfile(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userID = firebaseUser.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(NodeNames.NAME, username);
                hashMap.put(NodeNames.DESCRIPTION, etBio.getText().toString());

                if (serverFileUri != null) {
                    hashMap.put(NodeNames.PHOTO, serverFileUri.getPath());
                }

                databaseReference.child(userID).setValue(hashMap).addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        Log.d("FirebaseDatabase", "User profile updated successfully");
                        Toast.makeText(ProfileActivity.this, "User profile updated successfully", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Log.e("FirebaseDatabase", "Failed to update profile in Realtime Database", databaseTask.getException());
                        Toast.makeText(ProfileActivity.this, "Failed to update profile in Realtime Database" + databaseTask.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("FirebaseDatabase", "Failed to update profile in Authentication", task.getException());
                Toast.makeText(ProfileActivity.this, "Failed to update profile in Authentication" + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFirestore(String username, String email, String description) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        String photoUriString = (serverFileUri != null) ? serverFileUri.getPath() : "";

        userRef.update("username", username,
                        "emailAddress", email,
                        "description", description,
                        "photoUri", photoUriString)
                .addOnSuccessListener(aVoid -> Log.d("ProfileActivity", "User document updated in Firestore."))
                .addOnFailureListener(e -> Log.e("ProfileActivity", "Error updating user document in Firestore", e));
    }




    public void pickImage(View view){
        //check either user has permission to access file or not
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==102){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,101);
            }
            else{
                Toast.makeText(this, "Access permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode==RESULT_OK){

                localFileUri =data.getData();
                ivProfile.setImageURI(localFileUri);
            }
        }
    }









    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBtn:
                handleLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleLogout() {
        Intent intent = new Intent(ProfileActivity.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
