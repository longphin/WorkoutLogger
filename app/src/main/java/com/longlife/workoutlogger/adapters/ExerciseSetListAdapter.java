package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.longlife.workoutlogger.R;
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
        /*
        if (weight != null)
            holder.exerciseTypeText.setText(String.valueOf(weight));
        if (reps != null)
            holder.measurementTypeText.setText(String.valueOf(reps));
        */
    }

    @Override
    public int getItemCount() {
        if (sessionExerciseSets == null) return (0);
        return (sessionExerciseSets.size());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private final View.OnClickListener setOnClickListener = new SetOnClickListenerType();
        private final View.OnClickListener scoreOnClickListener = new SetOnClickListenerScore();
        public TextView typeText;
        public TextView scoreText;
        public ViewGroup container;

        public CustomViewHolder(View itemView, Exercise exercise) {
            super(itemView);
            this.container = itemView.findViewById(R.id.root_exerciseSet);

            this.typeText = itemView.findViewById(R.id.text_exerciseSet_type);
            this.scoreText = itemView.findViewById(R.id.text_exerciseSet_score);
            this.typeText.setOnClickListener(setOnClickListener);
            this.scoreText.setOnClickListener(scoreOnClickListener);
        }

        // OnClick listener for the entire set container
        private class SetOnClickListenerType implements View.OnClickListener {
            // [TODO] When this is clicked, set it as the focus of the app and bring up a custom keyboard based on the exercise type.
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();

                // Check if the item still exists in the position. For example, it will fail when the recycler view data is changed.
                if (pos != RecyclerView.NO_POSITION) {
                    Toast.makeText(context, "type click at " + Integer.toString(pos), Toast.LENGTH_SHORT).show();
                }
            }
        }

        private class SetOnClickListenerScore implements View.OnClickListener {
            // [TODO] When this is clicked, set it as the focus of the app and bring up a custom keyboard based on the measurement type.
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();

                // Check if the item still exists in the position. For example, it will fail when the recycler view data is changed.
                if (pos != RecyclerView.NO_POSITION) {
                    Toast.makeText(context, "score click at " + Integer.toString(pos), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
