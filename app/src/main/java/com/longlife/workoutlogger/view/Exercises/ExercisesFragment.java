package com.longlife.workoutlogger.view.Exercises;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.Exercise;
import com.longlife.workoutlogger.model.Exercise.ExerciseLocked;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.Exercise.ExerciseUpdated;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.ExerciseCreateFragment;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;
import com.longlife.workoutlogger.view.Exercises.Helper.DeletedExercise;
import com.longlife.workoutlogger.view.Exercises.PerformExercise.PerformExerciseFragment;
import com.longlife.workoutlogger.view.MainActivity;

import java.util.List;

import javax.inject.Inject;

public class ExercisesFragment
        extends FragmentBase
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        ExercisesAdapter.IClickExercise {
    public static final String TAG = ExercisesFragment.class.getSimpleName();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    @Inject
    public Context context;
    protected View mView;
    protected ExercisesListAdapter adapter;
    @Required
    private int rootId; //This is the root of the layout from the parent activity. This is needed to determine how to attach the child ExercisesCreateFragment when opened to create a new exercise.
    private ExercisesViewModel viewModel;
    @Required
    private int layoutId;
    private RecyclerView recyclerView;
    private ConstraintLayout viewRootLayout; // layout for recycler view
    // Input constants.
    protected static final String INPUT_ACTIVITY_ROOT = "activityRoot";
    protected static final String INPUT_EXERCISE_ITEM_LAYOUT = "exerciseItemLayout";

    public static ExercisesFragment newInstance(int activityRoot, int exerciseItemLayout) {
        ExercisesFragment fragment = new ExercisesFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ExercisesFragment.INPUT_ACTIVITY_ROOT, activityRoot);
        bundle.putInt(ExercisesFragment.INPUT_EXERCISE_ITEM_LAYOUT, exerciseItemLayout);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);

        this.rootId = getArguments().getInt(ExercisesFragment.INPUT_ACTIVITY_ROOT);
        this.layoutId = getArguments().getInt(ExercisesFragment.INPUT_EXERCISE_ITEM_LAYOUT);

        initializeAdapter(getActivity());

        addDisposable(viewModel.getLoadExercisesResponse().subscribe(response -> processLoadRoutineResponse(response)));
        addDisposable(viewModel.getExerciseInsertedObservable().subscribe(exercise -> processExerciseInserted(exercise)));
        addDisposable(viewModel.getExerciseEditedObservable().subscribe(exercise -> processExerciseEdited(exercise)));
        addDisposable(viewModel.getExerciseLockedObservable().subscribe(exerciseLocked -> processExerciseLocked(exerciseLocked)));
        addDisposable(viewModel.getExerciseRestoredObservable().subscribe(exerciseDeleted -> {
            if (isAdded())
                // If this fragment is currently active, then the adapter needs to re-add the exercise and notify the insert.
                adapter.restoreExercise(exerciseDeleted.getExercise(), exerciseDeleted.getPosition());
            else
                // If this fragment is not currently active but is listening, then the adapter just needs to notify that the item was changed.
                adapter.notifyItemChanged(exerciseDeleted.getPosition());
        }));

        Log.d(TAG, "OnCreate: loadExercises()");
        viewModel.loadExercises();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false);

            recyclerView = mView.findViewById(R.id.rv_exercises);
            viewRootLayout = mView.findViewById(R.id.exercises_overview_layout);

            FloatingActionButton btn_addRoutine = mView.findViewById(R.id.btn_addExercise);
            btn_addRoutine.setOnClickListener(view -> startCreateFragment());

            initializeRecyclerView();
        }
        // Inflate the layout for this fragment

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_Exercises));
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
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

    public void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    protected void initializeAdapter(Context context) {
        if (adapter == null)
            adapter = new ExercisesAdapter(context, this);
    }

    private void processLoadRoutineResponse(Response<List<ExerciseShort>> response) {
        //if(!isAdded()) return;
        switch (response.getStatus()) {
            case LOADING:
                renderLoadRoutineLoadState();
                break;
            case SUCCESS:
                renderLoadRoutineSuccessState(response.getValue());
                break;
            case ERROR:
                if (response.getError() != null)
                    renderLoadRoutineErrorState(response.getError());
                break;
        }
    }

    private void processExerciseInserted(Exercise ex) {
        adapter.addExercise(new ExerciseShort(ex));
    }

    // Methods
    private void processExerciseLocked(ExerciseLocked exerciseLocked) {
        adapter.exerciseLocked(exerciseLocked);
    }

    private void renderLoadRoutineLoadState() {
        Log.d(TAG, "loading exercises");
    }

    private void processExerciseEdited(ExerciseUpdated exercise) {
        adapter.exerciseUpdated(new ExerciseShort(exercise));
    }

    private void renderLoadRoutineErrorState(@NonNull Throwable throwable) {
        // change anything if loading data had an error.
        Log.d(TAG, throwable.getMessage());
    }

    private void renderLoadRoutineSuccessState(List<ExerciseShort> exercises) {
        StringBuilder sb = new StringBuilder();
        if (isAdded())
            sb.append("attached) ");
        else
            sb.append("detached) ");
        sb.append(exercises == null ? 0 : exercises.size());
        sb.append(" exercises obtained");

        Log.d(TAG, sb.toString());

        if (exercises == null)
            return;

        adapter.setExercises(exercises);
    }

    @Override
    public void exerciseClicked(Long idExercise) {
        startEditFragment(idExercise);
    }

    @Override
    public void exerciseLocked(Long idExercise, boolean lockStatus) {
        viewModel.updateLockedStatus(idExercise, lockStatus);
    }

    @Override
    public void exercisePerform(ExerciseShort ex) {//Long idExercise, String exerciseName) {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        PerformExerciseFragment fragment = (PerformExerciseFragment) manager.findFragmentByTag(PerformExerciseFragment.TAG);
        if (fragment == null) {
            fragment = PerformExerciseFragment.newInstance(ex);//idExercise, exerciseName);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    private void startEditFragment(Long idExercise) {
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
            final ExerciseShort deletedItem = adapter.getExercise(position);

            // Start the deleting process. It is only removed in the adapter, but it saved in the viewModel.
            // While the snackbar to undo the delete is available, the viewModel will keep the reference.
            // Upon the snackbar being dismissed, it will permanently remove the exercise.
            viewModel.addDeletedExercise(deletedItem, position);
            viewModel.setExerciseHiddenStatus(deletedItem.getIdExercise(), true);
            if (isAdded())
                adapter.removeExercise(position);

            Snackbar snackbar = Snackbar
                    .make(viewRootLayout, deletedItem.getName() + " deleted.", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", view -> {
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    final DeletedExercise firstDeletedExercise = viewModel.getFirstDeletedExercise();
                    if (firstDeletedExercise == null)
                        return;

                    // If the snackbar was dismissed via clicking the action (Undo button), then restore the exercise.
                    if (event == Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        viewModel.restoreExercise(firstDeletedExercise); // Unhides an exercise and publishes the restoration event.
                        return;
                    }
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
        return adapter.getSwipeDirs(viewHolder.getAdapterPosition());
    }
}
