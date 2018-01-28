package com.longlife.workoutlogger.controller;

import com.longlife.workoutlogger.model.DataAccessorInterface;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/26/2018.
 */

public class z_RoutineExerciseController {
    private DataAccessorInterface dataSource;

    public z_RoutineExerciseController(DataAccessorInterface dataSource) {
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
}
