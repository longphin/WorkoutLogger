package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public interface ExerciseDao {
    ///
    /// Gets
    ///
    @Query("SELECT * FROM Exercise")
//" ORDER BY favorited DESC, LOWER(name) ASC")
    Single<List<Exercise>> getExercises();

    @Query("SELECT idExercise FROM Exercise WHERE name = :name")
        // check if the exercise exists in the database already
    Maybe<Integer> getExercise(String name);

    @Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
    int exerciseExists(int idExercise);

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

    ///
    /// Deletes
    ///
    @Delete
    void deleteExercise(Exercise ex);
}
