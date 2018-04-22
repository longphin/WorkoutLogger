package com.longlife.workoutlogger.v2.view.RoutineOverview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.BaseActivity;

public class RoutinesOverviewActivity extends BaseActivity {
    private static final String TAG_FRAG = "ROUTINESOVERVIEW_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines_overview);

        FragmentManager manager = getSupportFragmentManager();
        RoutinesOverviewFragment fragment = (RoutinesOverviewFragment) manager.findFragmentByTag(TAG_FRAG);
        if (fragment == null) {
            fragment = RoutinesOverviewFragment.newInstance();
        }

        addFragmentToActivity(manager, fragment, R.id.root_routines_overview, TAG_FRAG);
    }
/*
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

        // Subscribe to get data // [TODO] Need
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
    */
}
