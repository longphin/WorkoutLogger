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
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragmentBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WorkoutCreateFragment extends ExercisesListFragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;
    private RoutinesViewModel routineViewModel;
    private int numberOfTabs = 4;
    private RoutinesPagerAdapter routineAdapter;
    private Long idWorkout;

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
    public void onDestroyView() {
        super.onDestroyView();
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
            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

            workoutViewModel.createNewWorkoutProgram()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new SingleObserver<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Long idWorkoutProgram) {
                            idWorkout = idWorkoutProgram;
                            routineViewModel.insertRoutineForWorkout(idWorkout)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new SingleObserver<Long>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Long idRoutine) {
                                            routineAdded(idRoutine);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_workout_create, container, false);
        initializeSelectedExercisesViewPager();

        initializeObservers();
        initializeRecyclerView(mView);
        return mView;
    }

    private void routineAdded(Long idRoutine) {
        numberOfTabs += 1;
        routineAdapter.addRoutine(idRoutine);
    }

    private void initializeSelectedExercisesViewPager() {
        ViewPager viewPager = mView.findViewById(R.id.view_pager);

        routineAdapter = new RoutinesPagerAdapter(getFragmentManager());
        for (int i = 1; i <= numberOfTabs; i++) {
            routineAdapter.addRoutine(Long.valueOf(i));
        }
        viewPager.setAdapter(routineAdapter);

        // Slider between the tabs.
        TabLayout tabLayout = mView.findViewById(R.id.tabSlider);
        tabLayout.setupWithViewPager(viewPager);

        // Listener for when to add a new routine.
        ImageButton addRoutineButton = mView.findViewById(R.id.btn_addRoutine);
        /*
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfTabs += 1;
                routineAdapter.addRoutine(Long.valueOf(numberOfTabs));
            }
        });
        */
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //workoutViewModel.insertRoutine(idWorkout); // [TODO] insertRoutine(idWorkout) will return the idRoutine that is inserted. When observed, we need to call routineAdded(idRoutine)
            }
        });
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        workoutViewModel.insertExercise(new RoutineAdapter.exerciseItemInRoutine(idExercise, exerciseName, 1));
    }
}
