package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.model.RoutineSession;
import com.longlife.workoutlogger.v2.model.SessionExercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public interface Dao {
    ///
    /// Gets
    ///
    @Query("SELECT * FROM Exercise ORDER BY favorited DESC, LOWER(name) ASC")
    Single<List<Exercise>> getExercises();

    @Query("SELECT idExercise FROM Exercise WHERE name = :name")
        // check if the exercise exists in the database already
    Maybe<Integer> getExercise(String name);

    @Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
    int exerciseExists(int idExercise);

    @Query("SELECT * FROM Routine")
    List<Routine> getRoutines();

    @Query("SELECT * FROM RoutineSession WHERE idRoutine = :idRoutine AND wasPerformed = 0 ORDER BY sessionDate DESC LIMIT 1")
    RoutineSession getLatestRoutineSession(int idRoutine);

    @Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
    List<SessionExercise> getSessionExercises(int idRoutineSession);

    @Query("SELECT e.*" +
            " FROM SessionExercise as se" +
            " INNER JOIN Exercise as e on se.idExercise=e.idExercise" +
            " WHERE se.idSessionExercise = :idSessionExercise")
    Exercise getExerciseFromSession(int idSessionExercise);

    @Query("SELECT * FROM Routine WHERE idRoutine = :idRoutine")
    Routine getRoutine(int idRoutine);

    ///
    /// UPDATE
    ///
    @Query("UPDATE Exercise SET favorited = :favorited WHERE idExercise = :idExercise")
    void updateFavorite(int idExercise, boolean favorited);

    ///
    /// Inserts
    ///
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertExercises(ArrayList<Exercise> ex);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    Long insertExercise(Exercise ex);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    void insertRoutines(ArrayList<Routine> r);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    Long insertRoutine(Routine r);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRoutineSession(RoutineSession rs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSessionExercises(ArrayList<SessionExercise> se);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSessionExerciseSets(ArrayList<SessionExerciseSet> ses);

    ///
    /// Deletes
    ///
    @Delete
    void deleteExercise(Exercise ex);

    @Delete
    void deleteRoutine(Routine r);

    @Delete
    void deleteRoutineSession(RoutineSession rs);

    @Delete
    void deleteSessionExercise(SessionExercise se);

    @Delete
    void deleteSessionExerciseSet(SessionExerciseSet ses);
}
