package com.longlife.workoutlogger.v2.view.ExercisesOverview;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.utils.FragmentWithCompositeDisposable;
import com.longlife.workoutlogger.v2.utils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.v2.utils.Response;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesOverviewFragment extends FragmentWithCompositeDisposable implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    public static final String TAG = ExercisesOverviewFragment.class.getSimpleName();
    @Inject
    public Context context;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private ExercisesOverviewViewModel viewModel;

    private RecyclerView recyclerView;
    private ConstraintLayout coordinatorLayout; // layout for recycler view
    private ExercisesAdapter adapter;

    public ExercisesOverviewFragment() {
    }

    public static ExercisesOverviewFragment newInstance() {
        return (new ExercisesOverviewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = //ViewModelProvider.AndroidViewModelFactory.getInstance(app).// [TODO] when upgrading lifecycle version to 1.1.1, ViewModelProviders will become deprecated and something like this will need to be used (this line is not correct, by the way).
                ViewModelProviders.of(getActivity(), viewModelFactory)
                        .get(ExercisesOverviewViewModel.class);

        // Observe events when the list of exercises is obtained.
        //viewModel.loadResponse().observe(this, response -> processResponse(response));
        viewModel.getLoadResponse().subscribe(response -> processLoadResponse(response));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercises_overview, container, false);

        // Add listener to "add routine button"
        FloatingActionButton btn_addRoutine = v.findViewById(R.id.btn_addExercise);
        btn_addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.startCreateFragment();
            }
        });

        // Initialize recyclerview.
        recyclerView = v.findViewById(R.id.rv_exercisesOverview);
        coordinatorLayout = v.findViewById(R.id.exercises_overview_layout);
        initializeRecyclerView();

        return (v);
    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new ExercisesAdapter(viewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // populate recycler view with all data
        viewModel.loadExercises();
    }

    // On Swipe for recycler view item.
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos) {
        if (viewHolder instanceof ExercisesViewHolder) {
            int position = viewHolder.getAdapterPosition();

            // get the removed item name to display it in snack bar
            List<Exercise> exercises = viewModel.getCachedExercises();
            String name = exercises.get(position).getName();

            // backup of removed item for undo purpose
            final Exercise deletedItem = exercises.get(position);
            final int deletedIndex = position;

            // remove the item from recycler view
            adapter.removeExercise(position);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreExercise(deletedItem, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    // If the snackbar was dismissed via clicking the action (Undo button), then do not permanently delete the exercise.
                    if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) return;

                    // For other dismiss events, permanently delete the exercise.
                    Log.d(TAG, "Exercise deleted permanently.");
                    viewModel.deleteExercise(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    ///
    /// GET EXERCISES RENDERING
    ///
    private void processLoadResponse(Response<List<Exercise>> response) {
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

    private void renderLoadingState() {
        Toast.makeText(context, "loading exercises", Toast.LENGTH_SHORT);

        Log.d(TAG, "loading exercises");
    }

    private void renderSuccessState(List<Exercise> exercises) {
        StringBuilder sb = new StringBuilder();
        sb.append(exercises == null ? 0 : exercises.size());
        sb.append(" exercises obtained");

        Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT);

        Log.d(TAG, sb.toString());

        adapter.setExercises(exercises);
    }

    private void renderErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT);
        Log.d(TAG, throwable.getMessage());
    }
    ///
}
