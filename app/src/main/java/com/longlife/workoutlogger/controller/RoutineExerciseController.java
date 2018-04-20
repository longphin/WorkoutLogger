package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.adapters.DataAccessorInterface;
import com.longlife.workoutlogger.enums.ExerciseType;
import com.longlife.workoutlogger.enums.MeasurementType;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/26/2018.
 */

public class RoutineExerciseController {
    private DataAccessorInterface dataSource;

    public RoutineExerciseController(DataAccessorInterface dataSource) {
        this.dataSource = dataSource;

        //RoutineSession latestRoutineSession = dataSource.getLatestRoutineSession(routine);//IdRoutineSession(idRoutine);
        //getExercisesFromDataSource(latestRoutineSession);
    }

    public List<SessionExercise> getSessionExercisesFromRoutineSession(RoutineSession routineSession) {
        return (dataSource.getSessionExercises(routineSession));
    }

    public RoutineSession getLatestRoutineSession(Routine routine) {
        return (dataSource.getLatestRoutineSession(routine));
    }

    public RoutineSession createRoutineSessionCopy(RoutineSession routineSession) {
        return (dataSource.createRoutineSessionCopy(routineSession));
    }

    public HashMap<Integer, List<SessionExerciseSet>> getSessionExerciseSetHash() {
        return (dataSource.getSessionExerciseSetHash());
    }

    public RoutineSession getOrCreateLatestRoutineSession(Routine routine) {
        return (dataSource.getOrCreateLatestRoutineSession(routine));
    }

    /*
        private ExerciseType exerciseType; // The type of exercise, such as weight, bodyweight, distance.
    private MeasurementType measurementType; // The measurement of the exercise, such as reps or duration.
     */
    public ExerciseType getExerciseType(SessionExerciseSet sessionExerciseSet) {
        return (dataSource.getExerciseType(sessionExerciseSet));
    }

    public MeasurementType getMeasurementType(SessionExerciseSet sessionExerciseSet) {
        return (dataSource.getMeasurementType(sessionExerciseSet));
    }

    public void addValueToExerciseSet(SessionExerciseSet ses, int s) {
        dataSource.addValueToExerciseSet(ses, s);
    }
}
