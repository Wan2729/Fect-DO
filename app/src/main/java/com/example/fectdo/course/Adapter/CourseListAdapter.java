package com.example.fectdo.course.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fectdo.models.CourseModel;
import com.example.fectdo.R;

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
        CourseModel couse = courseList.get(position);

        holder.courseTitle.setText(couse.getCourseName());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView courseTitle;
        Button manageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(com.example.fectdo.R.id.ibCourseIcon);
            courseTitle = itemView.findViewById(com.example.fectdo.R.id.tvCourseName);
            manageButton = itemView.findViewById(R.id.btnManage);

            manageButton.setText("Enroll");
            manageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageButton.setText("Enrolled");
                }
            });
        }
    }
}
