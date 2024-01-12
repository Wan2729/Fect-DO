package com.example.fectdo.course;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fectdo.R;
import com.example.fectdo.Soalan.QuizManager;
import com.example.fectdo.models.CourseModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Random;

public class NotificationScheduler extends BroadcastReceiver {

    FirebaseAuth mAuth;
    RemoteViews remoteViews,remoteViewsExpanded;
    String question;
    String correctAnswer;
    String[] choice;
    int correctAnswerIndex;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SCHEDULER", "onReceive: First");

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        remoteViewsExpanded = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout_expanded);
        String userID = intent.getStringExtra("userID");
        this.context = context;

        setCourseFromUserID(userID);

    }

    private void setCourseFromUserID(String userID) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("enrollment");
        Query query = collectionReference.whereEqualTo("userID",userID);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Log.d("COURSE FROM UID", "Succeed");

                        Random random = new Random();
                        int totalDocuments = queryDocumentSnapshots.size();
                        int counter = 0;
                        int max = 10;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String courseID = documentSnapshot.getString("courseID");
                            if(random.nextInt(10)==0){
                                setRandomizedTopic(courseID);
                            }else if(counter+1==totalDocuments){
                                Log.d("LAST DS CHECK", "Last but not found ");
                                setRandomizedTopic(courseID);
                            }
                            Log.d("COUNTER CHECK", counter+" vs "+totalDocuments);
                            counter++;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("NotificationSchedule", "onFailure: Error Getting Documents Course", e);
                    }
                });


    }

    private void setRandomizedTopic(String courseID){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("course").document(courseID);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CourseModel courseModel = documentSnapshot.toObject(CourseModel.class);

                        Log.d("TOPIC CHECK", courseModel.toString());

                        List<String> topic = courseModel.getTopics();
                        Random random = new Random();
                        int topicIndex = random.nextInt(topic.size());
                        String topicID = topic.get(topicIndex);
                        setRandomizedQuestion(topicID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("NotificationSchedule", "onFailure: Error Getting Documents Topic", e);
                    }
                });
    }



    private void setRandomizedQuestion(String topicID){
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("topics").document(topicID);
        docRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("NotificationSchedule", "onFailure: Error Getting Documents Question", e);
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        QuizManager quizManager = documentSnapshot.toObject(QuizManager.class);

                        if(quizManager!=null) {
                            Log.d("QUIZ MANAGER CHECK", quizManager.toString());

                            Random random = new Random();
                            int questionRandom = random.nextInt(quizManager.getQuestion().size());
                            question = quizManager.getQuestion(questionRandom);

                            Log.d("QUESTION CHECK", question);

                            String choiceA = quizManager.getChoiceA().get(questionRandom);
                            String choiceB = quizManager.getChoiceB().get(questionRandom);
                            String choiceC = quizManager.getChoiceC().get(questionRandom);
                            String choiceD = quizManager.getChoiceD().get(questionRandom);

                            correctAnswer = quizManager.getCorrectAnswer(questionRandom);
                            if (correctAnswer.equals(choiceA)) {
                                correctAnswerIndex = 1;
                            } else if (correctAnswer.equals(choiceB)) {
                                correctAnswerIndex = 2;
                            } else if (correctAnswer.equals(choiceC)) {
                                correctAnswerIndex = 3;
                            } else if (correctAnswer.equals(choiceD)) {
                                correctAnswerIndex = 4;
                            }

                            remoteViewsExpanded.setTextViewText(R.id.questionTV, question);
                            remoteViewsExpanded.setTextViewText(R.id.answer1TV, choiceA);
                            remoteViewsExpanded.setTextViewText(R.id.answer2TV, choiceB);
                            remoteViewsExpanded.setTextViewText(R.id.answer3TV, choiceC);
                            remoteViewsExpanded.setTextViewText(R.id.answer4TV, choiceD);

                            setNotificationIntent();
                        }
                    }
                });
    }

    private void setNotificationIntent(){
        Intent action1Intent = new Intent(context,FectDoNotification.class);
        if(correctAnswerIndex==1){
            action1Intent.setAction("TRUE");
        } else {
            action1Intent.setAction("FALSE");
        }
        PendingIntent action1Pending = PendingIntent.getBroadcast(context,0,action1Intent,PendingIntent.FLAG_IMMUTABLE);

        Intent action2Intent = new Intent(context,FectDoNotification.class);
        if(correctAnswerIndex==2){
            action2Intent.setAction("TRUE");
        } else {
            action2Intent.setAction("FALSE");
        }
        PendingIntent action2Pending = PendingIntent.getBroadcast(context,0,action2Intent,PendingIntent.FLAG_IMMUTABLE);

        Intent action3Intent = new Intent(context,FectDoNotification.class);
        if(correctAnswerIndex==3){
            action3Intent.setAction("TRUE");
        } else {
            action3Intent.setAction("FALSE");
        }
        PendingIntent action3Pending = PendingIntent.getBroadcast(context,0,action3Intent,PendingIntent.FLAG_IMMUTABLE);

        Intent action4Intent = new Intent(context,FectDoNotification.class);
        if(correctAnswerIndex==4){
            action4Intent.setAction("TRUE");
        } else {
            action4Intent.setAction("FALSE");
        }
        PendingIntent action4Pending = PendingIntent.getBroadcast(context,0,action4Intent,PendingIntent.FLAG_IMMUTABLE);

        remoteViewsExpanded.setOnClickPendingIntent(R.id.answer1Btn,action1Pending);
        remoteViewsExpanded.setOnClickPendingIntent(R.id.answer2Btn,action2Pending);
        remoteViewsExpanded.setOnClickPendingIntent(R.id.answer3Btn,action3Pending);
        remoteViewsExpanded.setOnClickPendingIntent(R.id.answer4Btn,action4Pending);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMe")
                .setSmallIcon(R.drawable.icon_logo_fd)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViewsExpanded)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200,builder.build());
        Log.d("SCHEDULER", "onReceive: Last");
    }
}
