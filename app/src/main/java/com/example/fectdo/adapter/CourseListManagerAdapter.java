package com.example.fectdo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.course.Activity.VideoPage;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;

import java.util.List;

public class CourseListManagerAdapter extends RecyclerView.Adapter<CourseListManagerAdapter.ViewHolder> {
    Context context;
    List<CourseModel> courseList;

    public CourseListManagerAdapter(Context context) {
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

        holder.courseTitle.setText(course.getCourseName());

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPage.class);
                intent.putExtra("COURSE_DOCUMENT_REF", course.getDocumentID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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
        Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(com.example.fectdo.R.id.ibCourseIcon);
            courseTitle = itemView.findViewById(com.example.fectdo.R.id.tvCourseName);
            viewButton = itemView.findViewById(R.id.btnManage);

            viewButton.setText("View Course");
        }
    }
}
