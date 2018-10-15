package com.longlife.workoutlogger.view.Exercises;

import android.content.Context;

public class ExercisesAdapter extends ExercisesListAdapter {
    public ExercisesAdapter(Context context, IClickExercise exerciseClickCallback) {
        super(context, exerciseClickCallback);
    }

    @Override
    protected void bindMyViewHolder(ExercisesViewHolder holder, int pos) {
    }
}
