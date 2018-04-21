package com.longlife.workoutlogger.v2.RoutineOverview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.Routine;

import java.util.List;

import io.reactivex.Observable;

public class RoutinesOverviewActivity extends AppCompatActivity {
    private RecyclerView rv_routinesList;

    private Observable<List<Routine>> routines;// = Database.getInstance(this).getRoutines();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_routinesList = findViewById(R.id.rv_routinesList);

        setContentView(R.layout.activity_routines_overview);
    }
}
