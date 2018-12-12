/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

class ExerciseListExerciseViewHolder extends RecyclerView.ViewHolder {
    private TextView nameTextView;

    ExerciseListExerciseViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_exerciseName);
    }

    TextView getNameTextView() {
        return nameTextView;
    }
}
