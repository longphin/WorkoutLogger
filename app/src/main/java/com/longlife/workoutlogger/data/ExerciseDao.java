package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseSessionWithSets;
import com.longlife.workoutlogger.model.SessionExercise;

import java.util.List;
import java.util.Set;

import io.reactivex.Maybe;
import io.reactivex.Single;



/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE hidden = 0 AND idExerciseSource IS NULL")
    // Get a list of exercises that are not hidden.
    public abstract Single<List<Exercise>> getExercises();

    @Query("SELECT Name FROM Exercise WHERE hidden = 0 AND idExerciseSOurce IS NULL")
    // Get the name of exercises that are not hidden.
    public abstract Single<List<String>> getExercisesNames();

    @Query("SELECT * FROM Exercise WHERE name = :name")
    // Check if an exercise with the given name already exists.
    public abstract Single<Exercise> getExercise(String name);

    @Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
    // Check if an exercise with the given id already exists.
    public abstract Single<Integer> exerciseExists(Long idExercise);

    @Query("SELECT * FROM Exercise WHERE idExercise IN (:ids)")
    // Get a list of exercises given a list of idExercises.
    public abstract Single<List<Exercise>> getExerciseFromId(Set<Long> ids);

    @Query("SELECT * FROM Exercise WHERE idExercise=:id")
    // Get a single exercise given the id.
    public abstract Single<Exercise> getExerciseFromId(Long id);

    @Query("UPDATE Exercise SET locked = :lockedStatus WHERE idExercise = :idExercise")
    // Update the lock status of an exercise.
    public abstract void updateLockedStatus(Long idExercise, boolean lockedStatus);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert an exercise and returns the id of the new exercise. This should NOT be used directly.
    public abstract Long insertExercise(Exercise ex);

    @Transaction
    @Query("SELECT * FROM SessionExercise WHERE idSessionExercise=:idSessionExercise")
    // Get session exercise with related sets.
    public abstract Single<ExerciseSessionWithSets> getSessionExerciseWithSets(Long idSessionExercise);

    @Query("SELECT se.*" +
            "FROM Exercise as e" +
            " INNER JOIN SessionExercise as se ON se.idExercise=e.idExercise" +
            " INNER JOIN RoutineSession as rs ON rs.idRoutineSession=se.idRoutineSession " +
            "WHERE e.idExercise=:idExercise " +
            "AND rs.performanceStatus=0 " + // Only look for new sessions, so we do not have to recreate a new session. // [TODO] Figure out a way to not hard-code this value. Get it from the enum.
            "AND rs.idRoutineHistory IS NULL " + // Only look for sessions specifically for this exercise, not related to a routine.
            "LIMIT 1")
    // Get the latest unperformed session for an exercise.
    public abstract Maybe<SessionExercise> getLatestExerciseSession(Long idExercise);

    // Update exercise source and save it to history.
    @Transaction
    public Exercise updateExerciseFull(Exercise source) {
        Exercise leaf = new Exercise(source);

        // Insert leaf node.
        Long idLeaf = insertExercise(leaf);
        leaf.setIdExerciseLeaf(null);
        leaf.setIdExercise(idLeaf);

        // Point the current row to the newly inserted leaf node.
        source.setIdExerciseLeaf(idLeaf);
        source.setCreateDateAsNow();
        updateExercise(source);

        return source;
    }

    @Update
    // Update an exercise.
    public abstract void updateExercise(Exercise ex);

    @Transaction
    // When inserting an exercise, save it into history and update it in the Exercise table. Returns the exercise with updated ids.
    public Exercise insertExerciseHistoryFull(Exercise source) {
        // Insert exercise
        Long idSource = insertExercise(source);
        source.setIdExercise(idSource);

        // Insert history
        Exercise leaf = new Exercise(source);
        //leaf.setIdExercise(null); // Copy constructor does not copy idExercise.
        leaf.setIdExerciseSource(idSource);
        Long idLeaf = insertExercise(leaf);
        leaf.setIdExercise(idLeaf); // Technically don't need to do this because leaf is never used.

        // Update the exercise with the obtained history id.
        updateIdLeaf(idSource, idLeaf);
        source.setIdExerciseLeaf(idLeaf);

        return source;
    }

    @Query("UPDATE Exercise SET idExerciseLeaf = :idExerciseLeaf WHERE idExercise = :idExercise")
    // Update the history id that an exercise points to. This is to keep track of which history is the exercise's most current history.
    public abstract void updateIdLeaf(Long idExercise, Long idExerciseLeaf);

    @Delete
    // Delete an exercise. Currently, we do not use this because exercises will only be hidden/unhidden.
    public abstract void deleteExercise(Exercise ex);

    @Query("UPDATE Exercise SET hidden = :isHidden WHERE idExercise = :idExercise")
    // Hide/unhide an exercise.
    public abstract void setExerciseHiddenStatus(Long idExercise, int isHidden); // isHidden = 1 for hidden, 0 for not hidden
}
