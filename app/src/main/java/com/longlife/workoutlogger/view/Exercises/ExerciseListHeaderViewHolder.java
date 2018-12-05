package com.longlife.workoutlogger.view.Exercises;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class ExerciseListHeaderViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;

    public ExerciseListHeaderViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_exercises_name);
    }

    public TextView getNameTextView() {
        return nameTextView;
    }
}
