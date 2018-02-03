package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.List;

/**
 * Created by Longphi on 1/31/2018.
 */

public class ExerciseSetListAdapter extends RecyclerView.Adapter<ExerciseSetListAdapter.CustomViewHolder> {
    private Context context;
    private List<SessionExerciseSet> sessionExerciseSets;
    private Exercise exercise;

    public ExerciseSetListAdapter(Context context, List<SessionExerciseSet> sessionExerciseSets, Exercise exercise) {
        this.context = context;
        this.sessionExerciseSets = sessionExerciseSets;
        this.exercise = exercise;
    }

    @Override
    public ExerciseSetListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_exercise_set, parent, false);
        return (new ExerciseSetListAdapter.CustomViewHolder(v, this.exercise));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        SessionExerciseSet bindingSessionExerciseSet = sessionExerciseSets.get(position);
        Integer weight = bindingSessionExerciseSet.getReps();
        Integer reps = bindingSessionExerciseSet.getWeights();
        if (weight != null)
            holder.exerciseTypeText.setText(String.valueOf(weight));
        if (reps != null)
            holder.measurementTypeText.setText(String.valueOf(reps));
    }

    @Override
    public int getItemCount() {
        if (sessionExerciseSets == null) return (0);
        return (sessionExerciseSets.size());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public EditText exerciseTypeText;
        public EditText measurementTypeText;
        public ViewGroup container;

        public CustomViewHolder(View itemView, Exercise exercise) {
            super(itemView);
            this.container = itemView.findViewById(R.id.root_exerciseSet);
            this.exerciseTypeText = itemView.findViewById(R.id.editText_exerciseSet_exerciseType);
            this.measurementTypeText = itemView.findViewById(R.id.editText_exerciseSet_measurementType);

            ExerciseRequestCode.ExerciseType exerciseType = exercise.getExerciseType();
            /*
            if(exerciseType == ExerciseRequestCode.ExerciseType.BODYWEIGHT
                    || exerciseType == ExerciseRequestCode.ExerciseType.WEIGHT)
            {
                exerciseTypeText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }else if (exerciseType == ExerciseRequestCode.ExerciseType.DISTANCE)
            {
                exerciseTypeText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            */
            exerciseTypeText.setInputType(ExerciseRequestCode.getExerciseTypeInputType(exerciseType));

            ExerciseRequestCode.MeasurementType measurementType = exercise.getMeasurementType();
            measurementTypeText.setInputType(ExerciseRequestCode.getMeasurementTypeInputType(measurementType));
        }
    }
}
