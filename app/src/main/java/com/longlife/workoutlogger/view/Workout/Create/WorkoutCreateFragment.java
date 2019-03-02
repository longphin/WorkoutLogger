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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.dataViewModel.WorkoutViewModel;
import com.longlife.workoutlogger.enums.ExerciseListGroupBy;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.model.Routine.Routine;
import com.longlife.workoutlogger.model.Workout.WorkoutProgram;
import com.longlife.workoutlogger.view.Exercises.ExercisesListAdapterBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesListFragmentBase;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.Exercises.IExerciseListCallbackBase;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.observers.DisposableSingleObserver;

public class WorkoutCreateFragment extends ExercisesListFragmentBase implements ExercisesListAdapter.IExerciseListCallback {
    public static final String TAG = WorkoutCreateFragment.class.getSimpleName();
    private static final String SAVEDSTATE_IDWORKOUT = "idWorkout";
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;
    private RoutinesViewModel routineViewModel;
    private RoutinesPagerAdapter routineAdapter;
    private Long idWorkout;
    private boolean routinesInitialized = false;
    private List<Routine> initializedRoutines = new ArrayList<>();

    public WorkoutCreateFragment() {
        // Required empty public constructor
    }

    private static final String INPUT_IDWORKOUT = "idWorkout";

    // Constructor for when a new workout. A new workout entry will be added to the database.
    public static WorkoutCreateFragment newInstance() {
        return newInstance(null);
    }

    // Constructor for a workout. If idWorkout != null, then it will load routines for the existing workout.
    // If idWorkout == null, then it will create one routine for the workout by default.
    public static WorkoutCreateFragment newInstance(@Nullable Long idWorkout) {
        Bundle bundle = new Bundle();
        bundle.putLong(INPUT_IDWORKOUT, idWorkout == null ? -1 : idWorkout);

        WorkoutCreateFragment fragment = new WorkoutCreateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) { // Restore a session from save state
            idWorkout = savedInstanceState.getLong(SAVEDSTATE_IDWORKOUT, -1);
        } else { // Initialize workout.
            if (getArguments() != null) {
                idWorkout = getArguments().getLong(INPUT_IDWORKOUT);
            } else {
                idWorkout = null;
            }
        }
    }

    private ViewPager routineViewPager;

    @Override
    protected int getExercisesRecyclerViewId() {
        return R.id.rv_exercises;
    }

    private Spinner groupBySelector;

    @Override
    public void getViewModel() {
        if (viewModel == null && getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
            workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
            routineViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeWorkoutData(mView);
        initializeGroupByOptions(mView);
    }

    private void initializeWorkoutData(View mView) {
        getViewModel();
        //viewModel.loadExercises();
        if (idWorkout == null || idWorkout == -1) {
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
        } else {
            getRoutinesForWorkout(idWorkout);
        }
    }

    private void initializeGroupByOptions(View v) {
        groupBySelector = v.findViewById(R.id.spinner_workout_exercise_group_by);
        ArrayAdapter<ExerciseListGroupBy.Type> groupByAdapter = new ArrayAdapter<>(getContext(), R.layout.weight_unit_spinner_item, ExerciseListGroupBy.getOptions(getContext()));
        // Specify the layout to use when the list appears.
        groupByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attach the adapter.
        groupBySelector.setAdapter(groupByAdapter);

        groupBySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedGroupBy = ((ExerciseListGroupBy.Type) groupBySelector.getSelectedItem()).getId();
                // When the group by is changed, execute the filter on the new group by.
                if (selectedGroupBy == 0) {
                    viewModel.loadExercises();
                } else if (selectedGroupBy > 0 && selectedGroupBy <= MuscleGroup.getAllMuscleGroupsIds(getContext()).size()) {
                    viewModel.loadExercisesByMuscleGroup(getContext(), selectedGroupBy - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void obtainedNewWorkout(Long idWorkoutProgram) {
        idWorkout = idWorkoutProgram;
        routineViewModel.insertRoutineForWorkout(idWorkout)
                .subscribe(new DisposableSingleObserver<Routine>() {
                    @Override
                    public void onSuccess(Routine routine) {
                        initializedRoutines.add(routine);
                        setRoutineSliderAdapterData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getRoutinesForWorkout(Long idWorkout) {
        routineViewModel.getRoutinesForWorkout(idWorkout)
                .subscribe(new DisposableSingleObserver<List<Routine>>() {
                    @Override
                    public void onSuccess(List<Routine> routines) {
                        initializedRoutines.addAll(routines);
                        setRoutineSliderAdapterData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        routinesInitialized = false;
        mView = null;
        routineAdapter = null;
        initializedRoutines = null;
        routineViewPager = null;

        if (groupBySelector != null) {
            groupBySelector.setAdapter(null);
            groupBySelector = null;
        }

        super.onDestroyView();
    }

    private void initializeSelectedExercisesViewPager() {
        routineViewPager = mView.findViewById(R.id.view_pager);

        routineAdapter = new RoutinesPagerAdapter(getChildFragmentManager());
        setRoutineSliderAdapterData();
        routineViewPager.setAdapter(routineAdapter);

        // Slider between the tabs.
        TabLayout tabLayout = mView.findViewById(R.id.tabSlider);
        tabLayout.setupWithViewPager(routineViewPager);

        // Listener for when to add a new routine.
        Button addRoutineButton = mView.findViewById(R.id.btn_addRoutine);
        addRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routineViewModel.insertRoutineForWorkout(idWorkout)
                        .subscribe(new DisposableSingleObserver<Routine>() {
                            @Override
                            public void onSuccess(Routine routine) {
                                addRoutineToSliderAdapterData(routine);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        });
    }

    private void obtainedExistingWorkout(WorkoutProgram workoutProgram) {
        idWorkout = workoutProgram.getIdWorkout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_workout_create, container, false);
        groupBySelector = mView.findViewById(R.id.spinner_workout_exercise_group_by);
        initializeSelectedExercisesViewPager();

        //initializeObservers();
        initializeRecyclerView(mView);
        return mView;
    }

/*    private ViewPager routineViewPager;
    private TabLayout tabLayout;
    private Button addRoutineButton;*/

    // Will check if the routineAdapter has been initialized and if the routine data has been initialized.
    // If they are, then add the routine id's to the routineAdapter.
    private void setRoutineSliderAdapterData() {
        if (!routinesInitialized && routineAdapter != null) {
            if (initializedRoutines == null) initializedRoutines = new ArrayList<>();
            if (!initializedRoutines.isEmpty()) {
                routineAdapter.addRoutines(initializedRoutines);

                initializedRoutines.clear();
                routinesInitialized = true;
            }
        }
    }

    private void addRoutineToSliderAdapterData(Routine routine) {
        routineAdapter.addRoutine(routine);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVEDSTATE_IDWORKOUT, idWorkout);
    }

    @Override
    public void addExerciseToRoutine(Long idExercise, String exerciseName) {
        workoutViewModel.insertExerciseForRoutine(new RoutineAdapter.exerciseItemInRoutine(routineAdapter.getRoutineId(routineViewPager.getCurrentItem()), idExercise, exerciseName, 1));
    }
}
