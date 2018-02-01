package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.RoutinesInterface;

import java.util.List;

/**
 * Created by Longphi on 1/6/2018.
 */

public class RoutineController {
    private RoutinesInterface view;
    private DataAccessorInterface dataSource;

        /**
     * As soon as this object is created, it does a few things:
     * 1. Assigns Interfaces Variables so that it can talk to the DataSource and that Activity
     * 2. Tells the dataSource to fetch a List of ListItems.
     * 3. Tells the View to draw the fetched List of Data.
     * @param view
     * @param dataSource
     */
    public RoutineController(RoutinesInterface view, DataAccessorInterface dataSource) {
        this.view = view;
        this.dataSource = dataSource;

        //getRoutinesFromDataSource();
    }

    public void onRoutineClick(Routine selectedItem){
        view.startRoutineActivity(selectedItem);
    }

    /**
     * In a real App, I would normally talk to this DataSource using RxJava 2. This is because most
     * calls to Services like a Database/Server should be executed on a seperate thread that the
     * mainThread (UI Thread). See my full projects for examples of this.
     */
    /*
    public void getRoutinesFromDataSource(){
        view.setUpAdapterAndView(
                dataSource.getRoutines()
        );
    }
    */
    public List<Routine> getRoutines() {
        return (dataSource.getRoutines());
    }

    public void saveRoutine(Routine routineToSave) {
        dataSource.saveRoutine(routineToSave);
    }

    public List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise) {
        return (dataSource.getSessionExerciseSets(sessionExercise));
    }
    /* // [TODO] remove
    public void deleteRoutineSession(RoutineSession routineSession)
    {
        dataSource.deleteRoutineSession(routineSession);
    }
    */
}
