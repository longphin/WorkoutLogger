/*
 * Created by Longphi Nguyen on 1/5/19 8:35 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/5/19 8:35 AM.
 */

package com.longlife.workoutlogger.view.Exercises;


import android.os.Bundle;
import android.view.View;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ExercisesListFragmentBase extends FragmentBase implements IExerciseListCallbackBase {
    protected ExercisesViewModel viewModel;
    protected ExercisesListAdapterBase adapter;
    //private SearchView searchView;
    //private Spinner groupBySelector;
    protected View mView;
    private RecyclerView recyclerView;
    private boolean needsToLoadData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (adapter != null) {
            adapter = null;
        }

        if (recyclerView != null) {
            recyclerView = null;
        }

        needsToLoadData = true;
        clearDisposables();

        mView = null;
        super.onDestroyView();
    }

    protected void initializeObservers() {
        if (needsToLoadData) {
            needsToLoadData = false;

            viewModel.loadExercises();
        }
    }

    protected void initializeRecyclerView(View v) {
        if (recyclerView == null)
            recyclerView = v.findViewById(getExercisesRecyclerViewId());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        setAdapterForRecyclerView();
    }

    protected abstract int getExercisesRecyclerViewId();

    private void setAdapterForRecyclerView() {
        if (recyclerView != null && adapter != null) {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getViewModel();
        addDisposable(viewModel.getExerciseInsertedObservable().subscribe(this::processExerciseInserted));
        addDisposable(viewModel.getExerciseEditedObservable().subscribe(this::loadExerciseUpdated));
        addDisposable(viewModel.getExerciseRestoreObservable().subscribe(this::restoreExercise));
        addDisposable(viewModel.getExerciseDeletedObservable().subscribe(this::deleteExercise));
        addDisposable(viewModel.getExercisesDataObservable().subscribe(this::loadDataInterface));
    }

    public abstract void getViewModel();

    private void loadExerciseUpdated(ExerciseUpdated exerciseUpdated) {
        if (adapter != null) adapter.exerciseUpdated(exerciseUpdated);
    }

    private void processExerciseInserted(Exercise ex) {
        if (adapter != null)
            adapter.addExercise(new ExerciseShort(ex));
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    protected void loadDataInterface(List<IExerciseListable> exercises) {
        if (adapter == null) {
            adapter = createAdapter(this, exercises);//adapter = new ExercisesListRemakeAdapter(this, exercises);
        } else {
            adapter.resetData(exercises);
        }

        setAdapterForRecyclerView();
    }

    protected abstract ExercisesListAdapterBase createAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises); // [TODO] change ExercisesListAdapterBase.IClickExercise to a base interface instead. That way, whether the adapter is for exercise list or workout create, the number of callback options is limited.

    @Override
    public void exerciseEdit(Long idExercise) {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExerciseEditFragment fragment = (ExerciseEditFragment) manager.findFragmentByTag(ExerciseEditFragment.TAG);
        if (fragment == null) {
            fragment = ExerciseEditFragment.newInstance(idExercise);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    protected abstract int getLayoutRoot();

    private void restoreExercise(ExerciseShort restoredExercise) {
        if (adapter != null && isAdded()) adapter.restoreExercise(restoredExercise);
    }

    private void deleteExercise(ExerciseShort deletedExercise) {
        adapter.deleteExercise(deletedExercise.getIdExercise());
    }
}
