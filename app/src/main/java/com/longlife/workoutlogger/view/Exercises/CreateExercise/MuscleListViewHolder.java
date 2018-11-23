package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class MuscleListViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private CheckBox selectionBox;

    public MuscleListViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_selectable_muscle_name);
        selectionBox = itemView.findViewById(R.id.ch_selectable_muscle);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    public CheckBox getCheckboxView() {
        return selectionBox;
    }
}
