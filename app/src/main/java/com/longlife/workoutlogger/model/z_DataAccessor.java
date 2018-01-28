package com.longlife.workoutlogger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/5/2018.
 */

public class z_DataAccessor implements DataAccessorInterface {
    private static List<Routine> routines;
    private static List<Exercise> exercises;
    //private static List<RoutineSession> routineSessions;
    //private static List<SessionExercise> sessionExercises;
    //private static List<SessionExerciseSet> sessionExerciseSets;
    private static boolean dataIsCreated = false;
    // hashmaps
    private static HashMap<Integer, List<RoutineSession>> routineSessionHash = new HashMap<>(); // <idRoutine, List<RoutineSession>>
    private static HashMap<Integer, List<SessionExercise>> sessionExerciseHash = new HashMap(); // <idRoutineSession, List<SessionExercise>>
    private static HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash = new HashMap(); // <idSessionExercise, List<SessionExerciseSet>>

    public z_DataAccessor() {
        if (!dataIsCreated) {
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

            // routineSessionHash
            AddToRoutineSessionHash(routines.get(0));
            AddToRoutineSessionHash(routines.get(1));
            AddToRoutineSessionHash(routines.get(2));
            AddToRoutineSessionHash(routines.get(3));

            // sessionExerciseHash
            SessionExercise addedSessionExercise;
            // routine 1
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(0)), exercises.get(0));
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(0)), exercises.get(1));
            AddToSessionExerciseSetHash(addedSessionExercise);

            // routine 2
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(2));
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(1));
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(2));
            AddToSessionExerciseSetHash(addedSessionExercise);

            // routine 3
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(2)), exercises.get(0));
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            AddToSessionExerciseSetHash(addedSessionExercise);
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(2)), exercises.get(0));

            // routine 4
            addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(3)), exercises.get(0));

            /*
            // sessionExerciseHash
            AddToSessionExerciseHash(routineSessions.get(0), exercises.get(0));
            AddToSessionExerciseHash(routineSessions.get(0), exercises.get(1));
            AddToSessionExerciseHash(routineSessions.get(0), exercises.get(0));

            AddToSessionExerciseHash(routineSessions.get(1), exercises.get(0));

            AddToSessionExerciseHash(routineSessions.get(2), exercises.get(1));
            AddToSessionExerciseHash(routineSessions.get(2), exercises.get(2));

            // sessionExerciseSetHash
            AddToSessionExerciseSetHash(sessionExercises.get(0));
            AddToSessionExerciseSetHash(sessionExercises.get(0));
            AddToSessionExerciseSetHash(sessionExercises.get(0));
            AddToSessionExerciseSetHash(sessionExercises.get(1));
            AddToSessionExerciseSetHash(sessionExercises.get(1));
            AddToSessionExerciseSetHash(sessionExercises.get(2));

            AddToSessionExerciseSetHash(sessionExercises.get(3));
            AddToSessionExerciseSetHash(sessionExercises.get(3));
            AddToSessionExerciseSetHash(sessionExercises.get(3));
            AddToSessionExerciseSetHash(sessionExercises.get(3));
            */

            dataIsCreated = true;
        }
    }

    private void AddToRoutineSessionHash(Routine routineToAdd) {
        /*
        if(routineSessionHash.get(routineToAdd.getIdRoutine()) == null)
        {
            routineSessionHash.put(routineToAdd.getIdRoutine(), new ArrayList<RoutineSession>());
        }
        routineSessionHash.get(routineToAdd.getIdRoutine()).add(new RoutineSession(routineToAdd));
        */
        AddToRoutineSessionHash(routineToAdd.getIdRoutine(), new RoutineSession(routineToAdd));
    }

    private void AddToRoutineSessionHash(RoutineSession routineSessionToAdd) {
        /*
        if(routineSessionHash.get(routineSessionToAdd.getIdRoutine()) == null)
        {
            routineSessionHash.put(routineSessionToAdd.getIdRoutine(), new ArrayList<RoutineSession>());
        }
        routineSessionHash.get(routineSessionToAdd.getIdRoutine()).add(routineSessionToAdd);
        */
        AddToRoutineSessionHash(routineSessionToAdd.getIdRoutine(), routineSessionToAdd);
    }

    private void AddToRoutineSessionHash(Integer idRoutine, RoutineSession routineSessionToAdd) {
        if (routineSessionHash.get(idRoutine) == null) {
            routineSessionHash.put(idRoutine, new ArrayList<RoutineSession>());
        }
        routineSessionHash.get(idRoutine).add(routineSessionToAdd);
    }

    private SessionExercise AddToSessionExerciseHash(RoutineSession routineSession, Exercise exercise) {
        /*
        if(sessionExerciseHash.get(routineSession.getIdRoutineSession()) == null)
        {
            sessionExerciseHash.put(routineSession.getIdRoutineSession(), new ArrayList<SessionExercise>());
        }
        sessionExerciseHash.get(routineSession.getIdRoutineSession()).add(new SessionExercise(routineSession, exercise));
        */
        return (AddToSessionExerciseHash(routineSession.getIdRoutineSession(), new SessionExercise(routineSession, exercise)));
    }

    private SessionExercise AddToSessionExerciseHash(Integer idRoutineSession, SessionExercise sessionExerciseToAdd) {
        if (sessionExerciseHash.get(idRoutineSession) == null) {
            sessionExerciseHash.put(idRoutineSession, new ArrayList<SessionExercise>());
        }
        sessionExerciseHash.get(idRoutineSession).add(sessionExerciseToAdd);

        return (sessionExerciseToAdd);
    }

    private void AddToSessionExerciseSetHash(SessionExercise sessionExercise) {
        /*
        if(sessionExerciseSetHash.get(sessionExercise.getIdSessionExercise()) == null)
        {
            sessionExerciseSetHash.put(sessionExercise.getIdSessionExercise(), new ArrayList<SessionExerciseSet>());
        }
        sessionExerciseSetHash.get(sessionExercise.getIdSessionExercise()).add(new SessionExerciseSet(sessionExercise));
        */
        AddToSessionExerciseSetHash(sessionExercise.getIdSessionExercise(), new SessionExerciseSet(sessionExercise));
    }

    private void AddToSessionExerciseSetHash(Integer idSessionExercise, SessionExerciseSet sessionExerciseSetToAdd) {
        if (sessionExerciseSetHash.get(idSessionExercise) == null) {
            sessionExerciseSetHash.put(idSessionExercise, new ArrayList<SessionExerciseSet>());
        }
        sessionExerciseSetHash.get(idSessionExercise).add(sessionExerciseSetToAdd);
    }

    @Override
    public List<Exercise> getExercises() {
        return (exercises);
    }

    @Override
    public List<Routine> getRoutines() {
        return (routines);
    }

    @Override
    public RoutineSession getLatestRoutineSession(Routine routine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;
        RoutineSession latestRoutineSession = new RoutineSession();

        List<RoutineSession> routineSessions = routineSessionHash.get(routine.getIdRoutine());
        if (routineSessions == null) return (latestRoutineSession);

        for (RoutineSession rs : routineSessionHash.get(routine.getIdRoutine())) {
            if (latestIdRoutineSession == -1 || rs.getSessionDate().after(latestRoutineDate)) {
                latestRoutineDate = rs.getSessionDate();
                latestIdRoutineSession = rs.getIdRoutineSession();
                latestRoutineSession = rs;
            }
        }

        return (latestRoutineSession);
    }

    // [TODO] make this an overloaded function instead the interace
    public RoutineSession getOrCreateLatestRoutineSession(Routine routine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;
        RoutineSession latestRoutineSession = new RoutineSession();

        for (RoutineSession rs : routineSessionHash.get(routine.getIdRoutine())) {
            if (rs.getIdRoutine() == routine.getIdRoutine()) {
                if (latestIdRoutineSession == -1 || rs.getSessionDate().after(latestRoutineDate)) {
                    latestRoutineDate = rs.getSessionDate();
                    latestIdRoutineSession = rs.getIdRoutineSession();
                    latestRoutineSession = rs;
                }
            }
        }

        if (!latestRoutineSession.getWasPerformed())
            // If the latest RoutineSession was not performed, then set it as thisRoutineSession.
            return (latestRoutineSession);
        else
            // Else, create a copy of the session.
            return (createRoutineSessionCopy(latestRoutineSession));
    }

    @Override
    public List<SessionExercise> getSessionExercises(RoutineSession routineSession) {
        return (sessionExerciseHash.get(routineSession.getIdRoutineSession()));
    }

    @Override
    public Exercise getExerciseFromSession(SessionExercise sessionExercise) {
        int idSessionExerciseToFind = sessionExercise.getIdSessionExercise();
        for (SessionExercise se : sessionExerciseHash.get(sessionExercise.getIdRoutineSession())) {
            if (se.getIdSessionExercise() == idSessionExerciseToFind) {
                // now that we found the idSessionExercise, find the exercise
                for (Exercise exercise : exercises) {
                    if (exercise.getIdExercise() == se.getIdExercise()) {
                        return (exercise);
                    }
                }
            }
        }

        return (null);
    }

    @Override
    public void saveExercise(Exercise exerciseToSave) {
        int idExerciseToSave = exerciseToSave.getIdExercise();
        for (Exercise exercise : exercises) {
            if (exercise.getIdExercise() == idExerciseToSave) {
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
        //routineSessions.add(newRoutineSession);
        AddToRoutineSessionHash(newRoutineSession);

        // find SessionExercises that need to be copied.
        for (SessionExercise se : sessionExerciseHash.get(newRoutineSession.getIdRoutineSession())) {
            SessionExercise sessionExerciseCopy = new SessionExercise(newRoutineSession, se);
            AddToSessionExerciseHash(sessionExerciseCopy.getIdRoutineSession(), sessionExerciseCopy);

            // Insert SessionExerciseSets copies for the SessionExerciseCopy.
            for (SessionExerciseSet ses : sessionExerciseSetHash.get(se.getIdSessionExercise())) {
                AddToSessionExerciseSetHash(sessionExerciseCopy.getIdSessionExercise(), new SessionExerciseSet(ses));
            }
        }

        return (newRoutineSession);
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

    @Override
    public HashMap<Integer, List<SessionExerciseSet>> getSessionExerciseSetHash() {
        return (sessionExerciseSetHash);
    }

    @Override
    public List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise) {
        return (sessionExerciseSetHash.get(sessionExercise.getIdSessionExercise()));
    }
}
