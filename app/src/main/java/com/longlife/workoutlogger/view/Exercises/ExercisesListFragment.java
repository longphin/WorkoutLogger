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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseListGroupBy;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.PerformExercise.PerformExerciseFragment;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class ExercisesListFragment extends ExercisesListFragmentBase implements ExercisesListRemakeAdapter.IExerciseListCallback {
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private SearchView searchView;
    private Spinner groupBySelector;

    public ExercisesListFragment() {
        // Required empty public constructor
    }

    public static ExercisesListFragment newInstance() {
        return new ExercisesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializeObservers();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_exercises, container, false);

        initializeObservers();
        initializeRecyclerView(mView);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (searchView != null) {
            searchView.setOnQueryTextListener(null);
            searchView = null;
        }

        if (groupBySelector != null) {
            groupBySelector.setAdapter(null);
            groupBySelector = null;
        }

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

    private void initializeGroupByOptions(View v) {
        groupBySelector = v.findViewById(R.id.spinner_exercises_group_by);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.exercises_search_menu, menu);

        initializeSearchForExercisesView(menu.findItem(R.id.exercises_list_searchview));
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

    @Override
    public void getViewModel() {
        if (viewModel == null && getActivity() != null) {
            ((MyApplication) getActivity().getApplication())
                    .getApplicationComponent()
                    .inject(this);
            viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
        }
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

    private void initializeSearchForExercisesView(MenuItem searchForExerciseItem) {
        searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());

        searchForExerciseItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchForExerciseItem.setActionView(searchView);
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
                    adapter.filterData(newText); //[TODO] Why is this not working when deleting characters from the filter? It seems to trigger, but the adapter.originalData is saving the filtered data?
                    return true;
                }
                return false;
            }
        });
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    @Override
    protected void loadDataInterface(List<IExerciseListable> exercises) {
        super.loadDataInterface(exercises);

        if (searchView != null && adapter != null) {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty())
                adapter.filterData(query);
        }
    }

    @Override
    protected ExercisesListAdapterBase createAdapter(IExerciseListCallbackBase callback, List<IExerciseListable> exercises) {
        return new ExercisesListRemakeAdapter(this, exercises);
    }

    @Override
    protected int getLayoutRoot() {
        return R.id.exercises_overview_layout;
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
