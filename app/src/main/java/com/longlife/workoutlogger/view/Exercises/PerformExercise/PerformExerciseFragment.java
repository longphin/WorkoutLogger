package com.longlife.workoutlogger.view.Exercises.PerformExercise;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longlife.workoutlogger.AndroidUtils.ExercisesWithSetsAdapter;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise.ExerciseShort;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Perform.PerformFragment;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import javax.inject.Inject;

import io.reactivex.observers.DisposableMaybeObserver;

public class PerformExerciseFragment
        extends FragmentBase
        implements PerformRoutineAdapter.IOnSetClick,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        PerformSetDialog.IOnSave {
    public static final String TAG = PerformFragment.TAG;
    private Long idExercise;
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private Long idSessionExercise;
    private String note;
    private ExercisesViewModel exercisesViewModel;
    // Input Constants
    private static final String INPUT_ID_EXERCISE = "idExercise";
    private static final String INPUT_EXERCISE_NAME = "exerciseName";
    private static final String INPUT_NOTE = "note";

    public PerformExerciseFragment() {
        // Required empty public constructor
    }

    private ExerciseSessionWithSets exerciseWithSets;
    private View mView;
    private RecyclerView exercisesRecyclerView;
    private PerformRoutineAdapter adapter;
    private ConstraintLayout coordinatorLayout; // layout for recycler view

    public static PerformExerciseFragment newInstance(ExerciseShort ex) {
        Bundle bundle = new Bundle();
        bundle.putLong(PerformExerciseFragment.INPUT_ID_EXERCISE, ex.getIdExercise());
        bundle.putString(PerformExerciseFragment.INPUT_EXERCISE_NAME, ex.getName());
        bundle.putString(PerformExerciseFragment.INPUT_NOTE, ex.getNote());

        PerformExerciseFragment fragment = new PerformExerciseFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idExercise = getArguments().getLong(PerformExerciseFragment.INPUT_ID_EXERCISE);
        note = getArguments().getString(PerformExerciseFragment.INPUT_NOTE);

        exercisesViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);

        // Look up an existing session for this exercise. If there is none, then create a new one with some default sets.
        // [TODO] We probably should not look up an existing session. Should always create a new session?
        exercisesViewModel.getLatestExerciseSession(idExercise)
                .subscribe(new DisposableMaybeObserver<SessionExercise>() {
                    @Override
                    public void onSuccess(SessionExercise sessionExercise) {
                        // A valid session was found for the exercise, so obtain the sets related to that session.
                        addDisposable(exercisesViewModel.getSessionExerciseWithSets(sessionExercise.getIdSessionExercise())
                                .subscribe(sessionExerciseWithSets -> setSessionExerciseWithSets(sessionExerciseWithSets)));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // Insert a routine session, then insert session exercise, then insert one set. After all those inserts, it returns that session exercise with sets.
                        addDisposable(
                                exercisesViewModel.insertNewSessionForExercise(idExercise, note) // Insert a new session.
                                        .flatMap(sessionExercise -> exercisesViewModel.getSessionExerciseWithSets(sessionExercise.getIdSessionExercise())) // From that session, grab the exercise session with sets.
                                        .subscribe(sessionExerciseWithSets -> setSessionExerciseWithSets(sessionExerciseWithSets))
                        );
                    }
                });
    }

    private void setSessionExerciseWithSets(ExerciseSessionWithSets exerciseWithSets) {
        this.exerciseWithSets = exerciseWithSets;
        // If the data was obtained after the view was initialized, then we need to fill the adapter.
        if (exercisesRecyclerView != null) {
            fillAdapterData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_perform_exercise, container, false);
            exercisesRecyclerView = mView.findViewById(R.id.rv_perform_exercise);
            coordinatorLayout = mView.findViewById(R.id.perform_exercise_layout);

            initializeRecyclerView();
        }

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_PerformExercise, getArguments().getString(PerformExerciseFragment.INPUT_EXERCISE_NAME)));

        // Inflate the layout for this fragment
        return mView;
    }

    private void fillAdapterData() {
        adapter.setExercisesToInclude(exerciseWithSets);
        adapter.notifyDataSetChanged();

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_PerformExercise, exerciseWithSets.getExercise().getName() + " (session " + String.valueOf(exerciseWithSets.getSessionExercise().getIdRoutineSession()) + ")"));
    }

    private void initializeRecyclerView() {
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new PerformRoutineAdapter(getContext(), this);
        exercisesRecyclerView.setAdapter(adapter);

        // Callback to detach swipe to delete motion.
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(exercisesRecyclerView);

        if (exerciseWithSets != null) {
            // If the data was obtained before the view was finished, then we can fill the data now.
            fillAdapterData();
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

            int swipedItemType = adapter.getItemViewType(position);

            if (swipedItemType == ExercisesWithSetsAdapter.HEADER_TYPE) {
                // get the removed item name to display it in snack bar

                // backup of removed item for undo purpose
                final int deletedIndex = adapter.getHeaderIndex(position);
                final RoutineExerciseHelper deletedItem = adapter.getHeaderAtPosition(position);
                //final int deletedIndex = position;
                String name = deletedItem.getExercise().getName();

                // remove the item from recycler view
                adapter.removeExerciseAtPosition(position);

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, name + " deleted.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", view -> adapter.restoreExercise(deletedItem, deletedIndex));
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                adapter.removeItemAtPosition(position);
            }
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
    public void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds, @Nullable Double weight, @Nullable Integer reps, int weightUnit) {
        // [TODO] When set edit dialog is saved, store the values into the adapter/database.
        adapter.setWeightForSet(exerciseIndex, exerciseSetIndex, restMinutes, restSeconds, weight, reps, weightUnit);
    }

    @Override
    public void onSetClick(@Nullable ExercisesWithSetsAdapter.RoutineExerciseSetPositions positionHelper, PerformSetDialog.EditingType initialFocus) {
        if (positionHelper == null)
            return;

        PerformSetDialog dialog = PerformSetDialog.newInstance(positionHelper, initialFocus);
        dialog.show(getChildFragmentManager(), PerformSetDialog.TAG);
    }

    @Override
    public void startRestTimer(View v, int headerIndex, int setIndex, int minutes, int seconds) {
        ((MainActivity) getActivity()).startTimerNotificationService(v, headerIndex, setIndex, minutes, seconds);
    }
}
