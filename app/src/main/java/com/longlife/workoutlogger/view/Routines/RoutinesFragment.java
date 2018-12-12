/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/4/18 6:25 PM.
 */

package com.longlife.workoutlogger.view.Routines;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.RoutineCreateFragment;
import com.longlife.workoutlogger.view.Routines.EditRoutine.RoutineEditFragment;
import com.longlife.workoutlogger.view.Routines.Helper.DeletedRoutine;

import java.util.List;

import javax.inject.Inject;

public class RoutinesFragment
        extends FragmentBase
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        RoutinesAdapter.IClickRoutine {
    public static final String TAG = RoutinesFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutinesViewModel viewModel;
    private RecyclerView recyclerView;
    private RoutinesAdapter adapter;
    private View mView;
    private ConstraintLayout viewRootLayout; // layout for recycler view

    @Override
    public void onDestroyView() {
        recyclerView.removeAllViews();
        super.onDestroyView();
    }

    public RoutinesFragment() {

    }

    public static RoutinesFragment newInstance() {
        return (new RoutinesFragment());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

        //viewModel.getLoadExercisesResponse().subscribe(response -> processLoadResponse(response));
        addDisposable(viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response)));
        addDisposable(viewModel.getInsertResponse().subscribe(response -> processInsertRoutineResponse(response)));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_routines, container, false);

            viewRootLayout = this.mView.findViewById(R.id.root_routines_layout);

            // Add listener to "add routine button"
            FloatingActionButton btn_addRoutine = mView.findViewById(R.id.btn_addRoutine);

            btn_addRoutine.setOnClickListener(v -> {
                startCreateFragment();
            });

            // Initialize recyclerview.
            recyclerView = mView.findViewById(R.id.rv_routines);
            initializeRecyclerView();
        }

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_Routines));
        return (mView);
    }

    private void startCreateFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        RoutineCreateFragment fragment = (RoutineCreateFragment) manager.findFragmentByTag(RoutineCreateFragment.TAG);
        if (fragment == null) {
            fragment = RoutineCreateFragment.newInstance();
        }

/*		manager.beginTransaction()
			.replace(R.id.frameLayout_main_activity,//R.id.root_routines_layout,
				fragment, RoutineCreateFragment.TAG
			)
			.addToBackStack(RoutineCreateFragment.TAG)
			.commit();*/

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutinesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        viewModel.loadRoutines();
    }

    private void processLoadResponse(Response<List<Routine>> response) {
        switch (response.getStatus()) {
            case LOADING:
                renderLoadingState();
                break;
            case SUCCESS:
                renderSuccessState(response.getValue());
                break;
            case ERROR:
                renderErrorState(response.getError());
                break;
        }
    }

    // Methods
    private void processInsertRoutineResponse(Response<Routine> response) {
        switch (response.getStatus()) {
            case LOADING:
                break;
            case SUCCESS:
                renderInsertSuccessState(response.getValue());
                break;
            case ERROR:
                break;
        }
    }

    private void renderLoadingState() {
        Log.d(TAG, "loading routines");
    }

    private void renderSuccessState(List<Routine> routines) {
        Log.d(TAG, String.valueOf(routines.size()) + " routines obtained");

        adapter.setRoutines(routines);
        adapter.notifyDataSetChanged();
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Log.d(TAG, throwable.getMessage());
    }

    private void renderInsertSuccessState(Routine routine) {
        adapter.addRoutine(routine);
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos) {
        if (viewHolder instanceof RecyclerViewHolderSwipeable) {
            int position = viewHolder.getAdapterPosition();
            final Routine deletedItem = adapter.getRoutine(position);

            Log.d(TAG, "Deleting... " + deletedItem.getName() + " at position " + String.valueOf(position));

            // Start the deleting process. It is only removed in the adapter, but it saved in the viewModel.
            // While the snackbar to undo the delete is available, the viewModel will keep the reference.
            // Upon the snackbar being dismissed, it will permanently remove the exercise.
            viewModel.addDeletedRoutine(deletedItem, position);
            adapter.removeRoutine(position);

            Snackbar snackbar = Snackbar
                    .make(viewRootLayout, deletedItem.getName() + " deleted.", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    final DeletedRoutine firstDeletedRoutine = viewModel.getFirstDeletedRoutine();
                    if (firstDeletedRoutine == null)
                        return;

                    // If the snackbar was dismissed via clicking the action (Undo button), then restore the exercise.
                    if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        //adapter.restoreExercise(deletedItem, deletedIndex);
                        adapter.restoreRoutine(firstDeletedRoutine.getRoutine(), firstDeletedRoutine.getPosition());
                        return;
                    }

                    // For other dismiss events, permanently delete the exercise.
                    Log.d(TAG, "Exercise deleted permanently: " + firstDeletedRoutine.getRoutine().getName() + " " + String.valueOf(firstDeletedRoutine.getRoutine().getIdRoutine()));
                    viewModel.setRoutineHiddenStatus(firstDeletedRoutine.getRoutine().getIdRoutine(), true);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return ItemTouchHelper.RIGHT;
    }

    @Override
    public void routineClicked(Long idRoutine) {
        startEditFragment(idRoutine);
    }

    private void startEditFragment(Long idRoutine) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        RoutineEditFragment fragment = (RoutineEditFragment) manager.findFragmentByTag(RoutineEditFragment.TAG);
        if (fragment == null) {
            fragment = RoutineEditFragment.newInstance(idRoutine);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }
}

