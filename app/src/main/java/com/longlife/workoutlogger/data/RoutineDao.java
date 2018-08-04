package com.longlife.workoutlogger.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.longlife.workoutlogger.model.Exercise;
import com.longlife.workoutlogger.model.Routine;
import com.longlife.workoutlogger.model.RoutineHistory;
import com.longlife.workoutlogger.model.RoutineSession;
import com.longlife.workoutlogger.model.SessionExercise;
import com.longlife.workoutlogger.model.SessionExerciseSet;
import com.longlife.workoutlogger.view.Routines.Helper.RoutineExerciseHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

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
	@Query("SELECT * FROM Routine WHERE hidden = 0")
	public abstract Single<List<Routine>> getRoutines();
	
	@Query("SELECT * FROM RoutineSession WHERE idRoutineHistory = :idRoutine AND wasPerformed = 0 ORDER BY sessionDate DESC LIMIT 1")
	public abstract Single<RoutineSession> getLatestRoutineSession(Long idRoutine);
	
	@Query("SELECT * FROM SessionExercise WHERE idRoutineSession = :idRoutineSession")
	public abstract Single<List<SessionExercise>> getSessionExercises(Long idRoutineSession);
	
	@Query("SELECT e.*" +
		" FROM SessionExercise as se" +
		" INNER JOIN Exercise as e on se.idExerciseHistory=e.idExercise" +
		" WHERE se.idSessionExercise = :idSessionExercise")
	public abstract Single<Exercise> getExerciseFromSession(Long idSessionExercise);
	
	@Query("SELECT * FROM Routine WHERE idRoutine = :idRoutine")
	public abstract Single<Routine> getRoutine(Long idRoutine);
	
	///
	/// UPDATE
	///
	///
	/// Inserts
	///
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract void insertRoutines(ArrayList<Routine> r);
	
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract Long insertRoutine(Routine r);
	
	@Insert(onConflict = OnConflictStrategy.ROLLBACK)
	public abstract Long insertRoutineHistory(RoutineHistory rh);
	
	@Transaction
	public Long insertRoutineFull(Routine r, List<RoutineExerciseHelper> reh)
	{
		// Insert routine.
		Long idRoutine = insertRoutine(r);
		r.setIdRoutine(idRoutine);
		// Insert this newly created routine to history.
		Long idRoutineHistory = insertRoutineHistory(new RoutineHistory(r));
		// Insert routine session using the new routine id.
		RoutineSession routineSessionToAdd = new RoutineSession();
		routineSessionToAdd.setIdRoutineHistory(idRoutineHistory);
		Long idRoutineSession = insertRoutineSession(routineSessionToAdd);
		// Insert exercises for the session using the new session.
		for(int i = 0; i < reh.size(); i++){
			//seArray[i] = new SessionExercise(reh.get(i).getExercise().getIdRoutineHistory(), idRoutineSession);
			Long idSessionExercise = insertSessionExercise(new SessionExercise(reh.get(i).getExercise().getIdExercise(), idRoutineSession));
			List<SessionExerciseSet> setsToAdd = reh.get(i).getSets();
			for(int j = 0; j < setsToAdd.size(); j++){
				setsToAdd.get(j).setIdSessionExercise(idSessionExercise);
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
	
	@Query("UPDATE Routine SET hidden = :isHidden WHERE idRoutine=:idRoutine")
	public abstract void setRoutineAsHidden(Long idRoutine, int isHidden);
}
