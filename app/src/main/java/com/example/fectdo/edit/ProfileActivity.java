package com.example.fectdo.edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fectdo.R;
import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.general.LoginEmailPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    private EditText etEmail, etName;
    private TextInputEditText etBio;
    private ImageView ivProfile;
    private FirebaseUser firebaseUser;
    private Uri serverFileUri;

    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(ProfileActivity.this, HomePage.class);
                                startActivity(homeIntent);
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.navigation_setting:
                                Intent settingIntent = new Intent(ProfileActivity.this, SettingActivity.class);
                                startActivity(settingIntent);
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.navigation_profile:
                                Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                overridePendingTransition(0,0);
                                return true;
                            default:
                                return false;
                        }
                    }
                }
        );

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        ivProfile = findViewById(R.id.ivProfile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        String username = documentSnapshot.getString("username");
                        String bio = documentSnapshot.getString("description");
                        if (username != null) {
                            etName.setText(username);
                            etEmail.setText(firebaseUser.getEmail());
                            etBio.setText(bio);
                        } else {
                            Log.d("ProfileActivity", "Username is null");
                        }
                    } else {
                        Log.d("ProfileActivity", "User document does not exist in Firestore");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ProfileActivity", "Error getting user document from Firestore", e);
                }
            });

            serverFileUri = firebaseUser.getPhotoUrl();

            if (serverFileUri != null) {
                Glide.with(this)
                        .load(serverFileUri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(ivProfile);
            }
        } else {
            Log.d("ProfileActivity", "Firebase User is null");
        }


    }

    public void btnLogoutClick(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginEmailPassword.class);
        startActivity(intent);
        finish();
    }

    public void saveButton(View view) {
        signUpBtn = findViewById(R.id.btnSave);
        String username = etName.getText().toString();
        String email = etEmail.getText().toString();
        String description = etBio.getText().toString();  // Assuming you have an EditText for description

        if (firebaseUser != null) {
            // Update Firebase Authentication user profile
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("ProfileActivity", "User profile updated.");
                                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Log.e("ProfileActivity", "Failed to update user profile.");
                                Toast.makeText(ProfileActivity.this,"Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update Firestore user document
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

            userRef.update("username", username,
                            "emailAddress", email,
                            "description", description)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("ProfileActivity", "User document updated in Firestore.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ProfileActivity", "Error updating user document in Firestore", e);
                        }
                    });
        }


    }
}
