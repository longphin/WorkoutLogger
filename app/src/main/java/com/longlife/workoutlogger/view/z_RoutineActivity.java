package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.adapters.z_ExpandableExerciseListAdapter;
import com.longlife.workoutlogger.controller.RoutineExerciseController;
import com.longlife.workoutlogger.model.DataAccessor;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.HashMap;
import java.util.List;

public class z_RoutineActivity extends AppCompatActivity {
    private ExpandableListView recyclerView;
    private ExpandableListAdapter recyclerAdapter;
    private RoutineExerciseController routineExerciseController;

    private List<SessionExercise> sessionExercises;
    private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash;
    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z_routine);

        recyclerView = (ExpandableListView) findViewById(R.id.expRecycler_exercises);

        // get data from Parcelable
        Intent intent = getIntent();
        thisRoutine = intent.getParcelableExtra("Routine");

        // Create controller to interact with database.
        routineExerciseController = new RoutineExerciseController(DataAccessor.getInstance());

        // Get latest RoutineSession for this Routine.
        RoutineSession latestRoutineSession = routineExerciseController.getLatestRoutineSession(thisRoutine);
        if (!latestRoutineSession.getWasPerformed())
            // If the latest RoutineSession was not performed, then set it as thisRoutineSession.
            thisRoutineSession = latestRoutineSession;
        else
            // Else, create a copy of the session.
            thisRoutineSession = routineExerciseController.createRoutineSessionCopy(latestRoutineSession);

        // Get the Exercises in this RoutineSession.
        sessionExercises = routineExerciseController.getSessionExercisesFromRoutineSession(thisRoutineSession);

        // Create list adapter
        sessionExerciseSetHash = routineExerciseController.getSessionExerciseSetHash();
        recyclerAdapter = new z_ExpandableExerciseListAdapter(this, sessionExercises, sessionExerciseSetHash);

        recyclerView.setAdapter(recyclerAdapter);

        for (int i = 0; i < recyclerAdapter.getGroupCount(); i++)
            recyclerView.expandGroup(i);
    }
}
