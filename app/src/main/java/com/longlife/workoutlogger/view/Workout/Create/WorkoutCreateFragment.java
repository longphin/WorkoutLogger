/*
 * Created by Longphi Nguyen on 12/29/18 1:15 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/29/18 1:15 PM.
 */

package com.longlife.workoutlogger.view.Workout.Create;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragmentBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class WorkoutCreateFragment extends ExercisesListFragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;

    public WorkoutCreateFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new WorkoutCreateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getExercisesRecyclerViewId() {
        return R.id.rv_exercises;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeGroupByOptions(mView);
    }

    @Override
    public void getViewModel() {
        if (viewModel == null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
            workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
        }
    }

    @Override
    protected ExercisesListAdapterBase createAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises) {
        return new ExercisesListAdapter(this, exercises);
    }

    @Override
    protected int getLayoutRoot() {
        return R.id.workout_create_overview_layout;
    }

    private void initializeGroupByOptions(View mView) {
        getViewModel();
        viewModel.loadExercises();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //rv_selectedExercises.setAdapter(null);
        //rv_selectedExercises = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_workout_create, container, false);
        initializeSelectedExercisesViewPager();

        initializeObservers();
        initializeRecyclerView(mView);
        return mView;
    }

    private void initializeSelectedExercisesViewPager() {
        ViewPager viewPager = mView.findViewById(R.id.view_pager);

        RoutinesPagerAdapter routineAdapter = new RoutinesPagerAdapter(getFragmentManager());
        routineAdapter.addRoutine(1L);
        routineAdapter.addRoutine(2L);
        routineAdapter.addRoutine(3L);
        routineAdapter.addRoutine(4L);
        routineAdapter.addRoutine(5L);
        routineAdapter.addRoutine(6L);
        routineAdapter.addRoutine(7L);
        viewPager.setAdapter(routineAdapter);

        // Bind tab slider.
        PagerSlidingTabStrip tabSlider = mView.findViewById(R.id.tabSlider);
        tabSlider.setViewPager(viewPager);
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        workoutViewModel.insertExercise(new RoutineAdapter.exerciseItemInRoutine(idExercise, exerciseName, 1));
    }
}
