package com.longlife.workoutlogger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Longphi on 1/5/2018.
 */

public class DataAccessor implements DataAccessorInterface {
    private static List<Routine> routines;
    private static List<Exercise> exercises;
    private static List<RoutineSession> routineSessions;
    private static List<SessionExercise> sessionExercises;
    private static boolean dataIsCreated = false;

    public DataAccessor()
    {
        if(!dataIsCreated) {
            // sample routines
            routines = new ArrayList<Routine>();
            routines.add(new Routine("routine1", "1st routine", true));
            routines.add(new Routine("routine2", "2nd routine", true));
            routines.add(new Routine("routine3", "3rd routine", true));
            routines.add(new Routine("routine4", "4th routine", true));
            routines.add(new Routine("routine5", "5th routine", true));
            routines.add(new Routine("routine6", "6th routine", true));
            routines.add(new Routine("routine7", "7th routine", true));
            routines.add(new Routine("routine8", "8th routine", true));
            routines.add(new Routine("routine9", "9th routine", true));

            // sample exercises
            exercises = new ArrayList<Exercise>();
            exercises.add(new Exercise("exercise1", "1st exercise", true));
            exercises.add(new Exercise("exercise2", "2nd exercise", true));
            exercises.add(new Exercise("exercise3", "3rd exercise", true));

            // for each routine, create a session
            routineSessions = new ArrayList<RoutineSession>();
            for (int i = 0; i < routines.size(); i++) {
                routineSessions.add(new RoutineSession(routines.get(i)));
            }

            // sample sessionExercises
            sessionExercises = new ArrayList<SessionExercise>();
            sessionExercises.add(new SessionExercise(routineSessions.get(0), exercises.get(0)));
            sessionExercises.add(new SessionExercise(routineSessions.get(0), exercises.get(1)));
            sessionExercises.add(new SessionExercise(routineSessions.get(0), exercises.get(0)));

            sessionExercises.add(new SessionExercise(routineSessions.get(1), exercises.get(0)));

            sessionExercises.add(new SessionExercise(routineSessions.get(2), exercises.get(1)));
            sessionExercises.add(new SessionExercise(routineSessions.get(2), exercises.get(2)));

            dataIsCreated = true;
        }
    }

    @Override
    public List<Exercise> getExercises() {
        return(exercises);
    }

    @Override
    public List<Routine> getRoutines() {
        return(routines);
    }

    /*
    @Override
    public int getLatestIdRoutineSession(int idRoutine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;

        for(RoutineSession rs : routineSessions)
        {
            if(rs.getIdRoutine() == idRoutine)
            {
                if(latestIdRoutineSession == -1 || rs.getSessionDate().after(latestRoutineDate))
                {
                    latestRoutineDate = rs.getSessionDate();
                    latestIdRoutineSession = rs.getIdRoutineSession();
                }
            }
        }

        return(latestIdRoutineSession);
    }
    */

    @Override
    public RoutineSession getLatestRoutineSession(Routine routine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;
        RoutineSession latestRoutineSession = new RoutineSession();

        for(RoutineSession rs : routineSessions)
        {
            if(rs.getIdRoutine() == routine.getIdRoutine())
            {
                if(latestIdRoutineSession == -1 || rs.getSessionDate().after(latestRoutineDate))
                {
                    latestRoutineDate = rs.getSessionDate();
                    latestIdRoutineSession = rs.getIdRoutineSession();
                    latestRoutineSession = rs;
                }
            }
        }

        return(latestRoutineSession);
    }

    @Override
    public List<SessionExercise> getSessionExercises(RoutineSession routineSession) {
        List<SessionExercise> exercisesInThisRoutineSession = new ArrayList<SessionExercise>();

        for(SessionExercise se : sessionExercises)
        {
            if(se.getIdRoutineSession() == routineSession.getIdRoutineSession())
            {
                exercisesInThisRoutineSession.add(se);
            }
        }
        return(exercisesInThisRoutineSession);
    }

    @Override
    public Exercise getExerciseFromSession(SessionExercise sessionExercise) {
        for(SessionExercise se : sessionExercises)
        {
            if(se.getIdSessionExercise() == sessionExercise.getIdSessionExercise())
            {
                // now that we found the idSessionExercise, find the exercise
                for(Exercise exercise : exercises)
                {
                    if(exercise.getIdExercise() == se.getIdExercise())
                    {
                        return(exercise);
                    }
                }
            }
        }

        return(null);
    }

    @Override
    public void saveExercise(Exercise exerciseToSave) {

        for(Exercise exercise : exercises)
        {
            if(exerciseToSave.getIdExercise() == exercise.getIdExercise())
            {
                exercise.setName(exerciseToSave.getName());
                exercise.setDescription(exerciseToSave.getDescription());
            }
        }
    }

    @Override
    public SessionExercise createBlankSessionExercise(RoutineSession sessionToAddTo) {
        Exercise newExercise = new Exercise("unnamed", "");
        SessionExercise newSessionExercise = new SessionExercise(sessionToAddTo, newExercise); // [TODO] create a new instance of SessionExercise. Needs Routine and Exercise.

        return(newSessionExercise);
    }


}
