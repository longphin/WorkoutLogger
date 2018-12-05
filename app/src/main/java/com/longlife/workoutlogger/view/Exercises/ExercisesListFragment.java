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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;

import java.util.List;

import javax.inject.Inject;

public class ExercisesListFragment extends FragmentBase {
    private static final String TAG = ExercisesListFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesViewModel viewModel;
    private RecyclerView recyclerView;
    private ExercisesListRemakeAdapter adapter;
    private boolean needsToLoadData = true;

    public ExercisesListFragment() {
        // Required empty public constructor
    }

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
        addDisposable(viewModel.getExerciseInsertedObservable().subscribe(exercise -> processExerciseInserted(exercise)));

        initializeObservers();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(getLayoutId(), container, false);

        FloatingActionButton btn_addExercise = v.findViewById(R.id.btn_addExercise);
        btn_addExercise.setOnClickListener(view -> startCreateFragment());

        initializeObservers();
        initializeRecyclerView(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //setAdapterForRecyclerView();//[TODO] need to recreate the adapter and recyclerview.
    }

    @Override
    public void onDestroyView() {

        //viewModelFactory = null;
        if (adapter != null) {
            adapter = null;
        }

        if (recyclerView != null) {
            recyclerView = null;
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
/*        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExerciseCreateRemakeFragment fragment = (ExerciseCreateRemakeFragment) manager.findFragmentByTag(ExerciseCreateRemakeFragment.TAG);
        if (fragment == null) {
            fragment = ExerciseCreateRemakeFragment.newInstance();
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }*/
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

    // Data was loaded, so now attach the adapter to the recyclerview.
    private void loadData(List<ExerciseShort> exercises) {
        if (adapter == null)
            adapter = new ExercisesListRemakeAdapter(exercises);
        Log.d(TAG, String.valueOf(exercises.size()) + " exercises obtained");

        setAdapterForRecyclerView();
    }

    private void setAdapterForRecyclerView() {
        if (recyclerView != null && adapter != null) {
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
