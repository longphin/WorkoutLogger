/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Exercises;

import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

import androidx.recyclerview.widget.RecyclerView;

public class ExerciseListExerciseViewHolder extends RecyclerView.ViewHolder implements ExercisesListRemakeAdapter.IViewHolder {
    private TextView nameTextView;
    private TextView moreOptions;

    ExerciseListExerciseViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.txt_exerciseName);
        moreOptions = itemView.findViewById(R.id.txt_exercise_moreOptions);
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    @Override
    public void onDestroy() {
        //nameTextView = null;
    }

    public TextView getMoreOptionsView() {
        return moreOptions;
    }
}
