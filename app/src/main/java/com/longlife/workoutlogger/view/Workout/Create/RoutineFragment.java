/*
 * Created by Longphi Nguyen on 1/27/19 8:43 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/27/19 8:43 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.view.Workout.WorkoutViewModel;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoutineFragment extends FragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    private static final String INPUT_IDROUTINE = "IdRoutine";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private Long idRoutine;
    private WorkoutViewModel workoutViewModel;
    private RoutineAdapter routineAdapter;
    private View mView;

    public static RoutineFragment newInstance(Long idRoutine) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putLong(INPUT_IDROUTINE, idRoutine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getViewModel();
    }

    public void getViewModel() {
        if (workoutViewModel == null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);

            addDisposable(workoutViewModel.getExerciseToInsertObservable().subscribe(this::exerciseInserted));
        }
    }

    private void exerciseInserted(RoutineAdapter.exerciseItemInRoutine exerciseInsertedToRoutine) {
        routineAdapter.addExercise(exerciseInsertedToRoutine);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idRoutine = getArguments().getLong(INPUT_IDROUTINE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_routine, container, false);
        initializeSelectedExercisesRecyclerView();

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        clearDisposables();
    }

    private void initializeSelectedExercisesRecyclerView() {
        routineAdapter = new RoutineAdapter(this);
        RecyclerView rv_selectedExercises = mView.findViewById(R.id.rv_selectedExercises);
        rv_selectedExercises.setAdapter(routineAdapter);
        rv_selectedExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_selectedExercises.setItemAnimator(new DefaultItemAnimator());
        rv_selectedExercises.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        //routineAdapter.addExercise(idExercise, exerciseName);
    }

    @Override
    public void exerciseEdit(Long idExercise) {

    }
}
