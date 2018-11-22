package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class MuscleGroupListViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;

    public MuscleGroupListViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_muscle_group_name);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }
}
