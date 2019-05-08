/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 8:13 PM.
 */

package com.longlife.workoutlogger.view.Exercises;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.PerformExercise.PerformExerciseFragment;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ExercisesListFragment extends ExercisesListFragmentBase implements ExercisesListRemakeAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    public ExercisesListFragment() {
        // Required empty public constructor
    }

    public static ExercisesListFragment newInstance() {
        return new ExercisesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_exercises;
    }

    @Override
    protected int getExercisesRecyclerViewId() {
        return R.id.rv_exercises;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void getViewModel() {
        if (viewModel == null && getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
        }
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    @Override
    protected void loadExercises(List<IExerciseListable> exercises) {
        super.loadExercises(exercises);
    }

    @Override
    protected ExercisesListAdapterBase createAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises, String query) {
        return new ExercisesListRemakeAdapter(this, exercises, query);
    }

    @Override
    protected int getLayoutRoot() {
        return R.id.exercises_overview_layout;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.exercises_search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.exercises_list_addExercise:
                startCreateFragment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCreateFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExerciseCreateFragment fragment = (ExerciseCreateFragment) manager.findFragmentByTag(ExerciseCreateFragment.TAG);
        if (fragment == null) {
            String currentFilter = "";
            if (searchView != null) currentFilter = searchView.getQuery().toString();

            fragment = ExerciseCreateFragment.newInstance(currentFilter);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    @Override
    public void exercisePerform(ExercisesListAdapterBase.exerciseItem ex) {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        PerformExerciseFragment fragment = (PerformExerciseFragment) manager.findFragmentByTag(PerformExerciseFragment.TAG);
        if (fragment == null) {
            fragment = PerformExerciseFragment.newInstance(ex);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    @Override
    public void exerciseDelete(ExerciseShort exerciseToDelete) {
        viewModel.deleteExercise(exerciseToDelete);

        Snackbar snackbar = Snackbar
                .make(mView.findViewById(getLayoutRoot())
                        , exerciseToDelete.getName() + " deleted.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", view -> {
        });
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                ExerciseShort restoredExercise = viewModel.getLastDeletedExercise();
                if (restoredExercise == null)
                    return;

                // If the snackbar was dismissed via clicking the action (Undo button), then restore the exercise.
                if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    viewModel.restoreLastExercise(); // [TODO] When dismissed after changing fragments, the undo is not done?
                    //if (adapter != null && isAdded()) adapter.restoreExercise(restoredExercise);
                    return;
                }
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
