/*
 * Created by Longphi Nguyen on 1/5/19 8:35 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 1/5/19 8:35 AM.
 */

package com.longlife.workoutlogger.view.Exercises;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.SpinnerInteractionListener;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseListGroupBy;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ExercisesListFragmentBase extends FragmentBase implements IExerciseListCallbackBase {
    private static final String SAVEDSTATE_groupBySelection = "initialGroupBySelection";
    protected ExercisesViewModel viewModel;
    protected ExercisesListAdapterBase adapter;
    protected View mView;
    protected SearchView searchView;
    protected Spinner groupBySelector;
    private RecyclerView recyclerView;
    private int initialGroupBySelection = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            initialGroupBySelection = savedInstanceState.getInt(SAVEDSTATE_groupBySelection, 0);
        }
    }

    private boolean exerciseOptionHasBeenInitialized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(getViewLayout(), container, false);

        //initializeObservers();
        initializeRecyclerView();
        initializeSearchExercises();
        initializeExerciseOptionsSpinner();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeExerciseOptionsSpinner() {
        groupBySelector = mView.findViewById(R.id.spinner_exercises_group_by);
        if (getContext() != null) {
            ArrayAdapter<ExerciseListGroupBy.Type> groupByAdapter = new ArrayAdapter<>(getContext(), R.layout.weight_unit_spinner_item, ExerciseListGroupBy.getOptions(getContext()));
            // Specify the layout to use when the list appears.
            groupByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Attach the adapter.
            groupBySelector.setAdapter(groupByAdapter);
        }
    }

    // This is the fragment layout resource.
    protected abstract int getViewLayout();

    private void initializeRecyclerView() {
        if (recyclerView == null)
            recyclerView = mView.findViewById(getExercisesRecyclerViewId());

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        setAdapterForRecyclerView();
    }

    private void initializeSearchExercises() {
        if (searchView == null) {
            searchView = mView.findViewById(R.id.exercises_search_exercises);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (adapter != null) {
                        adapter.filterData(query);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) {
                        adapter.filterData(newText);
                        return true;
                    }
                    return false;
                    /* // [TODO] - If we want to add a delay to the search, use a handler instead.
                    searchHandler.removeCallbacksAndMessages(null);
                    String searchText = newText;
                    searchHandler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {
                            if (adapter != null) {
                                adapter.filterData(searchText);
                            }
                        }
                    }, 400);
                    return true;*/
                }
            });
        }
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
        addDisposable(viewModel.getExercisesDataObservable().subscribe(this::loadExercises));

        initializeGroupByOptions(mView);
    }

    public abstract void getViewModel();

    @Override
    public void onDestroyView() {
        if (adapter != null) {
            adapter = null;
        }

        if (recyclerView != null) {
            recyclerView = null;
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(null);
            searchView = null;
        }

        if (groupBySelector != null) {
            groupBySelector.setAdapter(null);
            groupBySelector.setOnItemSelectedListener(null);
            groupBySelector = null;
        }
        exerciseOptionHasBeenInitialized = false;

        clearDisposables();

        mView = null;
        super.onDestroyView();
    }

    private void initializeGroupByOptions(View v) {
        if (!exerciseOptionHasBeenInitialized && groupBySelector != null && getContext() != null) {
            SpinnerInteractionListener selectionListener = new SpinnerInteractionListener() {
                @Override
                public void onItemSelectedFunction(AdapterView<?> parent, View view, int pos, long id) {
                    int selectedGroupBy = ((ExerciseListGroupBy.Type) groupBySelector.getSelectedItem()).getId();
                    // When the group by is changed, execute the filter on the new group by.
                    if (selectedGroupBy == 0) {
                        viewModel.loadExercises();
                    } else if (selectedGroupBy > 0 && selectedGroupBy <= MuscleGroup.getAllMuscleGroupsIds(getContext()).size()) {
                        viewModel.loadExercisesByMuscleGroup(getContext(), selectedGroupBy - 1);
                    }
                }
            };
            groupBySelector.setOnItemSelectedListener(selectionListener); // Need this to trigger when the spinner item is chosen.
            groupBySelector.setOnTouchListener(selectionListener); // Need this to prevent the fragment from only triggering when user interacts with listener or on first load.

            groupBySelector.setSelection(initialGroupBySelection, false);
            exerciseOptionHasBeenInitialized = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (groupBySelector != null) {
            outState.putLong(SAVEDSTATE_groupBySelection, ((ExerciseListGroupBy.Type) groupBySelector.getSelectedItem()).getId());
        }
    }

    private void loadExerciseUpdated(ExerciseUpdated exerciseUpdated) {
        if (adapter != null) adapter.exerciseUpdated(exerciseUpdated);
    }

    private void processExerciseInserted(Exercise ex) {
        if (adapter != null)
            adapter.addExercise(new ExerciseShort(ex));
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    protected void loadExercises(List<IExerciseListable> exercises) {
        if (isAdded()) {
            if (adapter == null) {
                adapter = createAdapter(this, exercises);//adapter = new ExercisesListRemakeAdapter(this, exercises);
            } else {
                adapter.resetData(exercises);
            }
        }

        if (isAdded() && searchView != null && adapter != null) {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty())
                adapter.filterData(query);
        }

        if (isAdded()) {
            setAdapterForRecyclerView();
        }
    }

    protected abstract ExercisesListAdapterBase createAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises); // [TODO] change ExercisesListAdapterBase.IClickExercise to a base interface instead. That way, whether the adapter is for exercise list or workout create, the number of callback options is limited.

    @Override
    public void exerciseEdit(Long idExercise) {
        if (getActivity() != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();

            ExerciseEditFragment fragment = (ExerciseEditFragment) manager.findFragmentByTag(ExerciseEditFragment.TAG);
            if (fragment == null) {
                fragment = ExerciseEditFragment.newInstance(idExercise);
            }

            if (fragmentNavigation != null) {
                fragmentNavigation.pushFragment(fragment);
            }
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
