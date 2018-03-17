package com.longlife.workoutlogger.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.RoutineActivity;

import java.util.List;

/**
 * Created by Longphi on 1/31/2018.
 */

public class ExerciseSetListAdapter extends RecyclerView.Adapter<ExerciseSetListAdapter.CustomViewHolder> {
    private static int FocusedIdSessionExercise; // The Id for the parent session exercise containing the selected view.
    private static int FocusedIdSessionExerciseSet; // The Id for the session exercise set containing the selected view.
    private static int FocusedIdView; // The Id for the selected view in the exercise set.
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

        return (new ExerciseSetListAdapter.CustomViewHolder(v));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        SessionExerciseSet bindingSessionExerciseSet = sessionExerciseSets.get(position);
        Integer weight = bindingSessionExerciseSet.getReps();
        Integer reps = bindingSessionExerciseSet.getWeights();

        // When the "focused" view is out of the screen, it is deleted. So we need to reset the focus
        // when the view comes back onto the screen.
        holder.resetFocusOnCreate(position);
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
        //private final View.OnTouchListener scoreOnTouchListener = new SetOnTouchListenerScore();
        public TextView typeText;
        public EditText scoreText;
        public ViewGroup container;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.root_exerciseSet);

            this.typeText = itemView.findViewById(R.id.text_exerciseSet_type);
            this.scoreText = itemView.findViewById(R.id.text_exerciseSet_score);
            this.typeText.setOnClickListener(setOnClickListener);
            this.scoreText.setOnClickListener(scoreOnClickListener);
            //this.scoreText.setOnTouchListener(scoreOnTouchListener);
        }

        // [TODO] Not working. It is supposed to set the newly binded view if it was the focus.
        private void resetFocusOnCreate(int pos) {
            if (pos != RecyclerView.NO_POSITION
                    && sessionExerciseSets.get(pos).getIdSessionExercise() == FocusedIdSessionExercise
                    && sessionExerciseSets.get(pos).getIdSessionExerciseSet() == FocusedIdSessionExerciseSet) {
                if (this.typeText.getId() == FocusedIdView) {
                    //this.typeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    this.typeText.setTextColor(Color.parseColor("#4256f4"));
                    this.typeText.requestFocus();
                } else if (this.scoreText.getId() == FocusedIdView) {
                    this.scoreText.setTextColor(Color.parseColor("#4256f4"));
                    this.scoreText.requestFocus();
                }
            }
        }

        private void setFocusedSetInfo(int pos, View v) {
            if (pos != RecyclerView.NO_POSITION
                    && (
                    sessionExerciseSets.get(pos).getIdSessionExercise() != FocusedIdSessionExercise
                            || sessionExerciseSets.get(pos).getIdSessionExerciseSet() != FocusedIdSessionExerciseSet
                            || v.getId() != FocusedIdView)
                    ) {
                FocusedIdSessionExercise = sessionExerciseSets.get(pos).getIdSessionExercise();
                FocusedIdSessionExerciseSet = sessionExerciseSets.get(pos).getIdSessionExerciseSet();
                FocusedIdView = v.getId();

                Toast.makeText(context, "current focus SessionExercise: " + Integer.toString(FocusedIdSessionExercise) + ", ExerciseSet: " + Integer.toString(FocusedIdSessionExerciseSet) +
                                ", ViewId: " + Integer.toString(FocusedIdView),
                        Toast.LENGTH_SHORT).show();

                ((TextView) v).setTextColor(Color.parseColor("#4256f4"));

                v.requestFocus();

                // Create keyboard based on the selected view's type
                RoutineActivity ra = (RoutineActivity) context;
                ra.createKeyboardFragment();
            }
        }

        private class SetOnTouchListenerScore implements View.OnTouchListener {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.requestFocus();
                        /*
                        int pos = getAdapterPosition();

                        // Check if the item still exists in the position. For example, it will fail when the recycler view data is changed.
                        if (pos != RecyclerView.NO_POSITION) {
                            //[TODO] This should not set the focus because it switches unnecessarily
                            setFocusedSetInfo(pos, view);
                        }
                        */
                        break;
                }
                return (false);
            }
        }
        // OnClick listener for the entire set container
        private class SetOnClickListenerType implements View.OnClickListener {
            // [TODO] When this is clicked, set it as the focus of the app and bring up a custom keyboard based on the exercise type.
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();

                // Check if the item still exists in the position. For example, it will fail when the recycler view data is changed.
                if (pos != RecyclerView.NO_POSITION) {
                    setFocusedSetInfo(pos, view);
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
                    setFocusedSetInfo(pos, view);
                }
            }
        }
    }
}
