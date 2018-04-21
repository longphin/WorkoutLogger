package com.longlife.workoutlogger.v2.RoutineOverview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.v2.data.Database;
import com.longlife.workoutlogger.v2.model.Routine;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoutinesOverviewActivity extends AppCompatActivity {
    private RecyclerView rv_routinesList;

    private Observable<List<Routine>> routines;// = Database.getInstance(this).getRoutines();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_routinesList = findViewById(R.id.rv_routinesList);
        //DatabaseInitializer.populateAsync(Database.getInstance(this));

        initializeDatabase(RoutinesOverviewActivity.this);
        setContentView(R.layout.activity_routines_overview);
    }

    // [TODO] remove
    private void initializeDatabase(Context context) {
        Database db = Database.getInstance(context);

        // dummy records
        ArrayList<Routine> routinesToAdd = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Routine routineToAdd = new Routine();
            routineToAdd.setName("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " name");
            routineToAdd.setDescription("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " description");
            //addRoutine(db, routineToAdd);
            routinesToAdd.add(routineToAdd);
        }

        db.dao().insertRoutines(routinesToAdd);

        // Subscribe to get data
        db.dao().getRoutines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
        //.subscribe();

    }

    private void handleResponse(List<Routine> routines) {
        Log.e("routine size : ", routines.size() + "");

        for (int i = 0; i < routines.size(); i++) {
            Log.e("routine name: ", routines.get(i).getName());
        }
    }
}
