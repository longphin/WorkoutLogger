package com.longlife.workoutlogger.adapters;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.longlife.workoutlogger.enums.ExerciseRequestCode;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.utils.ExerciseComparator;
import com.longlife.workoutlogger.utils.RoutineComparator;
import com.longlife.workoutlogger.utils.SessionExerciseComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Longphi on 1/5/2018.
 */


@android.arch.persistence.room.Database(
        entities =
                {
                        Exercise.class,
                        Routine.class,
                        RoutineSession.class,
                        SessionExercise.class,
                        SessionExerciseSet.class
                },
        version = 1
)
public abstract class Database extends RoomDatabase {
    private static HashMap<Integer, Routine> routines = new HashMap<>();// <IdRoutine, Routine> routines;
    private static HashMap<Integer, Exercise> exercises = new HashMap<>();// <IdExercise, Exercise> exercises;
    private static HashMap<Integer, List<RoutineSession>> routineSessionHash = new HashMap<>(); // <idRoutine, List<RoutineSession>>
    private static HashMap<Integer, List<SessionExercise>> sessionExerciseHash = new HashMap(); // <idRoutineSession, List<SessionExercise>>
    private static HashMap<Integer, List<SessionExerciseSet>> sessionExerciseSetHash = new HashMap(); // <idSessionExercise, List<SessionExerciseSet>>

    private static Database INSTANCE;

    private Database() {
        for (int i = 0; i < 9; i++) {
            Routine routineToAdd = new Routine("", "", true);
            routineToAdd.setName("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " name");
            routineToAdd.setDescription("routine " + String.valueOf(routineToAdd.getIdRoutine()) + " description");
            routines.put(routineToAdd.getIdRoutine(), routineToAdd);
        }

        for (int i = 0; i < 9; i++) {
            Exercise exerciseToAdd = new Exercise("", "", ExerciseRequestCode.ExerciseType.BODYWEIGHT, ExerciseRequestCode.MeasurementType.REP, true);
            exerciseToAdd.setName("exercise " + String.valueOf(exerciseToAdd.getIdExercise()) + " name");
            exerciseToAdd.setDescription("exercise " + String.valueOf(exerciseToAdd.getIdExercise()) + " description");
            exercises.put(exerciseToAdd.getIdExercise(), exerciseToAdd);
        }

        // routineSessionHash
        AddToRoutineSessionHash(routines.get(1));
        AddToRoutineSessionHash(routines.get(2));
        AddToRoutineSessionHash(routines.get(3));
        AddToRoutineSessionHash(routines.get(4));

        // sessionExerciseHash
        SessionExercise addedSessionExercise;
        // routine 1
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(1));
        AddToSessionExerciseSetHash(addedSessionExercise, 3);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(2));
        AddToSessionExerciseSetHash(addedSessionExercise, 1);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(3));
        AddToSessionExerciseSetHash(addedSessionExercise, 3);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(4));
        AddToSessionExerciseSetHash(addedSessionExercise, 1);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(5));
        AddToSessionExerciseSetHash(addedSessionExercise, 3);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(6));
        AddToSessionExerciseSetHash(addedSessionExercise, 1);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(7));
        AddToSessionExerciseSetHash(addedSessionExercise, 3);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(8));
        AddToSessionExerciseSetHash(addedSessionExercise, 1);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(1)), exercises.get(9));

        // routine 2
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(2)), exercises.get(3));
        AddToSessionExerciseSetHash(addedSessionExercise, 2);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(2)), exercises.get(2));
        AddToSessionExerciseSetHash(addedSessionExercise, 2);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(2)), exercises.get(3));
        AddToSessionExerciseSetHash(addedSessionExercise, 1);

        // routine 3
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(3)), exercises.get(1));
        AddToSessionExerciseSetHash(addedSessionExercise, 3);
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(3)), exercises.get(1));

        // routine 4
        addedSessionExercise = AddToSessionExerciseHash(getOrCreateLatestRoutineSession(routines.get(4)), exercises.get(1));
    }

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                    .build();
        }
        return (INSTANCE);
    }

    /*
    public static void destroyInstance()
    {
        INSTANCE = null;
    }
    */

    public abstract Dao dao();

    private void AddToRoutineSessionHash(Routine routineToAdd) {
        AddToRoutineSessionHash(routineToAdd.getIdRoutine(), new RoutineSession(routineToAdd));
    }

    private void AddToRoutineSessionHash(RoutineSession routineSessionToAdd) {
        AddToRoutineSessionHash(routineSessionToAdd.getIdRoutine(), routineSessionToAdd);
    }

    private void AddToRoutineSessionHash(Integer idRoutine, RoutineSession routineSessionToAdd) {
        if (routineSessionHash.get(idRoutine) == null) {
            routineSessionHash.put(idRoutine, new ArrayList<RoutineSession>());
        }
        routineSessionHash.get(idRoutine).add(routineSessionToAdd);
    }

    private SessionExercise AddToSessionExerciseHash(RoutineSession routineSession, Exercise exercise) {
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
        AddToSessionExerciseSetHash(sessionExercise.getIdSessionExercise(), new SessionExerciseSet(sessionExercise));
    }

    private void AddToSessionExerciseSetHash(SessionExercise sessionExercise, int numberOfSets) {
        for (int i = 0; i < numberOfSets; i++) {
            AddToSessionExerciseSetHash(sessionExercise.getIdSessionExercise(), new SessionExerciseSet(sessionExercise));
        }
    }

    private void AddToSessionExerciseSetHash(Integer idSessionExercise, SessionExerciseSet sessionExerciseSetToAdd) {
        if (sessionExerciseSetHash.get(idSessionExercise) == null) {
            sessionExerciseSetHash.put(idSessionExercise, new ArrayList<SessionExerciseSet>());
        }
        sessionExerciseSetHash.get(idSessionExercise).add(sessionExerciseSetToAdd);
    }

    public List<Exercise> getExercises() {
        List<Exercise> exercisesList = new ArrayList<Exercise>(exercises.values());

        Collections.sort(exercisesList, new ExerciseComparator());

        return (exercisesList);
    }

    public List<Routine> getRoutines() {
        List<Routine> routinesList = new ArrayList<Routine>(routines.values());

        Collections.sort(routinesList, new RoutineComparator());

        return routinesList;
    }

    public RoutineSession getLatestRoutineSession(Routine routine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;
        RoutineSession latestRoutineSession = new RoutineSession();

        List<RoutineSession> routineSessions = routineSessionHash.get(routine.getIdRoutine());
        if (routineSessions == null) {
            routineSessionHash.put(routine.getIdRoutine(), new ArrayList<RoutineSession>());
            return (latestRoutineSession);
        }

        for (RoutineSession rs : routineSessions) {
            if (latestIdRoutineSession == -1 || rs.getSessionDate().after(latestRoutineDate)) {
                latestRoutineDate = rs.getSessionDate();
                latestIdRoutineSession = rs.getIdRoutineSession();
                latestRoutineSession = rs;
            }
        }

        return (latestRoutineSession);
    }

    public RoutineSession getOrCreateLatestRoutineSession(Routine routine) {
        Date latestRoutineDate = new Date();
        int latestIdRoutineSession = -1;
        RoutineSession latestRoutineSession = new RoutineSession();

        if (routine == null)
            throw new NullPointerException("Could not create session for null routine.");

        // Initialize the RoutineSessionHash(idRoutine) if needed, or if no RoutineSession exists.
        List<RoutineSession> routineSessions = routineSessionHash.get(routine.getIdRoutine());
        if (routineSessions == null || routineSessions.size() == 0) {
            RoutineSession createdRoutineSession = createRoutineSessionCopy(latestRoutineSession);
            return (createdRoutineSession);
        }

        // If there exist RoutineSessions, then find the latest one.
        for (RoutineSession rs : routineSessions) {
            if (latestIdRoutineSession == -1 ||
                    //latestRoutineSession.getIdRoutine() == -1 ||
                    rs.getSessionDate().after(latestRoutineDate)) {
                latestRoutineDate = rs.getSessionDate();
                latestIdRoutineSession = rs.getIdRoutineSession();
                latestRoutineSession = rs;
            }
        }

        // Check if the picked RoutineSession was performed. If it was, then return it. If not,
        // then return a copy of it.
        if (!latestRoutineSession.getWasPerformed()) {
            // If the latest RoutineSession was not performed, then set it as thisRoutineSession.
            return (latestRoutineSession);
        } else {// Else, create a copy of the session.
            RoutineSession createdRoutineSession = createRoutineSessionCopy(latestRoutineSession);
            return (createdRoutineSession);
        }
    }

    public List<SessionExercise> getSessionExercises(RoutineSession routineSession) {
        List<SessionExercise> sessionExercises = sessionExerciseHash.get(routineSession.getIdRoutineSession());
        if (sessionExercises == null) return (null);

        Collections.sort(sessionExercises, new SessionExerciseComparator());
        return (sessionExercises);
    }

    public Exercise getExerciseFromSession(SessionExercise sessionExercise) {
        Exercise foundExercise = exercises.get(sessionExercise.getIdExercise());
        if (foundExercise != null) return (foundExercise);

        /*
        int idExerciseToFind = sessionExercise.getIdExercise();
        for(int i = 0; i<exercises.size(); i++)
        {
            if(exercises.get(i).getIdExercise() == idExerciseToFind)
            {
                return(exercises.get(i));
            }
        }
        */
        return (null);
    }

    public void saveExercise(Exercise exerciseToSave) {
        exercises.put(exerciseToSave.getIdExercise(), exerciseToSave);
        /*
        int idExerciseToSave = exerciseToSave.getIdExercise();
        for (Exercise exercise : exercises) {
            if (exercise.getIdExercise() == idExerciseToSave) {
                exercise.setName(exerciseToSave.getName());
                exercise.setDescription(exerciseToSave.getDescription());
            }
        }
        */
    }

    // create a new RoutineSession that will create copies of SessionExercises and Exercise sets as well.
    public RoutineSession createRoutineSessionCopy(RoutineSession routineSessionToCopy) {
        // create a copy of the RoutineSession, but with give it a unique ID
        RoutineSession newRoutineSession = new RoutineSession(routineSessionToCopy);

        // add the new RoutineSession to the database
        //routineSessions.add(newRoutineSession);
        AddToRoutineSessionHash(newRoutineSession);

        List<SessionExercise> sessionExercises = sessionExerciseHash.get(newRoutineSession.getIdRoutineSession());
        if (sessionExercises == null || sessionExercises.size() == 0) {
            sessionExerciseHash.put(newRoutineSession.getIdRoutineSession(), new ArrayList<SessionExercise>());
            return (newRoutineSession);
        }

        // find SessionExercises that need to be copied.
        for (SessionExercise se : sessionExercises) {
            SessionExercise sessionExerciseCopy = new SessionExercise(newRoutineSession, se);
            AddToSessionExerciseHash(sessionExerciseCopy.getIdRoutineSession(), sessionExerciseCopy);

            List<SessionExerciseSet> exerciseSets = sessionExerciseSetHash.get(se.getIdSessionExercise());
            if (exerciseSets == null) {
                sessionExerciseSetHash.put(se.getIdSessionExercise(), new ArrayList<SessionExerciseSet>());
                break;
            }

            // Insert SessionExerciseSets copies for the SessionExerciseCopy.
            for (SessionExerciseSet ses : sessionExerciseSetHash.get(se.getIdSessionExercise())) {
                AddToSessionExerciseSetHash(sessionExerciseCopy.getIdSessionExercise(), new SessionExerciseSet(ses));
            }
        }

        return (newRoutineSession);
    }

    public void saveRoutine(Routine routineToSave) {
        routines.put(routineToSave.getIdRoutine(), routineToSave);
        /*
        int idRoutineToSave = routineToSave.getIdRoutine();
        for (Routine routine : routines) {
            if (routine.getIdRoutine() == idRoutineToSave) {
                routine.setName(routineToSave.getName());
                routine.setDescription(routineToSave.getDescription());
            }
        }
        */
    }

    public HashMap<Integer, List<SessionExerciseSet>> getSessionExerciseSetHash() {
        return (sessionExerciseSetHash);
    }

    public List<SessionExerciseSet> getSessionExerciseSets(SessionExercise sessionExercise) {
        return (sessionExerciseSetHash.get(sessionExercise.getIdSessionExercise()));
    }

    public ExerciseRequestCode.ExerciseType getExerciseType(SessionExerciseSet sessionExerciseSet) {
        Exercise exercise = exercises.get(sessionExerciseSet.getIdExercise());
        if (exercise != null) {
            return (exercise.getExerciseType());
        }

        return (null);
    }

    public ExerciseRequestCode.MeasurementType getMeasurementType(SessionExerciseSet sessionExerciseSet) {
        Exercise exercise = exercises.get(sessionExerciseSet.getIdExercise());
        if (exercise != null) {
            return (exercise.getMeasurementType());
        }

        return (null);
    }

    public void addValueToExerciseSet(SessionExerciseSet sesToInsertInto, int s) {
        List<SessionExerciseSet> listOfSets = sessionExerciseSetHash.get(sesToInsertInto.getIdSessionExercise());

        int idSesToInsertInto = sesToInsertInto.getIdSessionExerciseSet();
        for (SessionExerciseSet ses : listOfSets) {
            if (ses.getIdSessionExerciseSet() == idSesToInsertInto) {
                String currentVal = "";

                Double currentWeights = ses.getWeights();
                if (currentWeights != null) {
                    currentVal = ses.getWeights().toString();
                }

                currentVal += String.valueOf(s);
                ses.setWeights(Double.parseDouble(currentVal));
                return; // exit after added
            }
        }
    }
}