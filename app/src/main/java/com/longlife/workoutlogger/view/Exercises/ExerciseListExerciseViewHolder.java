package com.longlife.workoutlogger.view.Exercises;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class ExerciseListExerciseViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;

    public ExerciseListExerciseViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_exerciseName);
    }

    public TextView getNameTextView() {
        return nameTextView;
    }
}
