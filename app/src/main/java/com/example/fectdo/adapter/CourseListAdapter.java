package com.example.fectdo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;
import com.example.fectdo.models.EnrollmentModel;
import com.example.fectdo.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    Context context;
    List<CourseModel> courseList;

    public CourseListAdapter(Context context) {
        this.context = context;
    }

    public void setCourseList(List<CourseModel> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.course_list_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModel course = courseList.get(position);
        String userID = FirebaseUtil.currentUserId();
        String courseID = course.getDocumentID();

        holder.courseTitle.setText(course.getCourseName());

        FirebaseUtil.getCollection("enrollment")
                .whereEqualTo("userID", userID)
                .whereEqualTo("courseID", courseID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots.isEmpty()){
                            holder.enrollButton.setText("Enroll ");
                            holder.enrollButton.setEnabled(true);
                        }
                        else {
                            holder.enrollButton.setText("Enrolled");
                        }
                        holder.enrollButton.setVisibility(View.VISIBLE);
                    }
                });

        holder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrollmentModel enroll = new EnrollmentModel(FirebaseUtil.currentUserId(), course.getDocumentID());

                FirebaseUtil.getCollection("enrollment").add(enroll);
                holder.enrollButton.setText("Enrolled");
                holder.enrollButton.setEnabled(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView courseTitle;
        Button enrollButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(com.example.fectdo.R.id.ibCourseIcon);
            courseTitle = itemView.findViewById(com.example.fectdo.R.id.tvCourseName);
            enrollButton = itemView.findViewById(R.id.btnManage);

            enrollButton.setEnabled(false);
            enrollButton.setVisibility(View.INVISIBLE);
        }
    }
}
