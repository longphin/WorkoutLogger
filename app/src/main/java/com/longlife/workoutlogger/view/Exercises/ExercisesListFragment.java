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
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.enums.ExerciseListGroupBy;
import com.longlife.workoutlogger.enums.MuscleGroup;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.model.Exercise.IExerciseListable;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;
import com.longlife.workoutlogger.view.Exercises.PerformExercise.PerformExerciseFragment;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExercisesListFragment extends FragmentBase implements ExercisesListRemakeAdapter.IClickExercise {
    private static final String TAG = ExercisesListFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesViewModel viewModel;
    private RecyclerView recyclerView;
    private ExercisesListRemakeAdapter adapter;
    private boolean needsToLoadData = false;
    private SearchView searchView;
    private Spinner groupBySelector;
    private View mView;


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
        mView = inflater.inflate(getLayoutId(), container, false);

        initializeObservers();
        initializeRecyclerView(mView);
        initializeGroupByOptions(mView);
        return mView;
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

        mView = null;
        super.onDestroyView();
    }

    private int getLayoutId() {
        return R.layout.fragment_exercises;
    }

    private void initializeObservers() {
        if (needsToLoadData) {
            needsToLoadData = false;

            viewModel.loadExercises();
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

    private void setAdapterForRecyclerView() {
        if (recyclerView != null && adapter != null) {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);
        addDisposable(viewModel.getExerciseInsertedObservable().subscribe(this::processExerciseInserted));
        addDisposable(viewModel.getExerciseEditedObservable().subscribe(this::loadExerciseUpdated));
        addDisposable(viewModel.getExerciseRestoreObservable().subscribe(this::restoreExercise));
        addDisposable(viewModel.getExerciseDeletedObservable().subscribe(this::deleteExercise));
        addDisposable(viewModel.getExercisesDataObservable().subscribe(this::loadDataInterface));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if (inflater != null) {
            inflater.inflate(R.menu.exercises_search_menu, menu);

            initializeSearchForExercisesView(menu.findItem(R.id.exercises_list_searchview));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        searchView.setOnClickListener(view -> {
                }
        );
    }

    private void loadExerciseUpdated(ExerciseUpdated exerciseUpdated) {
        if (adapter != null) adapter.exerciseUpdated(exerciseUpdated);
    }

    private void processExerciseInserted(Exercise ex) {
        if (adapter != null)
            adapter.addExercise(new ExerciseShort(ex));
    }

    // Data was loaded, so now attach the adapter to the recyclerview.
    private void loadDataInterface(List<IExerciseListable> exercises) {
        if (adapter == null) {
            adapter = new ExercisesListRemakeAdapter(this, exercises);
        } else {
            adapter.resetData(exercises);
        }

        setAdapterForRecyclerView();
        if (searchView != null && adapter != null) {
            String query = searchView.getQuery().toString();
            if (!query.isEmpty())
                /*
                Observable.fromIterable(exercises)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<IExerciseListable>() {
                        @Override
                        public boolean test(IExerciseListable ex) throws Exception {
                            return ex.getName().toLowerCase().contains(query);
                        }
                    })
                    .toList()
                    .toObservable()
                .subscribe(new Observer<List<IExerciseListable>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<IExerciseListable> iExerciseListables) {
                        adapter.setData(iExerciseListables);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                */
                adapter.filterData(query);
            //adapter.filter(query);
            //filterData(exercises, query);
        }
    }

    private void filterData(List<IExerciseListable> exercises, String query) {
        //adapter.filter(exercises, query);
    }

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

    @Override
    public void exercisePerform(ExercisesListRemakeAdapter.exerciseItem ex) {
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
                .make(mView.findViewById(R.id.exercises_overview_layout)
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

    private void restoreExercise(ExerciseShort restoredExercise) {
        if (adapter != null && isAdded()) adapter.restoreExercise(restoredExercise);
    }

    private void deleteExercise(ExerciseShort deletedExercise) {
        adapter.deleteExercise(deletedExercise.getIdExercise());
    }
}
