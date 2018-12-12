/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 8:13 PM.
 */

package com.longlife.workoutlogger.view.Exercises;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseListGroupBy;
import com.longlife.workoutlogger.enums.Muscle;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseWithMuscleGroup;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ExercisesListFragment extends FragmentBase {
    private static final String TAG = ExercisesListFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesViewModel viewModel;
    private RecyclerView recyclerView;
    private ExercisesListRemakeAdapter adapter;
    private boolean needsToLoadData = false;
    private SearchView searchView;

    public ExercisesListFragment() {
        // Required empty public constructor
    }

    private Spinner groupBySelector;



    /* [TODO] if we want to add more items to action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    public static ExercisesListFragment newInstance() {
        return new ExercisesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
        addDisposable(viewModel.getExerciseListObservable().subscribe(this::loadData));
        addDisposable(viewModel.getExerciseInsertedObservable().subscribe(this::processExerciseInserted));
        addDisposable(viewModel.getExerciseListByMuscleObservable().subscribe(this::loadDataWithMuscles));

        //initializeObservers();
        setHasOptionsMenu(true);
    }

    private void processExerciseInserted(Exercise ex) {
        if (adapter != null)
            adapter.addExercise(new ExerciseShort(ex));
    }

    private void initializeObservers() {
        if (needsToLoadData) {
            needsToLoadData = false;

            viewModel.loadExercises();
        }
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    private void loadData(List<ExerciseShort> exercises) {
        if (adapter == null) {
            adapter = new ExercisesListRemakeAdapter(convertExerciseToInferface(exercises));
        } else {
            adapter.resetData(convertExerciseToInferface(exercises));
        }
        Log.d(TAG, String.valueOf(exercises.size()) + " exercises obtained");

        setAdapterForRecyclerView();
        if (searchView != null && adapter != null) {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty())
                adapter.filter(query);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(getLayoutId(), container, false);

        FloatingActionButton btn_addExercise = v.findViewById(R.id.btn_addExercise);
        btn_addExercise.setOnClickListener(view -> startCreateFragment());

        initializeObservers();
        initializeRecyclerView(v);
        initializeGroupByOptions(v);
        return v;
    }

    // Data was loaded as a list of exercises grouped by muscles.
    private void loadDataWithMuscles(List<ExerciseWithMuscleGroup> exercises) {
        if (adapter == null) {
            adapter = new ExercisesListRemakeAdapter(convertExerciseWithMusclesToInferface(exercises));
        } else {
            adapter.resetData(convertExerciseWithMusclesToInferface(exercises));
        }
        Log.d(TAG, String.valueOf(exercises.size()) + " exercises obtained");

        setAdapterForRecyclerView();
        if (searchView != null && adapter != null) {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty())
                adapter.filter(query);
        }
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

        if (searchView != null) {
            searchView.setOnQueryTextListener(null);
            searchView.setOnClickListener(null);
            searchView = null;
        }

        if (groupBySelector != null) {
            groupBySelector.setAdapter(null);
            groupBySelector = null;
        }

        needsToLoadData = true;
        //clearDisposables();

        super.onDestroyView();
    }

    protected int getLayoutId() {
        return R.layout.fragment_exercises;
    }

    protected void startCreateFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExerciseCreateFragment fragment = (ExerciseCreateFragment) manager.findFragmentByTag(ExerciseCreateFragment.TAG);
        if (fragment == null) {
            fragment = ExerciseCreateFragment.newInstance();
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    private void initializeRecyclerView(View v) {
        if (recyclerView == null)
            recyclerView = v.findViewById(R.id.rv_exercises);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        setAdapterForRecyclerView();
    }

    private void setAdapterForRecyclerView() {
        if (recyclerView != null && adapter != null) {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    // Convert a list of exercises to a list of the underlying interface.
    private List<IExerciseListable> convertExerciseToInferface(List<ExerciseShort> exercises) {
        return new ArrayList<>(exercises);
    }

    // Convert a list of exercises with muscles to a list of the underlying interface.
    private List<IExerciseListable> convertExerciseWithMusclesToInferface(List<ExerciseWithMuscleGroup> exercises) {
        List<IExerciseListable> interfaces = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            ExerciseWithMuscleGroup ex = exercises.get(i);
            ex.setMuscleName(Muscle.getMuscleName(getContext(), ex.getIdMuscle()));
            interfaces.add(ex);
        }

        return interfaces;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if (inflater != null) {
            inflater.inflate(R.menu.exercises_search_menu, menu);
            MenuItem item = menu.findItem(R.id.exercises_list_searchview);
            searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());

            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setActionView(searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (adapter != null) {
                        adapter.filter(query);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (adapter != null) {
                        adapter.filter(newText); //[TODO] Why is this not working when deleting characters from the filter? It seems to trigger, but the adapter.originalData is saving the filtered data?
                        return true;
                    }
                    return false;
                }
            });
            searchView.setOnClickListener(view -> {
                    }
            );
        }
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
                } else if (selectedGroupBy > 0 && selectedGroupBy <= MuscleGroup.getAllMuscleGroupsIds().size()) {
                    viewModel.loadExercisesByMuscleGroup(selectedGroupBy - 1);
                }
                //adapter.filter(selectedGroupBy, searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Clear any memory references in recyclerview.
    private void clearRecyclerView() {
        recyclerView.setAdapter(null);
        adapter = null;
    }
}
