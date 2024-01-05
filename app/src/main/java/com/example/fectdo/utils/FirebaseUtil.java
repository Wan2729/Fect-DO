package com.example.fectdo.utils;

import androidx.annotation.NonNull;

import com.example.fectdo.models.CourseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;

public class FirebaseUtil {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DocumentReference currentUserDetails() {
        String userId = currentUserId();
        if (userId != null) {
            return FirebaseFirestore.getInstance().collection("users").document(userId);
        } else {
            // Handle the case where userId is null
            // For example, you can log a message or return a default document reference.
            // Here, I'll return a reference to the "users" collection without a specific document.
            return FirebaseFirestore.getInstance().collection("users").document();
        }
    }

    public static CollectionReference getCollection(String collectionName) {
        return FirebaseFirestore.getInstance().collection(collectionName);
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static <T> Task<T> getDocumentById(String collectionName, String documentId, Class<T> documentClass) {
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .document(documentId);

        return documentReference.get().continueWith(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    return document.toObject(documentClass);
                }
            }
            return null;
        });
    }

    public static CourseModel getCourseModel(String collectionName, String documentId) {
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection(collectionName)
                .document(documentId);

        try {
            Task<DocumentSnapshot> task = documentReference.get();
            Tasks.await(task);  // Blocking call to wait for the result

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    CourseModel courseModel = document.toObject(CourseModel.class);
                    if (courseModel != null) {
                        courseModel.setDocumentID(document.getId());
                    }
                    return courseModel;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
