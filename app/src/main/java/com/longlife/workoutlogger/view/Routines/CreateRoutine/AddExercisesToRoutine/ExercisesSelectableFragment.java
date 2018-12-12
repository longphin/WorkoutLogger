/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.Exercises.ExercisesFragment;
import com.longlife.workoutlogger.view.MainActivity;

public class ExercisesSelectableFragment
        extends ExercisesFragment
        implements ExercisesSelectableAdapter.IExercisesSelectableAdapterCallback {
    public static final String TAG = ExercisesFragment.class.getSimpleName(); // The tag will be the same as the base class. //ExercisesSelectableFragment.class.getSimpleName();
    private ExercisesSelectableViewModel exercisesSelectableViewModel;

    public static ExercisesSelectableFragment newInstance(int activityRoot, int exerciseItemLayout) {
        ExercisesSelectableFragment fragment = new ExercisesSelectableFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ExercisesFragment.INPUT_ACTIVITY_ROOT, activityRoot);
        bundle.putInt(ExercisesFragment.INPUT_EXERCISE_ITEM_LAYOUT, exerciseItemLayout);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeAdapter();

        exercisesSelectableViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesSelectableViewModel.class);
    }

    @Override
    protected void initializeAdapter() {
        if (adapter == null)
            adapter = new ExercisesSelectableAdapter(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Button addExercisesToRoutineButton = mView.findViewById(R.id.btn_routine_exercises_addExercisesToRoutine);
        addExercisesToRoutineButton.setOnClickListener(
                view ->
                {
                    exercisesSelectableViewModel.addExercisesToRoutine();
                    exercisesSelectableViewModel.clearIdSelectedExercises(); // Clear selected exercises, which is possibly when the selected exercises are clicked again.
                    getActivity().onBackPressed();
                }
        );

        Button clearSelectionButton = mView.findViewById(R.id.btn_routine_exercises_clear);
        clearSelectionButton.setOnClickListener(
                view ->
                {
                    exercisesSelectableViewModel.clearIdSelectedExercises();
                    adapter.notifyDataSetChanged();
                }
        );

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_RoutineCreate));
        return mView;
    }

    @Override
    public boolean isIdSelected(Long idExercise) {
        return exercisesSelectableViewModel.isIdSelected(idExercise);
    }

    @Override
    public void removeSelectedExcercise(Long idExercise) {
        exercisesSelectableViewModel.removeSelectedExercise(idExercise);
    }

    @Override
    public void addSelectedExercise(Long idExercise) {
        exercisesSelectableViewModel.addSelectedExercise(idExercise);
    }
}
