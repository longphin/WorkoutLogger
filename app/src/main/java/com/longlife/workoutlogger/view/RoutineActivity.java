package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.adapters.DataAccessor;
import com.longlife.workoutlogger.adapters.ExerciseListAdapter;
import com.longlife.workoutlogger.controller.RoutineExerciseController;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.utils.NestedLinearLayoutManager;
import com.longlife.workoutlogger.utils.NumericKeyboardFragment;

public class RoutineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExerciseListAdapter recyclerAdapter;

    private RoutineExerciseController routineExerciseController;

    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

    // This is the set that is in focus
    private SessionExerciseSet focusedSet; // [TODO] use this when setting the value when keyboard if pressed.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        // Create controller to interact with database.
        routineExerciseController = new RoutineExerciseController(DataAccessor.getInstance());

        initRoutineData();
        initRecyclerView();

        // Check that the activity is using a layout that contains the keyboard fragment container.
        if (findViewById(R.id.fragment_container) != null) {
            // However, if restoring from a previous state, then we don't need to do anything.
            if (savedInstanceState != null) {
                return;
            }
        }
    }

    public void setFocusedExerciseSet(SessionExerciseSet toFocus) {
        focusedSet = toFocus;
    }

    public void createKeyboardFragment(SessionExerciseSet sessionExerciseSet) {
        // [TODO] Add view type parameter to createKeyboardFragment(), so that it can decide between typeText type and scoreText type

        /*
         [TODO] add a InputType parameter, where the keyboard that is created depends on the type.
         For example, numeric input, time input, etc.
          */

        ExerciseType exerciseType = routineExerciseController.getExerciseType(sessionExerciseSet);

        if (exerciseType == ExerciseType.WEIGHT || exerciseType == ExerciseType.BODYWEIGHT) {
            // Create an instance of the fragment.
            NumericKeyboardFragment kbFragment = new NumericKeyboardFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments.
            kbFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the fragment container.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, kbFragment)
                    .commit();
        } else {
            // Clear all fragments in fragment container.
            FragmentManager fm = getSupportFragmentManager();
            for (Fragment fragment : fm.getFragments()) {
                fm.beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }

    public void addFocusValue(int val) {
        if (focusedSet == null) return;

        routineExerciseController.addValueToExerciseSet(focusedSet, val);

        // [TODO] iterate through recycler views and get the focus item, then append val to the current value.
        Toast.makeText(getApplicationContext(), String.valueOf(val),
                Toast.LENGTH_SHORT).show();
    }

    private void initRoutineData() {
        // get data from Parcelable
        Intent intent = getIntent();
        thisRoutine = intent.getParcelableExtra("Routine");

        // Get latest RoutineSession for this Routine.
        thisRoutineSession = routineExerciseController.getOrCreateLatestRoutineSession(thisRoutine);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.a_expRecycler_exercises);
        recyclerAdapter = new ExerciseListAdapter(this, thisRoutineSession);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new NestedLinearLayoutManager(this));
    }
}
