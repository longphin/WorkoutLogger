/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/4/18 4:55 PM.
 */

package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsViewHolder;
import com.longlife.workoutlogger.R;

class PerformRoutineViewHolder
        extends ExercisesWithSetsViewHolder {
    private TextView weightsTextView;
    private TextView repsTextView;

    private ImageView startRestView;

    PerformRoutineViewHolder(View view) {
        super(view);

        weightsTextView = view.findViewById(R.id.txt_perform_routine_set_weights);
        repsTextView = view.findViewById(R.id.txt_perform_routine_set_reps);
        startRestView = view.findViewById(R.id.img_perform_routine_startRest);
    }

    TextView getWeightsTextView() {
        return weightsTextView;
    }

    TextView getRepsTextView() {
        return repsTextView;
    }

    ImageView getStartRestView() {
        return startRestView;
    }
}
