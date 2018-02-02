package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.List;

/**
 * Created by Longphi on 1/31/2018.
 */

public class ExerciseSetListAdapter extends RecyclerView.Adapter<ExerciseSetListAdapter.CustomViewHolder> {

    Context context;
    private List<SessionExerciseSet> sessionExerciseSets;

    public ExerciseSetListAdapter(Context context, List<SessionExerciseSet> sessionExerciseSets) {
        this.context = context;
        this.sessionExerciseSets = sessionExerciseSets;
    }

    @Override
    public ExerciseSetListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_exercise_set, parent, false);
        return (new ExerciseSetListAdapter.CustomViewHolder(v));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        SessionExerciseSet bindingSessionExerciseSet = sessionExerciseSets.get(position);
        Integer weight = bindingSessionExerciseSet.getReps();
        Integer reps = bindingSessionExerciseSet.getWeights();
        if (weight != null)
            holder.weights.setText(String.valueOf(weight));
        if (reps != null)
            holder.reps.setText(String.valueOf(reps));
    }

    @Override
    public int getItemCount() {
        if (sessionExerciseSets == null) return (0);
        return (sessionExerciseSets.size());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public EditText weights;
        public EditText reps;
        public ViewGroup container;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.container = (ViewGroup) itemView.findViewById(R.id.root_exerciseSet);
            this.weights = (EditText) itemView.findViewById(R.id.editText_exerciseSet_weight);
            this.reps = (EditText) itemView.findViewById(R.id.editText_exerciseSet_rep);
        }
    }
}
