package com.example.fectdo.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
