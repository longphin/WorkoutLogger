package com.longlife.workoutlogger.v2.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.longlife.workoutlogger.v2.model.Exercise;
import com.longlife.workoutlogger.v2.model.Routine;
import com.longlife.workoutlogger.v2.model.RoutineSession;
import com.longlife.workoutlogger.v2.model.SessionExercise;
import com.longlife.workoutlogger.v2.model.SessionExerciseSet;
import com.longlife.workoutlogger.v2.utils.Conversions;
import com.longlife.workoutlogger.v2.view.RoutineOverview.RoutineExerciseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

// Inner Classes

/**
 * Created by Longphi on 1/3/2018.
 */

@android.arch.persistence.room.Dao
public abstract class RoutineDao
{
	// Getters
	///
	/// Gets
	///
	@Query("SELECT * FROM Routine")
	public abstract Flowable<List<Routine>> getRoutines();
	
	@Query("SELECT * FROM RoutineSession WHERE idRoutine = :idRoutine AND wasPerformed = 0 ORDER BY sessionDate DESC LIMIT 1")
	public abstract Maybe<RoutineSession> getLatestRoutineSession(int idRoutine);
	
	@Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
	public abstract Maybe<List<SessionExercise>> getSessionExercises(int idRoutineSession);
	
	@Query("SELECT e.*" +
		" FROM SessionExercise as se" +
		" INNER JOIN Exercise as e on se.idExercise=e.idExercise" +
		" WHERE se.idSessionExercise = :idSessionExercise")
	public abstract Maybe<Exercise> getExerciseFromSession(int idSessionExercise);
	
	@Query("SELECT * FROM Routine WHERE idRoutine = :idRoutine")
	public abstract Flowable<Routine> getRoutine(int idRoutine);
	
	///
	/// UPDATE
	///
	@Query("UPDATE Routine SET displayOrder = :order WHERE idRoutine = :idRoutine")
	public abstract void updateDisplayOrder(int idRoutine, int order);
	
	///
	/// Inserts
	///
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract void insertRoutines(ArrayList<Routine> r);
	
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract Long insertRoutine(Routine r);
	
	@Transaction
	public Long insertRoutineFull(Routine r, List<RoutineExerciseHelper> reh)
	{
		// Insert routine.
		Long idRoutine = insertRoutine(r);
		// Insert routine session using the new routine id.
		Long idRoutineSession = insertRoutineSession(new RoutineSession(Conversions.safeLongToInt(idRoutine)));
		// Insert exercises for the session using the new session.
		for(int i = 0; i < reh.size(); i++){
			//seArray[i] = new SessionExercise(reh.get(i).getExercise().getIdExercise(), idRoutineSession);
			Long idSessionExercise = insertSessionExercise(new SessionExercise(reh.get(i).getExercise().getIdExercise(), Conversions.safeLongToInt(idRoutineSession)));
			List<SessionExerciseSet> setsToAdd = reh.get(i).getSets();
			for(int j = 0; j < setsToAdd.size(); j++){
				setsToAdd.get(j).setIdSessionExercise(Conversions.safeLongToInt(idSessionExercise));
			}
			insertSessionExerciseSets(setsToAdd);
		}
		return idRoutine;
	}
	
	@Insert
	public abstract Long insertSessionExercise(SessionExercise se);
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public abstract Long insertRoutineSession(RoutineSession rs);
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public abstract Long[] insertSessionExercises(SessionExercise[] se);
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	public abstract void insertSessionExerciseSets(List<SessionExerciseSet> ses);
	
	///
	/// Deletes
	///
	@Delete
	public abstract void deleteRoutine(Routine r);
	
	@Delete
	public abstract void deleteRoutineSession(RoutineSession rs);
	
	@Delete
	public abstract void deleteSessionExercise(SessionExercise se);
	
	@Delete
	public abstract void deleteSessionExerciseSet(SessionExerciseSet ses);

}
