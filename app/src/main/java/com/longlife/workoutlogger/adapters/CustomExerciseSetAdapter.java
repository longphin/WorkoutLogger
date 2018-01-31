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
 * Created by Longphi on 1/25/2018.
 */
public class CustomExerciseSetAdapter extends RecyclerView.Adapter<CustomExerciseSetAdapter.CustomViewHolder> {
    Context context;
    private List<SessionExerciseSet> sessionExerciseSets;

    public CustomExerciseSetAdapter(Context context, List<SessionExerciseSet> sessionExerciseSets) {
        this.context = context;
        this.sessionExerciseSets = sessionExerciseSets;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_exercise_set, parent, false);
        return (new CustomViewHolder(v));
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

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.weights = (EditText) itemView.findViewById(R.id.editText_exerciseSet_weight);
            this.reps = (EditText) itemView.findViewById(R.id.editText_exerciseSet_rep);
        }
    }
}
