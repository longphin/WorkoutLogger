package com.longlife.workoutlogger.view.Exercises.PerformExercise;

import android.view.View;
import android.widget.TextView;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsViewHolder;
import com.longlife.workoutlogger.R;

public class PerformRoutineViewHolder
        extends ExercisesWithSetsViewHolder {
    private TextView weightsTextView;
    private TextView repsTextView;

    public PerformRoutineViewHolder(View view) {
        super(view);

        weightsTextView = view.findViewById(R.id.txt_perform_routine_set_weights);
        repsTextView = view.findViewById(R.id.txt_perform_routine_set_reps);
    }

    public TextView getWeightsTextView() {
        return weightsTextView;
    }

    public TextView getRepsTextView() {
        return repsTextView;
    }
}
