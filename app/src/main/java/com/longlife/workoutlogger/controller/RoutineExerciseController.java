package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.view.RoutineExerciseInterface;

/**
 * Created by Longphi on 1/6/2018.
 */

public class RoutineExerciseController {
    /*
    private ViewInterface view;
    private DataSourceInterface dataSource;
    */
    private RoutineExerciseInterface view;
    private DataAccessorInterface dataSource;

        /**
     * As soon as this object is created, it does a few things:
     * 1. Assigns Interfaces Variables so that it can talk to the DataSource and that Activity
     * 2. Tells the dataSource to fetch a List of ListItems.
     * 3. Tells the View to draw the fetched List of Data.
     * @param view
     * @param dataSource
     */
    public RoutineExerciseController(RoutineExerciseInterface view, DataAccessorInterface dataSource, Routine routine) {
        this.view = view;
        this.dataSource = dataSource;

        RoutineSession latestRoutineSession = dataSource.getLatestRoutineSession(routine);//IdRoutineSession(idRoutine);
        getExercisesFromDataSource(latestRoutineSession);
    }

    public void onExerciseClick(Exercise selectedItem){
        view.startExerciseActivity(selectedItem);
    }

    /**
     * In a real App, I would normally talk to this DataSource using RxJava 2. This is because most
     * calls to Services like a Database/Server should be executed on a seperate thread that the
     * mainThread (UI Thread). See my full projects for examples of this.
     */
    public void getExercisesFromDataSource(RoutineSession routineSession){
        view.setUpAdapterAndView(
                dataSource.getSessionExercises(routineSession)
        );
    }

    public Exercise getExerciseFromSession(SessionExercise sessionExercise)
    {
        return(dataSource.getExerciseFromSession(sessionExercise));
    }

    public void saveExercise(Exercise exercise)
    {
        dataSource.saveExercise(exercise);
    }

    public SessionExercise createBlankSessionExercise(RoutineSession sessionToAddTo) {
        SessionExercise newSessionExercise = dataSource.createBlankSessionExercise(sessionToAddTo);
        //view.addNewSessionExercise(sessionToAddTo);

        return(newSessionExercise);
    }

    public RoutineSession getLatestRoutineSession(Routine routine)
    {
        return(dataSource.getLatestRoutineSession(routine));
    }
}
