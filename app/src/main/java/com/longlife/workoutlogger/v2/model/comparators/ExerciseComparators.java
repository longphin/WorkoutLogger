package com.longlife.workoutlogger.v2.model.comparators;

import com.longlife.workoutlogger.v2.data.UserProfile;
import com.longlife.workoutlogger.v2.model.Exercise;

import java.util.Comparator;

/**
 * Created by Longphi on 2/2/2018.
 */
public class ExerciseComparators
{
	private static class DefaultComparator
		implements Comparator<Exercise>
	{
		// Overrides
		@Override
		public int compare(Exercise e1, Exercise e2)
		{
			// Order by favorites.
			if((e1.getFavorited() ? 1 : 0) > (e2.getFavorited() ? 1 : 0))
				return -1;
			else if((e1.getFavorited() ? 1 : 0) < (e2.getFavorited() ? 1 : 0))
				return 1;
			
			// Order by name.
			int res = e1.getName().toLowerCase(UserProfile.getLocale()).compareTo(e2.getName().toLowerCase(UserProfile.getLocale()));
			
			// Order by id.
			if(res == 0)
				return (e1.getIdExercise() >= e2.getIdExercise() ? 1 : -1);
			return (res);
		}
	}
	
	// Getters
	public static DefaultComparator getDefaultComparator()
	{
		return new DefaultComparator();
	}
}

// Inner Classes
