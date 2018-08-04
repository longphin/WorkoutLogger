package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.SessionExercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */

public class SessionExerciseComparator
	implements Comparator<SessionExercise>
{
	// Overrides
	@Override
	public int compare(SessionExercise se1, SessionExercise se2)
	{
		// Order by id.
		return (se1.getIdSessionExercise() >= se2.getIdSessionExercise() ? 1 : -1);
	}
}

// Inner Classes
