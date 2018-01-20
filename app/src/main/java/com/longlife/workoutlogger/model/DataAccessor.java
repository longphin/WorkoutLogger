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
        int idSessionExerciseToFind = sessionExercise.getIdSessionExercise();
        for(SessionExercise se : sessionExercises)
        {
            if (se.getIdSessionExercise() == idSessionExerciseToFind)
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
        int idExerciseToSave = exerciseToSave.getIdExercise();
        for(Exercise exercise : exercises)
        {
            if (exercise.getIdExercise() == idExerciseToSave)
            {
                exercise.setName(exerciseToSave.getName());
                exercise.setDescription(exerciseToSave.getDescription());
            }
        }
    }

    // create a new RoutineSession that will create copies of SessionExercises and Exercise sets as well.
    @Override
    public RoutineSession createRoutineSessionCopy(RoutineSession routineSessionToCopy) {
        // create a copy of the RoutineSession, but with give it a unique ID
        RoutineSession newRoutineSession = new RoutineSession(routineSessionToCopy);

        // add the new RoutineSession to the database
        routineSessions.add(newRoutineSession);

        // find SessionExercises that need to be copied.
        List<Integer> indexesOfSessionExercisesToCopy = new ArrayList<Integer>();
        for(int i = 0; i<sessionExercises.size(); i++)
        {
            if(sessionExercises.get(i).getIdRoutineSession() == routineSessionToCopy.getIdRoutineSession())
            {
                indexesOfSessionExercisesToCopy.add(i);
            }
        }
        // add copies of the SessionExercises to the database
        for(Integer i : indexesOfSessionExercisesToCopy)
        {
            SessionExercise sessionExerciseCopy = new SessionExercise(newRoutineSession, sessionExercises.get(i));
            sessionExercises.add(sessionExerciseCopy);
        }

        // [TODO] need to copy exercise sets as well, when those get implemented
        return(newRoutineSession);
    }

    /* // [TODO] maybe if adding deleteRoutine(), something like this would be implemented for the whole routine
    @Override
    public void deleteRoutineSession(RoutineSession routineSession) {
        List<Integer> indexesToDrop = new ArrayList<Integer>();
        for(int i = sessionExercises.size()-1; i>0; i--)
        {
            if(sessionExercises.get(i).getIdRoutineSession() == routineSession.getIdRoutineSession())
            {
                indexesToDrop.add(i);
            }
        }
        // and then drop those SessionExercises
        for(Integer i : indexesToDrop)
        {
            sessionExercises.remove(i);
        }
        // Note: we iterate backwards to produce indexesToDrop so that when we do a remove, items are not shifted over
        // which would change the indexes when we do a sequence of remove()
        // Collections.sort(indexesToDrop, Collections.reverseOrder()) // If the backwards iteration does not work, this can be done first to reverse the order.
        // Find any SessionExercises linked to the RoutineSession

        // [TODO] need to remove exercise sets when that is implemented

        // There should only be 1 of this index, so we break out of the loop once it is found
        int indexToDrop = -1;
        for(int i = 0; i<routineSessions.size(); i++)
        {
            if(routineSessions.get(i).getIdRoutineSession() == routineSession.getIdRoutineSession())
            {
                indexToDrop = i;
                break;
            }
        }
        // and then drop that index.
        if(indexToDrop != -1)
        {
            routineSessions.remove(indexToDrop);
        }
    }
    */

    @Override
    public void saveRoutine(Routine routineToSave) {
        int idRoutineToSave = routineToSave.getIdRoutine();
        for (Routine routine : routines) {
            if (routine.getIdRoutine() == idRoutineToSave) {
                routine.setName(routineToSave.getName());
                routine.setDescription(routineToSave.getDescription());
            }
        }
    }
}
