package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class MuscleListViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;

    public MuscleListViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_selectable_muscle_name);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }
}
