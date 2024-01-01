package com.example.fectdo.general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.util.HashMap;

public class
SignUpUsernameEmailPassword extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailInput;
    EditText passwordInput;
    EditText reEnterPasswordInput;
    Button signUpBtn;
    EditText usernameInput;
    UserModel userModel;
    CollectionReference userDatabase;
    ProgressBar progressBar;

    private FirebaseUser firebaseUser;
    private ImageView ivProfile;
    private DatabaseReference databaseReference;
    private StorageReference fileStorage;
    private Uri localFileUri, serverFileUri;
    private String description ="";

    private String strFileName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_username_email_password);

        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        reEnterPasswordInput = findViewById(R.id.reEnterPasswordInput);
        signUpBtn = findViewById(R.id.btnSave);
        usernameInput = findViewById(R.id.etName);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseUtil.getCollection("users");
        ivProfile = findViewById(R.id.ivProfile);
        fileStorage = FirebaseStorage.getInstance().getReference();


        signUpBtn.setOnClickListener((v) -> {
            String username = usernameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String reEnterPassword = reEnterPasswordInput.getText().toString();

            if (username.length() < 5) {
                usernameInput.setError("Username must be at least 8 characters long.");
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
                passwordInput.setError("Password must be at least 8 characters long.");
                return;
            }

            if (!reEnterPassword.equals(password)) {
                reEnterPasswordInput.setError("Password is not the same.");
                return;
            }

            signUpBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            emailIsRegistered(email, isTakenEmail -> {
                if(isTakenEmail){
                    signUpBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    emailInput.setError("This email is already registered.");
                } else {
                    usernameIsTaken(username, isTakenUserame -> {
                        if (isTakenUserame){
                            signUpBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            usernameInput.setError("This username is not available");
                        } else {
                            signUp(username, email, password, mAuth);
                        }
                    });
                }
            });
        });
    }

    void signUp(String username, String email, String password, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                mAuth.getCurrentUser();
                if (localFileUri != null) {
                    updateNameAndPhoto();
                }
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_LONG).show();

                userModel = new UserModel(username, Timestamp.now(), email, this.description, serverFileUri.getPath());
                userModel.setUsername(username);

                FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SignUpUsernameEmailPassword.this, HomePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                signUpBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_LONG).show();
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        strFileName = firebaseUser.getUid() + ".jpg";
        final StorageReference fileRef = fileStorage.child("images/" + strFileName);

        fileRef.putFile(localFileUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    serverFileUri = uri;


                                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                             .setDisplayName(usernameInput.getText().toString().trim())
                                            .setPhotoUri(serverFileUri)
                                            .build();

                                    firebaseUser.updateProfile(request)
                                            .addOnCompleteListener(profileTask -> {
                                                if (profileTask.isSuccessful()) {
                                                    String userID = firebaseUser.getUid();
                                                    // Use the correct reference structure here
                                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("PHOTO", serverFileUri.getPath());

                                                    databaseReference.setValue(hashMap)
                                                            .addOnCompleteListener(dbTask -> {
                                                                if (dbTask.isSuccessful()) {
                                                                    Toast.makeText(SignUpUsernameEmailPassword.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(SignUpUsernameEmailPassword.this, HomePage.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                } else {
                                                                    handleError(dbTask.getException(), "Failed to update profile in database");
                                                                }
                                                            });
                                                } else {
                                                    handleError(profileTask.getException(), "Failed to update user profile");
                                                }
                                            });
                                });
                    } else {
                        // Handle the case where file upload fails
                        handleError(task.getException(), "Failed to upload profile picture");
                    }
                });
    }


    private void handleError(Exception exception, String message) {
        Toast.makeText(SignUpUsernameEmailPassword.this, message + exception.getMessage(), Toast.LENGTH_SHORT).show();
        signUpBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
