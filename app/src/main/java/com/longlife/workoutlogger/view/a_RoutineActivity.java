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
import com.longlife.workoutlogger.model.z_DataAccessor;
import com.longlife.workoutlogger.utils.CustomLinearLayoutManager;

import java.util.List;

public class a_RoutineActivity extends AppCompatActivity implements RoutineExerciseInterface {
    private RecyclerView recyclerView;
    private a_CustomExerciseAdapter recyclerAdapter;

    private z_RoutineExerciseController routineExerciseController;

    private Routine thisRoutine;
    private RoutineSession thisRoutineSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__routine);

        // Create controller to interact with database.
        routineExerciseController = new z_RoutineExerciseController(z_DataAccessor.getInstance());

        initRoutineData();
        initRecyclerView();
    }

    private void initRoutineData() {
        // get data from Parcelable
        Intent intent = getIntent();
        thisRoutine = intent.getParcelableExtra("Routine");

        // Get latest RoutineSession for this Routine.
        thisRoutineSession = routineExerciseController.getOrCreateLatestRoutineSession(thisRoutine);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.a_expRecycler_exercises);
        recyclerAdapter = new a_CustomExerciseAdapter(this, thisRoutineSession);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
    }

    @Override
    public void startExerciseActivity(Exercise exercise) {

    }

    @Override
    public void setSessionExercises(List<SessionExercise> sessionExercises) {

    }
}
