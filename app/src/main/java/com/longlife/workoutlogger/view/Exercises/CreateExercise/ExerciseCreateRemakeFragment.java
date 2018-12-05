package com.longlife.workoutlogger.view.Exercises.CreateExercise;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.R;

public class ExerciseCreateRemakeFragment extends FragmentBase {
    public static final String TAG = ExerciseCreateRemakeFragment.class.getSimpleName();

    public ExerciseCreateRemakeFragment() {
        // Required empty public constructor
    }

    public static ExerciseCreateRemakeFragment newInstance() {
        return new ExerciseCreateRemakeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_create_remake, container, false);
    }

}
