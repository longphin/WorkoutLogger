package com.longlife.workoutlogger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.adapters.a_CustomExerciseAdapter;
import com.longlife.workoutlogger.controller.z_RoutineExerciseController;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.model.z_DataAccessor;
import com.longlife.workoutlogger.utils.CustomLinearLayoutManager;

import java.util.HashMap;
import java.util.List;

public class a_RoutineActivity extends AppCompatActivity implements RoutineExerciseInterface {
    private RecyclerView recyclerView;
    private a_CustomExerciseAdapter recyclerAdapter; // [TODO] create a new adapter for recyclerview

    private z_RoutineExerciseController routineExerciseController; // [TODO] create new controller for recyclerview

    private List<SessionExercise> sessionExercises;
    private HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash;
    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__routine);

        recyclerView = (RecyclerView) findViewById(R.id.a_expRecycler_exercises);

        // get data from Parcelable
        Intent intent = getIntent();
        thisRoutine = intent.getParcelableExtra("Routine");

        // Create controller to interact with database.
        routineExerciseController = new z_RoutineExerciseController(z_DataAccessor.getInstance());

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
        recyclerAdapter = new a_CustomExerciseAdapter(this, thisRoutineSession);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this)); // [TODO] need to use custom linear layout

        // add expand/collapse listener
        /*
        recyclerView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            @Override
            public void onGroupExpand(int groupPosition)
            {
                recyclerView.collapseGroup(groupPosition);
            }
        });
        recyclerView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                expandableListView.expandGroup(i);
                return(false);
            }
        });
        */
        /*
        // Set up routineExerciseController.
        routineExerciseController = new RoutineExerciseController(this, new DataAccessor(), thisRoutine);

        // Get latest RoutineSession for this Routine.
        RoutineSession latestRoutineSession = routineExerciseController.getLatestRoutineSession(thisRoutine);
        if (!latestRoutineSession.getWasPerformed())
            // If the latest RoutineSession was not performed, then set it as thisRoutineSession.
            thisRoutineSession = latestRoutineSession;
        else
            // Else, create a copy of the session.
            thisRoutineSession = routineExerciseController.createRoutineSessionCopy(latestRoutineSession);

        adapter = new CustomExerciseAdapter(getApplicationContext(), sessionExercises, routineExerciseController);//new CustomAdapter(getApplicationContext());
        recyclerAdapter = new ExpandableExerciseListAdapter(this, sessionExercises, )
        */
    }

    @Override
    public void startExerciseActivity(Exercise exercise) {

    }

    @Override
    public void setSessionExercises(List<SessionExercise> sessionExercises) {

    }
}
