package com.example.fectdo.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fectdo.course.Activity.HomePage;
import com.example.fectdo.models.CourseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference getCollection(String collectionName){
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    public static boolean isLoggedIn(){
        return currentUserId()!=null;
    }

    public static void logOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
