package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.ExerciseHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;

// Inner Classes

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class ExerciseDao
{
	// Getters
	///
	/// Gets
	///
	@Query("SELECT * FROM Exercise WHERE hidden = 0")
	//" ORDER BY favorited DESC, LOWER(name) ASC")
	public abstract Single<List<Exercise>> getExercises();
	
	@Query("SELECT * FROM Exercise WHERE name = :name")
		// check if the exercise exists in the database already
	public abstract Single<Exercise> getExercise(String name);
	
	@Query("SELECT EXISTS (SELECT 1 FROM Exercise WHERE idExercise = :idExercise)")
	public abstract Single<Integer> exerciseExists(Long idExercise);
	
	@Query("SELECT * FROM Exercise WHERE idExercise IN (:ids)")
	public abstract Single<List<Exercise>> getExerciseFromId(Set<Long> ids);
	
	@Query("SELECT * FROM Exercise WHERE idExercise=:id")
	public abstract Single<Exercise> getExerciseFromId(Long id);
	
	///
	/// UPDATE
	///
	@Query("UPDATE Exercise SET favorited = :favorited WHERE idExercise = :idExercise")
	public abstract void updateFavorite(Long idExercise, boolean favorited);
	
	@Query("UPDATE Exercise SET currentIdExerciseHistory = :idExerciseHistory WHERE idExercise = :idExercise")
	public abstract void updateIdHistory(Long idExercise, Long idExerciseHistory);
	
	///
	/// Inserts
	///
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract void insertExercises(ArrayList<Exercise> ex);
	
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract Long insertExercise(Exercise ex);
	
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract Long insertExerciseHistory(ExerciseHistory eh);
	
	// Insert Exercise and ExerciseHistory. Returns idExercise for the inserted exercise.
	@Transaction
	public Long insertExerciseFull(Exercise ex)
	{
		// Insert exercise.
		Long idExercise = insertExercise(ex);
		ex.setIdExercise(idExercise);
		
		// Insert history.
		Long idExerciseHistory = insertExerciseHistory(new ExerciseHistory(ex));
		// Update the inserted exercise to look at this inserted history.
		// ex.setCurrentIdExerciseHistory(idExerciseHistory);
		updateIdHistory(idExercise, idExerciseHistory);
		
		return idExercise;
	}
	
	// When updating an exercise, save it into history and update it in the Exercise table. Returns the idExerciseHistory for the inserted history.
	@Transaction
	public Long insertExerciseHistoryFull(ExerciseHistory eh, Exercise ex)
	{
		// Insert history
		Long idExerciseHistory = insertExerciseHistory(eh);
		
		ex.setCurrentIdExerciseHistory(idExerciseHistory);
		// Update exercise
		updateExercise(ex);
		//updateExerciseIdHistory(ex.getIdExercise(), idExerciseHistory);
		
		return idExerciseHistory;
	}
	
	/*
	@Query("UPDATE Exercise SET currentIdExerciseHistory = :idExerciseHistory WHERE idExercise = :idExercise")
	public abstract void updateExerciseIdHistory(Long idExercise, Long idExerciseHistory);*/
	
	///
	/// Deletes
	///
	// [TODO] Need to check if the exercise is tied to a session, then we use setExerciseAsHidden() instead. Otherwise, we can delete.
	@Delete
	public abstract void deleteExercise(Exercise ex);
	
	@Query("UPDATE Exercise SET hidden = :isHidden WHERE idExercise = :idExercise")
	public abstract void setExerciseAsHidden(Long idExercise, int isHidden); // isHidden = 1 for hidden, 0 for not hidden
	
	@Update
	public abstract void updateExercise(Exercise ex);
}
