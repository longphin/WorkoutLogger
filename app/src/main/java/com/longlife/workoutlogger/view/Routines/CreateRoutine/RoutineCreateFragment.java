package com.longlife.workoutlogger.view.Routines.CreateRoutine;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.longlife.workoutlogger.AndroidUtils.ActivityBase;
import com.longlife.workoutlogger.AndroidUtils.FragmentBase;
import com.longlife.workoutlogger.AndroidUtils.RecyclerItemTouchHelper;
import com.longlife.workoutlogger.AndroidUtils.RecyclerViewHolderSwipeable;
import com.longlife.workoutlogger.AndroidUtils.StringArrayAdapter;
import com.longlife.workoutlogger.MyApplication;
import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.utils.Animation;
import com.longlife.workoutlogger.utils.Response;
import com.longlife.workoutlogger.view.DialogFragment.AddNoteDialog;
import com.longlife.workoutlogger.view.Exercises.EditExercise.ExerciseEditFragment;
import com.longlife.workoutlogger.view.Exercises.ExercisesViewModel;
import com.longlife.workoutlogger.view.MainActivity;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableAdapter;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableFragment;
import com.longlife.workoutlogger.view.Routines.CreateRoutine.AddExercisesToRoutine.ExercisesSelectableViewModel;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;
import com.longlife.workoutlogger.view.Routines.RoutinesViewModel;

import java.util.List;

import javax.inject.Inject;

public class RoutineCreateFragment
        extends FragmentBase
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        AddNoteDialog.OnInputListener,
        ExercisesSelectableAdapter.IClickExercise,
        RoutineCreateAdapter.IOnSetClick,
        EditSetDialog.IOnSave {
    public static final String TAG = RoutineCreateFragment.class.getSimpleName();
    // View models
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    // Other
    String descrip;
    @Inject
    Context context;
    private RecyclerView recyclerView;
    private RoutineCreateAdapter adapter;
    // OnClick listener for when item in recyclerview is clicked.
    private AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> Log.d(TAG, "Selected " + adapterView.getItemAtPosition(i));
    private AutoCompleteTextView searchBox;
    private ConstraintLayout coordinatorLayout; // layout for recycler view
    private View mView;
    private EditText name;
    private ImageView searchBoxStatusImage;
    private RoutinesViewModel routinesViewModel;
    private ExercisesSelectableViewModel exercisesSelectedViewModel;
    // OnClick listener for when "search exercise" image is clicked.
    private View.OnClickListener onSearchClickListener = view -> startSearchExercises();
    private ExercisesViewModel exercisesViewModel;
    // Adapter for free form exercise search.
    private StringArrayAdapter searchAdapter;

    public RoutineCreateFragment() {
        // Required empty public constructor
    }

    public static RoutineCreateFragment newInstance() {
        return (new RoutineCreateFragment());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);

        routinesViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(RoutinesViewModel.class);

        exercisesSelectedViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesSelectableViewModel.class);

        exercisesViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ExercisesViewModel.class);

        // Observer to get list of exercises to add to this routine through the ExercisesOverviewFragment.
        addDisposable(exercisesSelectedViewModel.getAddExercisesToRoutineResponse().subscribe(response -> processSelectedExercisesResponse(response)));
        // Observer to get when routine is successfully saved.
        addDisposable(routinesViewModel.getInsertResponse().subscribe(response -> processInsertResponse(response)));
        // Observer to get when an exercise is updated.
        addDisposable(exercisesViewModel.getExerciseEditedObservable().subscribe(exercise -> {
            adapter.exerciseUpdated(exercise);
            // [TODO] Need to update the search box by calling loadExercises(). It might be better to update the search box when an exercise is added, deleted, or edited, but unless we store the exercises, this is difficult to do because the search box adapter only keeps the names. However, this is extremely expensive to do if there are many exercises (which there may well be).
            addDisposable(exercisesViewModel.loadExerciseNames().subscribe(exerciseNames -> searchAdapter.setList(exerciseNames)));
        }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_routine_create, container, false);

            this.name = mView.findViewById(R.id.edit_routineCreateName);
            ImageView addNoteImage = mView.findViewById(R.id.imv_routine_create_addNote);
            Button cancelButton = mView.findViewById(R.id.btn_routineCreateCancel);
            Button saveButton = mView.findViewById(R.id.btn_routineCreateSave);
            searchBox = mView.findViewById(R.id.txt_routineexercisecreate_searchBox);
            searchBoxStatusImage = mView.findViewById(R.id.imv_routine_create_searchBoxStatus);
            ImageView searchExercises = mView.findViewById(R.id.btn_searchExercises);
            coordinatorLayout = mView.findViewById(R.id.routine_create_layout);

            recyclerView = mView.findViewById(R.id.rv_routineCreateExercises);
            // Initialize recycler view
            initializeRecyclerView();

            // OnClick cancel button.
            cancelButton.setOnClickListener(view -> ((ActivityBase) getActivity()).onBackPressedCustom(view));

            // OnClick listener to change the routine name and description. Opens up a dialog fragment for user to change the values.
            addNoteImage.setOnClickListener(view ->
            {
                AddNoteDialog dialog = AddNoteDialog.newInstance(this.descrip);
                dialog.show(getChildFragmentManager(), AddNoteDialog.TAG);
            });

            // OnClick for saving routine.
            saveButton.setOnClickListener(view ->
            {
                if (!this.name.getText().toString().trim().equals("")) {
                    Routine routineToAdd = new Routine();
                    routineToAdd.setName(name.getText().toString());
                    routineToAdd.setNote(descrip);//descrip.getText().toString());
                    routinesViewModel.insertRoutineFull(routineToAdd, adapter.getRoutineExerciseHelpers());

                    getActivity().onBackPressed();
                } else {
                    if (isAdded()) {
                        this.name.startAnimation(Animation.shakeError());
                        Toast.makeText(context,
                                getResources().getString(R.string.requiredFieldsMissing),
                                Toast.LENGTH_SHORT
                        )
                                .show();
                    }
                }
            });

            // Search exercises image.
            searchExercises.setOnClickListener(onSearchClickListener);
            // Search icon: When selected, search through entire exercise list.
            searchBox.setOnItemClickListener(onItemClickListener);
            // Add a TextWatcher to the search box to determine if the search has a match.
            searchBox.addTextChangedListener(new TextWatcher() {
                // Other
                // Add handler and runnable to give a delay on the text check.
                Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
                Runnable workRunnable;


                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Remove runnables that check the search box input.
                    handler.removeCallbacks(workRunnable);

                    final Context activityContext = getActivity();

                    // Check if input is empty or only contains whitespace. If empty, then we don't need to check for validity.
                    if (charSequence.toString().trim().length() == 0) // empty
                    {
                        searchBoxStatusImage.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_error_outline_black_24dp));
                        return;
                    }

                    // Need to check validity. Show a "loading" icon until user finishes entering input.
                    searchBoxStatusImage.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_settings_ethernet_black_24dp));

                    // Create runnable to check the input after a delay.
                    workRunnable = () -> {
                        if (searchAdapter.contains(charSequence.toString())) {
                            Log.d(TAG, charSequence.toString() + " is in list");
                            searchBoxStatusImage.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_check_black_24dp));
                        } else {
                            Log.d(TAG, charSequence.toString() + " not in list");
                            searchBoxStatusImage.setImageDrawable(ContextCompat.getDrawable(activityContext, R.drawable.ic_error_outline_black_24dp));
                        }
                    };
                    // Set the runnable's delay.
                    handler.postDelayed(workRunnable, 500);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            // Get a list of exercise names for the search box.
            addDisposable(exercisesViewModel.loadExerciseNames().subscribe(exerciseNames ->
            {
                searchAdapter = new StringArrayAdapter(context, R.layout.autocompletetextview, exerciseNames); // Initializes adapter
                searchBox.setAdapter(searchAdapter);
            }));
        }

        ((MainActivity) getActivity()).updateToolbarTitle(getString(R.string.Toolbar_RoutineCreate));
        return (mView);
    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new RoutineCreateAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        // Callback to detach swipe to delete motion.
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    // Process list of exercises that were selected in the searchbox fragment.
    private void processSelectedExercisesResponse(Response<List<Exercise>> response) {
        switch (response.getStatus()) {
            case LOADING:
                renderSelectedExercisesState();
                break;
            case SUCCESS:
                renderSelectedExercisesSuccessState(response.getValue());
                break;
            case ERROR:
                renderSelectedExercisesErrorState(response.getError());
                break;
        }
    }

    private void processInsertResponse(Response<Routine> response) {
        switch (response.getStatus()) {
            case LOADING:
                renderInsertLoadingState();
                break;
            case SUCCESS:
                renderInsertSuccessState(response.getValue());
                break;
            case ERROR:
                renderInsertErrorState(response.getError());
                break;
        }
    }
    // Insertion Response

    private void renderSelectedExercisesState() {
    }

    private void renderSelectedExercisesSuccessState(List<Exercise> ex) {
        adapter.addExercises(ex);
    }

    private void renderSelectedExercisesErrorState(Throwable error) {
    }

    // Methods
    private void renderInsertLoadingState() {
        if (isAdded())
            Log.d(TAG, "attached: loading exercises");
        else
            Log.d(TAG, "detached: loading exercises");
    }

    private void renderInsertSuccessState(Routine routine) {
        if (isAdded()) {
            Log.d(TAG, "attached: " + routine.getName());

            getActivity().onBackPressed();
        } else {
            Log.d(TAG, "detached: " + routine.getName());
        }
        clearDisposables();
    }

    private void renderInsertErrorState(Throwable throwable) {
        // change anything if loading data had an error.
        Log.d(TAG, throwable.getMessage());
    }

    @Override
    public void sendInput(String descrip) {
        this.descrip = descrip;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    // On Swipe for recyclerview item.
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int pos) {
        if (viewHolder instanceof RecyclerViewHolderSwipeable) {
            int position = viewHolder.getAdapterPosition();

            int swipedItemType = adapter.getItemViewType(position);

            if (swipedItemType == RoutineCreateAdapter.getHeaderTypeEnum()) {
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

    // On drag up and down for recyclerview item.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        if (fromPosition < adapter.getItemCount() && toPosition < adapter.getItemCount()) {

            boolean swapWasDone = false;

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    swapWasDone = swapWasDone || adapter.swap(i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    swapWasDone = swapWasDone || adapter.swap(i, i - 1);
                }
            }
            if (swapWasDone)
                adapter.notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return ItemTouchHelper.RIGHT;
    }

    // When exercise is clicked, open up edit fragment
    @Override
    public void exerciseClicked(Long idExercise) {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExerciseEditFragment fragment = (ExerciseEditFragment) manager.findFragmentByTag(ExerciseEditFragment.TAG);
        if (fragment == null) {
            fragment = ExerciseEditFragment.newInstance(idExercise);
        }

/*		addFragmentToActivity(manager, fragment,
			R.id.frameLayout_main_activity,//R.id.root_main_activity,
			ExerciseEditFragment.TAG, ExerciseEditFragment.TAG
		);*/
        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    @Override
    public void exerciseLocked(Long idExercise, boolean lockStatus) {
        exercisesViewModel.updateLockedStatus(idExercise, lockStatus);
    }

    @Override
    public void exercisePerform(Long idExercise, String exerciseName) {
        // [TODO] Routines does not need to implement this. May want to separate this interface so this routine does not need to be overridden.
    }

    @Override
    public void onSetClick(@Nullable RoutineCreateAdapter.RoutineExerciseSetPositions positionHelper) {
        if (positionHelper == null)
            return;

        Log.d(TAG, "starting idSessionExerciseSet " + String.valueOf(positionHelper.getExerciseIndex()) + " " + String.valueOf(positionHelper.getSetIndexWithinExerciseIndex()));
        EditSetDialog dialog = EditSetDialog.newInstance(positionHelper);
        dialog.show(getChildFragmentManager(), EditSetDialog.TAG);
    }

    @Override
    public void saveSet(int exerciseIndex, int exerciseSetIndex, int restMinutes, int restSeconds) {
        adapter.setRestTimeForSet(exerciseIndex, exerciseSetIndex, restMinutes, restSeconds);
    }

    // Click search exercise button
    private void startSearchExercises() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        ExercisesSelectableFragment fragment = (ExercisesSelectableFragment) manager.findFragmentByTag(ExercisesSelectableFragment.TAG);
        if (fragment == null) {
            fragment = ExercisesSelectableFragment.newInstance(exercisesSelectedViewModel, R.id.root_main_activity, R.layout.fragment_routine_exercises);
        }

        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
        //addFragmentToActivity(manager, fragment, R.id.root_main_activity, ExercisesSelectableFragment.TAG, ExercisesSelectableFragment.TAG);
    }
}

