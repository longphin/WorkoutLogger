package com.longlife.workoutlogger.data;

import android.arch.persistence.room.*;
import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;
import io.reactivex.Single;

import java.util.List;
import java.util.Set;



/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE hidden = 0")
    // Get a list of exercises that are not hidden.
    public abstract Single<List<Exercise>> getExercises();

    @Query("SELECT Name FROM Exercise WHERE hidden = 0")
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

    @Transaction
    // Insert Exercise and ExerciseHistory. Returns idExercise for the inserted exercise.
    public Long insertExerciseFull(Exercise ex) {
        // Insert exercise.
        Long idExercise = insertExercise(ex);
        ex.setIdExercise(idExercise);

        // Insert history.
        Long idExerciseHistory = insertExerciseHistory(new ExerciseHistory(ex));
        // Update the inserted exercise to look at this inserted history.
        updateIdHistory(idExercise, idExerciseHistory);

        return idExercise;
    }

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert an exercise and returns the id of the new exercise. This should NOT be used directly.
    public abstract Long insertExercise(Exercise ex);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    // Insert an exercise history and returns the id of the new history. This should NOT be used directly.
    public abstract Long insertExerciseHistory(ExerciseHistory eh);

    @Query("UPDATE Exercise SET currentIdExerciseHistory = :idExerciseHistory WHERE idExercise = :idExercise")
    // Update the history id that an exercise points to. This is to keep track of which history is the exercise's most current history.
    public abstract void updateIdHistory(Long idExercise, Long idExerciseHistory);

    @Transaction
    // When updating an exercise, save it into history and update it in the Exercise table. Returns the exercise with the updated history id.
    public Exercise updateExerciseHistoryFull(ExerciseHistory eh, Exercise ex) {
        // Insert history
        Long idExerciseHistory = insertExerciseHistory(eh);
        ex.setCurrentIdExerciseHistory(idExerciseHistory);

        // Update exercise
        updateExercise(ex);

        return ex;
    }

    @Update
    // Update an exercise.
    public abstract void updateExercise(Exercise ex);

    @Transaction
    // When inserting an exercise, save it into history and update it in the Exercise table. Returns the exercise with updated ids.
    public Exercise insertExerciseHistoryFull(Exercise ex) {
        // Insert exercise
        Long idExercise = insertExercise(ex);
        ex.setIdExercise(idExercise);

        // Insert history
        ExerciseHistory exerciseHistory = new ExerciseHistory(ex);
        Long idExerciseHistory = insertExerciseHistory(exerciseHistory);
        ex.setCurrentIdExerciseHistory(idExerciseHistory);

        // Update the exercise with the obtained history id.
        updateIdHistory(idExercise, idExerciseHistory);

        return ex;
    }

    @Delete
    // Delete an exercise. Currently, we do not use this because exercises will only be hidden/unhidden.
    public abstract void deleteExercise(Exercise ex);

    @Query("UPDATE Exercise SET hidden = :isHidden WHERE idExercise = :idExercise")
    // Hide/unhide an exercise.
    public abstract void setExerciseHiddenStatus(Long idExercise, int isHidden); // isHidden = 1 for hidden, 0 for not hidden
}
