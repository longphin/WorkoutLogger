package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.RoutineExerciseInterface;

import java.util.List;

/**
 * Created by Longphi on 1/6/2018.
 */

public class RoutineExerciseController {
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
        view.setSessionExercises(
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

    public RoutineSession getLatestRoutineSession(Routine routine)
    {
        return(dataSource.getLatestRoutineSession(routine));
    }

    public RoutineSession createRoutineSessionCopy(RoutineSession routineSession)
    {
        return(dataSource.createRoutineSessionCopy(routineSession));
    }

    public List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise) {
        return (dataSource.getSessionExerciseSets(sessionExercise));
    }

    /*
    public void deleteRoutineSession(RoutineSession routineSession)
    {
        dataSource.deleteRoutineSession(routineSession);
    }
    */
    /*
    private static HashMap<Integer, List<RoutineSession>> routineSessionHash; // <idRoutine, List<RoutineSession>>
    private static HashMap<Integer, List<SessionExercise>> sessionExerciseHash; // <idRoutineSession, List<SessionExercise>>
    private static HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash; // <idSessionExercise, List<SessionExerciseSet>>

     */
}
