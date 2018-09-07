package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class RoutineCreateExerciseSetFragment
        extends Fragment {
    public static final String TAG = RoutineCreateExerciseSetFragment.class.getSimpleName();
    private Exercise exercise;
    private List<SessionExerciseSet> sets;
    private View mView;

    public static RoutineCreateExerciseSetFragment newInstance(Exercise exercise, ArrayList<SessionExerciseSet> sets) {
        RoutineCreateExerciseSetFragment fragment = new RoutineCreateExerciseSetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise", exercise);
        bundle.putParcelableArrayList("exerciseSets", sets);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercise = getArguments().getParcelable("exercise");
        sets = getArguments().getParcelableArrayList("exerciseSets");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_routine_create_exercise_set, container, false);
            TextView exerciseName = mView.findViewById(R.id.txt_routine_create_exercise_set_name);

            if (exercise != null)
                exerciseName.setText(exercise.getName());
        }
        return mView;
    }

}
