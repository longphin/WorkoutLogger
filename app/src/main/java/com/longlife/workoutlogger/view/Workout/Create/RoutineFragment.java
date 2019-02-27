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
import com.longlife.workoutlogger.dataViewModel.WorkoutViewModel;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

// This fragment displays the list of exercises used in a specific routine.
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

    private List<RoutineAdapter.exerciseItemInRoutine> exercisesToAddIntoRoutine = new ArrayList<>();
    public void getViewModel() {
        if (workoutViewModel == null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
            RoutinesViewModel routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

            // Observe when an exercise is to be added to the routine.
            addDisposable(workoutViewModel.getExerciseToInsertObservable().subscribe(this::exerciseInserted));
            if (idRoutine != null)
                routineViewModel.getExercisesShortForRoutine(idRoutine)
                        .subscribe(new SingleObserver<List<RoutineAdapter.exerciseItemInRoutine>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<RoutineAdapter.exerciseItemInRoutine> exercisesToAdd) {
                                includeExercisesToRoutine(exercisesToAdd);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            // [TODO] Add observable for when this routine is restored. The observable calls query to grab all RoutineExercise where idRoutine=:idRoutine
        }
    }

    // Exercise is inserted into the routine.
    private void exerciseInserted(RoutineAdapter.exerciseItemInRoutine exerciseToAdd) {
        if (exerciseToAdd.getIdRoutine().equals(idRoutine)) {
            //routineAdapter.addExercise(exerciseInsertedToRoutine);
            exercisesToAddIntoRoutine.add(exerciseToAdd);
            addExercisesToRoutine();
        }
    }

    private void includeExercisesToRoutine(List<RoutineAdapter.exerciseItemInRoutine> exercisesToAdd) {
        if (exercisesToAddIntoRoutine == null) exercisesToAddIntoRoutine = new ArrayList<>();
        exercisesToAddIntoRoutine.addAll(exercisesToAdd);
        addExercisesToRoutine();
    }

    // If exercisesToAddIntoRoutine has any items, then add the items into the routine.
    private void addExercisesToRoutine() {
        if (isAdded() && routineAdapter != null && exercisesToAddIntoRoutine != null && !exercisesToAddIntoRoutine.isEmpty()) {
            routineAdapter.addExercises(exercisesToAddIntoRoutine);
            exercisesToAddIntoRoutine.clear();
        }
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
        routineAdapter = null;
        mView = null;
    }

    // Initialize recyclerview.
    private void initializeSelectedExercisesRecyclerView() {
        routineAdapter = new RoutineAdapter(this);
        RecyclerView rv_selectedExercises = mView.findViewById(R.id.rv_selectedExercises);
        rv_selectedExercises.setAdapter(routineAdapter);
        rv_selectedExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_selectedExercises.setItemAnimator(new DefaultItemAnimator());
        if (getContext() != null)
            rv_selectedExercises.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // If the exercises for this routine were loaded, then add them to the recyclerview.
        addExercisesToRoutine();
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        //routineAdapter.addExercise(idExercise, exerciseName);
    }

    @Override
    public void exerciseEdit(Long idExercise) {

    }
}
