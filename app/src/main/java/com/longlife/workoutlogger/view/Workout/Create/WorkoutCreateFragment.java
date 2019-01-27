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

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragmentBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class WorkoutCreateFragment extends ExercisesListFragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private RoutineAdapter routineAdapter;
    //private RecyclerView rv_selectedExercises; //[TODO - potential optimization] Does this need to be saved into a variable? Try making it local only.

    public WorkoutCreateFragment() {
        // Required empty public constructor
    }

    private SelectedExercisesAdapter routineHeaderAdapter;

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

        routineAdapter = null;
        //rv_selectedExercises.setAdapter(null);
        //rv_selectedExercises = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_workout_create, container, false);
        initializeSelectedExercisesViewPager();
        initializeSelectedExercisesRecyclerView();

        initializeObservers();
        initializeRecyclerView(mView);
        return mView;
    }

    private void initializeSelectedExercisesViewPager() {
        ViewPager viewPager = mView.findViewById(R.id.view_pager);

        routineHeaderAdapter = new SelectedExercisesAdapter();
        routineHeaderAdapter.addExercise(1L, "test1");
        routineHeaderAdapter.addExercise(2L, "test2");
        routineHeaderAdapter.addExercise(3L, "test3");
        viewPager.setAdapter(routineHeaderAdapter);

        RecyclerTabLayout selectedExercisesLayout = mView.findViewById(R.id.rvtab_selectedExercises);
        selectedExercisesLayout.setUpWithViewPager(viewPager);
    }

/*    private void initializeSelectedExercisesViewPager() {
        ViewPager viewPager = mView.findViewById(R.id.view_pager);

        *//*SelectedExercisesAdapter adapter = new SelectedExercisesAdapter();
        adapter.addExercise(1L, "test1");
        adapter.addExercise(2L, "test2");
        viewPager.setAdapter(adapter);*//*

        RoutineHeaderAdapter adapter = new RoutineHeaderAdapter(viewPager);
        adapter.addHeader(new RoutineHeaderAdapter.headerObject(1L, "test1"));
        adapter.addHeader(new RoutineHeaderAdapter.headerObject(2L, "test2"));
        adapter.addHeader(new RoutineHeaderAdapter.headerObject(3L, "test3"));
        viewPager.setAdapter(adapter);

        RecyclerTabLayout selectedExercisesLayout = mView.findViewById(R.id.rvtab_selectedExercises);
        selectedExercisesLayout.setUpWithAdapter(adapter);
    }*/

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
        routineAdapter.addExercise(idExercise, exerciseName);
        routineHeaderAdapter.addExercise(idExercise, exerciseName);
    }
}
