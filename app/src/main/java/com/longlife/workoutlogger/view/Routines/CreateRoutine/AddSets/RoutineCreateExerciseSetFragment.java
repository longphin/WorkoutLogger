/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/24/18 1:46 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddSets;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.ArrayList;
import java.util.List;

public class RoutineCreateExerciseSetFragment
        extends Fragment {
    public static final String TAG = RoutineCreateExerciseSetFragment.class.getSimpleName();
    private Exercise exercise;
    private List<SessionExerciseSet> sets;
    private View mView;
    // Input constants.
    private static final String INPUT_EXERCISE = "exercise";
    private static final String INPUT_EXERCISE_SET = "exerciseSets";

    public static RoutineCreateExerciseSetFragment newInstance(Exercise exercise, ArrayList<SessionExerciseSet> sets) {
        RoutineCreateExerciseSetFragment fragment = new RoutineCreateExerciseSetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RoutineCreateExerciseSetFragment.INPUT_EXERCISE, exercise);
        bundle.putParcelableArrayList(RoutineCreateExerciseSetFragment.INPUT_EXERCISE_SET, sets);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exercise = getArguments().getParcelable(RoutineCreateExerciseSetFragment.INPUT_EXERCISE);
        sets = getArguments().getParcelableArrayList(RoutineCreateExerciseSetFragment.INPUT_EXERCISE_SET);
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
