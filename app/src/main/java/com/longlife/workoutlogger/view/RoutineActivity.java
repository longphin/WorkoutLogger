package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.adapters.DataAccessor;
import com.longlife.workoutlogger.adapters.ExerciseListAdapter;
import com.longlife.workoutlogger.controller.RoutineExerciseController;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.utils.NestedLinearLayoutManager;
import com.longlife.workoutlogger.utils.NumericKeyboardFragment;

public class RoutineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExerciseListAdapter recyclerAdapter;

    private RoutineExerciseController routineExerciseController;

    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

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

    public void createKeyboardFragment() {
        // Create an instance of the fragment.
        NumericKeyboardFragment kbFragment = new NumericKeyboardFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments.
        kbFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the fragment container.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, kbFragment)
                .commit();
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
