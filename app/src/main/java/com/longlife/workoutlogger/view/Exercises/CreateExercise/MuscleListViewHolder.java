/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/22/18 10:16 PM.
 */

package com.longlife.workoutlogger.view.Exercises.CreateExercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

public class MuscleListViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private CheckBox selectionBox;

    MuscleListViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_selectable_muscle_name);
        selectionBox = itemView.findViewById(R.id.ch_selectable_muscle);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    CheckBox getCheckboxView() {
        return selectionBox;
    }
}
