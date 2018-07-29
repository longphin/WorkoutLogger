package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

// Inner Classes

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public interface ExerciseDao
{
	// Getters
	///
	/// Gets
	///
	@Query("SELECT * FROM Exercise WHERE hidden = 0")
	//" ORDER BY favorited DESC, LOWER(name) ASC")
	Flowable<List<Exercise>> getExercises();
	
	@Query("SELECT * FROM Exercise WHERE name = :name")
		// check if the exercise exists in the database already
	Flowable<Exercise> getExercise(String name);
	
	@Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
	Maybe<Integer> exerciseExists(int idExercise);
	
	@Query("SELECT * FROM Exercise WHERE idExercise IN (:ids)")
		//Flowable<List<Exercise>> getExerciseFromId(List<Integer> ids);
	Flowable<List<Exercise>> getExerciseFromId(Set<Integer> ids);
	
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
	// [TODO] Need to check if the exercise is tied to a session, then we use setExerciseAsHidden() instead. Otherwise, we can delete.
	@Delete
	void deleteExercise(Exercise ex);
	
	@Query("UPDATE Exercise SET hidden = :isHidden WHERE idExercise = :idExercise")
	int setExerciseAsHidden(int idExercise, int isHidden); // isHidden = 1 for hidden, 0 for not hidden
}
