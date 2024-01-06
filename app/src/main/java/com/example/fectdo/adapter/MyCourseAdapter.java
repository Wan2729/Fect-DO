package com.example.fectdo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.course.Activity.VideoPage;
import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;

import java.util.List;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseAdapter.ViewHolder> {
    Context context;
    List<CourseModel> courseList;

    public MyCourseAdapter(Context context) {
        this.context = context;
    }

    public void setCourseList(List<CourseModel> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mycourse_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseModel course = courseList.get(position);

        holder.courseTitle.setText(course.getCourseName());

        holder.icon.setOnClickListener(new View.OnClickListener() {
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
        ImageButton icon;
        TextView courseTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseTitle = itemView.findViewById(R.id.tvCourseName);

            icon = itemView.findViewById(R.id.ibCourseIcon);
        }
    }
}
