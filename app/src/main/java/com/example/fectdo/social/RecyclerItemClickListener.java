package com.example.fectdo.social;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private OnItemClickListener onItemClickListener;
    private RecyclerView recyclerView;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener,RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.onItemClickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    onItemClickListener.onItemDoubleTap(position);
                    return true;
                }
                return false;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemDoubleTap(int position);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // Do nothing here
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // Do nothing here
    }
}
