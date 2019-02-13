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
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.WorkoutProgram;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragmentBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class WorkoutCreateFragment extends ExercisesListFragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;
    private RoutinesViewModel routineViewModel;
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

    private Long idFirstRoutine;
    private boolean firstRoutineWasCreated = false;

    private boolean routinesInitialized = false;
    private List<Routine> initializedRoutines = new ArrayList<>();

    @Override
    public void getViewModel() {
        if (viewModel == null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
            workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

            // [TODO] Need to check if a non-saved workout exists. Then we would need to load the workout. Otherwise, create a new workout with a single routine by default.
            workoutViewModel.getFirstUnsavedWorkout()
                    .subscribe(new DisposableMaybeObserver<WorkoutProgram>() {
                                   @Override
                                   public void onSuccess(WorkoutProgram workoutProgram) {
                                       obtainedExistingWorkout(workoutProgram);
                                   }

                                   @Override
                                   public void onError(Throwable e) {

                                   }

                                   @Override
                                   public void onComplete() {
                                       workoutViewModel.createNewWorkoutProgram()
                                               .subscribe(new DisposableSingleObserver<Long>() {
                                                   @Override
                                                   public void onSuccess(Long idWorkoutProgram) {
                                                       obtainedNewWorkout(idWorkoutProgram);
                                                   }

                                                   @Override
                                                   public void onError(Throwable e) {

                                                   }
                                               });
                                   }
                               }
                    );

/*            workoutViewModel.createNewWorkoutProgram()
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
                                            idFirstRoutine = idRoutine;
                                            firstRoutineCreated(idRoutine); // [TODO] Do not allow user to interact with fragment until this is completed.
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });*/
        }
    }

    private void obtainedExistingWorkout(WorkoutProgram workoutProgram) {
        idWorkout = workoutProgram.getIdWorkoutProgram();
    }

    private void obtainedNewWorkout(Long idWorkoutProgram) {
        idWorkout = idWorkoutProgram;
        routineViewModel.insertRoutineForWorkout(idWorkout)
                .subscribe(new DisposableSingleObserver<Routine>() {
                    @Override
                    public void onSuccess(Routine routine) {
                        //idFirstRoutine = routine.getIdRoutine();
                        //firstRoutineCreated(idFirstRoutine);
                        initializedRoutines.add(routine);
                        setRoutineSliderAdapterData();
                        // [TODO] We instead need a function that accepts List<Routine>. That way, when an existing workout is loaded, it can load all of its associated routines as well. Then, the viewpager will create a tab per routine.
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
/*        routineViewModel.insertRoutineForWorkout(idWorkout)
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long idRoutine) {
                        idFirstRoutine = idRoutine;
                        firstRoutineCreated(idRoutine); // [TODO] Do not allow user to interact with fragment until this is completed.
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });*/
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

/*    private void firstRoutineCreated(Long idRoutine) {
        if (!firstRoutineWasCreated && idRoutine != null) {
            firstRoutineWasCreated = true;
            routineAdded(idRoutine);
        }
    }*/

    // Will check if the routineAdapter has been initialized and if the routine data has been initialized.
    // If they are, then add the routine id's to the routineAdapter.
    private void setRoutineSliderAdapterData() {
        if (!routinesInitialized && routineAdapter != null) {
            if (!initializedRoutines.isEmpty()) {
                routineAdapter.addRoutines(initializedRoutines);

                routinesInitialized = true;
            }
        }
    }

    private void initializeSelectedExercisesViewPager() {
        ViewPager viewPager = mView.findViewById(R.id.view_pager);

        routineAdapter = new RoutinesPagerAdapter(getFragmentManager());
        //firstRoutineCreated(idFirstRoutine);
        // [TODO] if initializedRoutinesFlag = true, then add the initializedRoutines list to the routineAdapter.
        setRoutineSliderAdapterData();
        viewPager.setAdapter(routineAdapter);

        // Slider between the tabs.
        TabLayout tabLayout = mView.findViewById(R.id.tabSlider);
        tabLayout.setupWithViewPager(viewPager);

        // Listener for when to add a new routine.
        Button addRoutineButton = mView.findViewById(R.id.btn_addRoutine);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //workoutViewModel.insertRoutine(idWorkout); // [TODO] insertRoutine(idWorkout) will return the idRoutine that is inserted. When observed, we need to call routineAdded(idRoutine)
                routineViewModel.insertRoutineForWorkout(idWorkout)
                        .subscribe(new DisposableSingleObserver<Routine>() {
                            @Override
                            public void onSuccess(Routine routine) {
                                //idFirstRoutine = routine.getIdRoutine();
                                //firstRoutineCreated(idFirstRoutine);
                                //initializedRoutines.add(routine);
                                //setRoutineSliderAdapterData();
                                addRoutineToSliderAdapterData(routine);
                                // [TODO] We instead need a function that accepts List<Routine>. That way, when an existing workout is loaded, it can load all of its associated routines as well. Then, the viewpager will create a tab per routine.
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        });
    }

    private void addRoutineToSliderAdapterData(Routine routine) {
        routineAdapter.addRoutine(routine);
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        workoutViewModel.insertExercise(new RoutineAdapter.exerciseItemInRoutine(idExercise, exerciseName, 1));
    }
}
