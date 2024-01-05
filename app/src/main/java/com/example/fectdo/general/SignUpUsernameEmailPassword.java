package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.fectdo.utils.NodeNames;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fectdo.R;
import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.models.UserModel;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class
SignUpUsernameEmailPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailInput;
    EditText passwordInput;
    EditText reEnterPasswordInput;
    Button signUpBtn;
    EditText usernameInput;
    UserModel userModel;
    CollectionReference userDatabase;



    private FirebaseUser firebaseUser;
    private ImageView ivProfile;
    private DatabaseReference databaseReference;
    private StorageReference fileStorage;
    private Uri localFileUri, serverFileUri;
    private String strFileName = "";
    private View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_username_email_password);

        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        reEnterPasswordInput = findViewById(R.id.reEnterPasswordInput);
        signUpBtn = findViewById(R.id.btnSave);
        usernameInput = findViewById(R.id.etName);
        userDatabase = FirebaseUtil.getCollection("users");
        ivProfile = findViewById(R.id.ivProfile);
        fileStorage = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        fileStorage = FirebaseStorage.getInstance().getReference().child("images");



    }

    public void signUpButton(View view){
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String reEnterPassword = reEnterPasswordInput.getText().toString();

            if (username.length() < 5) {
                usernameInput.setError("Username must be at least 5 characters long.");
                return;
            }

            if (username.contains(" ")) {
                usernameInput.setError("Username cannot contains space.");
                return;
            }

            if (email.isEmpty()) {
                emailInput.setError("Please put an Email.");
                return;
            }

            if (password.length() < 8) {
                passwordInput.setError("Password must be at least 5 characters long.");
                return;
            }

            if (!reEnterPassword.equals(password)) {
                reEnterPasswordInput.setError("Password is not the same.");
                return;
            }




        emailIsRegistered(email, isTakenEmail -> {
            if (isTakenEmail) {
                emailInput.setError("This email is already registered.");
            } else {
                usernameIsTaken(username, isTakenUserame -> {
                    if (isTakenUserame) {
                        usernameInput.setError("This username is not available");
                    } else {
                        signUp(username, email, password,firebaseAuth);  // Removed 'firebaseAuth' parameter
                    }
                });
            }
        });
    }

    interface UsernameCheckCallback {
        void onUsernameChecked(boolean isTaken);
    }

    interface EmailCheckCallBack{
        void onEmailChecked(boolean isTaken);
    }

    private void emailIsRegistered(String email,EmailCheckCallBack callBack) {
        userDatabase.whereEqualTo("emailAddress",email)
                .get()
                .addOnCompleteListener(task -> {
                    boolean isTaken = false;
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if(document.exists()){
                                isTaken = true;
                                break;
                            }
                        }
                    } else {
                        Log.d("SignUP","Error getting documents",task.getException());
                    }
                    callBack.onEmailChecked(isTaken);
                });

    }

    void usernameIsTaken(String username, UsernameCheckCallback callback) {
        userDatabase.whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    boolean isTaken = false;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                isTaken = true;
                                break;
                            }
                        }
                    } else {
                        Log.d("SignUP", "Error getting documents: ", task.getException());
                    }
                    callback.onUsernameChecked(isTaken);
                });
    }

    public void signUp(String username, String email, String password, FirebaseAuth mAuth) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                // Get the current user after successful creation
                firebaseUser = mAuth.getCurrentUser();

                progressBar.setVisibility(View.GONE);
                if (localFileUri != null) {
                    updateNameAndPhoto();
                } else {
                    updateOnlyName();
                }

                // Check if serverFileUri is not null before accessing its path
                if (serverFileUri != null) {
                    userModel = new UserModel(email, username, "true", "", serverFileUri.getPath());
                    userModel.setUsername(username);
                } else {
                    userModel = new UserModel(email, username, "true", "", "");
                    userModel.setUsername(username);
                }

                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignUpUsernameEmailPassword.this, HomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpUsernameEmailPassword.this, "Error creating database SIGN UP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_LONG).show();
            }
        });
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

    private void updateNameAndPhoto() {
        progressBar.setVisibility(View.VISIBLE);

        String strFileName = firebaseUser.getUid()+".jpg";
        final StorageReference fileRef = fileStorage.child("images/");

        fileRef.putFile(localFileUri).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    serverFileUri = uri;

                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernameInput.getText().toString().trim())
                            .setPhotoUri(Uri.parse(serverFileUri.toString())).build();

                    firebaseUser.updateProfile(request).addOnCompleteListener(updateProfileTask -> {
                        if (updateProfileTask.isSuccessful()) {
                            String userID = firebaseUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(NodeNames.NAME, usernameInput.getText().toString());
                            hashMap.put(NodeNames.EMAIL, emailInput.getText().toString());
                            hashMap.put(NodeNames.ONLINE, "true");
                            hashMap.put(NodeNames.PHOTO, serverFileUri.getPath());
                            hashMap.put(NodeNames.DESCRIPTION, "");

                            databaseReference.child(userID).setValue(hashMap).addOnCompleteListener(writeToDatabaseTask -> {
                                if (writeToDatabaseTask.isSuccessful()) {
                                    Toast.makeText(SignUpUsernameEmailPassword.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpUsernameEmailPassword.this, HomePage.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Log.e("FirebaseDatabase", "Failed to write to database", writeToDatabaseTask.getException());
                                    Toast.makeText(SignUpUsernameEmailPassword.this, "Failed to write to database", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.e("FirebaseDatabase", "Failed to update profile", updateProfileTask.getException());
                            Toast.makeText(SignUpUsernameEmailPassword.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Log.e("FirebaseStorage", "Failed to upload file", task.getException());
                Toast.makeText(SignUpUsernameEmailPassword.this, "Failed to upload file", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateOnlyName() {
        progressBar.setVisibility(View.VISIBLE);
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(usernameInput.getText().toString().trim()).build();

        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    String userID = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(NodeNames.NAME, usernameInput.getText().toString());
                    hashMap.put(NodeNames.EMAIL, emailInput.getText().toString());
                    hashMap.put(NodeNames.ONLINE, "true");
                    hashMap.put(NodeNames.PHOTO, "");

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child(userID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseDatabase", "User profile updated successfully");
                                Toast.makeText(SignUpUsernameEmailPassword.this, "User created successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("FirebaseDatabase", "Failed to update profile", task.getException());
                                Toast.makeText(SignUpUsernameEmailPassword.this, "Failed to update profile" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.e("FirebaseDatabase", "Failed to update profile", task.getException());
                }
            }
        });
    }









}
