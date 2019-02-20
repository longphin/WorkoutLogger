/*
 * Created by Longphi Nguyen on 2/17/19 12:56 PM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 2/17/19 12:56 PM.
 */

package com.longlife.workoutlogger.view.Workout;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.dataViewModel.WorkoutViewModel;
import com.longlife.workoutlogger.model.Workout.WorkoutProgramShort;
import com.longlife.workoutlogger.view.Workout.Create.WorkoutCreateFragment;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutListFragment extends FragmentBase {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private WorkoutViewModel workoutViewModel;
    private WorkoutListAdapter workoutListAdapter;

    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new WorkoutListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workout_list, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.rv_workoutL_list);
        workoutListAdapter = new WorkoutListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getContext() != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        recyclerView.setAdapter(workoutListAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        workoutViewModel.getWorkoutShortList()
                .subscribe(new SingleObserver<List<WorkoutProgramShort>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<WorkoutProgramShort> workoutPrograms) {
                        workoutListAdapter.setData(workoutPrograms);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
        workoutViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(WorkoutViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        workoutListAdapter = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.workout_search_menu, menu);

        //initializeSearchForExercisesView(menu.findItem(R.id.exercises_list_searchview));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.workout_list_addWorkout:
                startCreateFragment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCreateFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        WorkoutCreateFragment fragment = (WorkoutCreateFragment) manager.findFragmentByTag(WorkoutCreateFragment.TAG);
        if (fragment == null) {
            fragment = WorkoutCreateFragment.newInstance();
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
        /* // [TODO] When adding filter capability for workouts, implement this.
        if (fragment == null) {
            String currentFilter = "";
            if (searchView != null) currentFilter = searchView.getQuery().toString();

            fragment = ExerciseCreateFragment.newInstance(currentFilter);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
        */
    }
}
