package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.Exercise;

import java.util.Comparator;
import java.util.Locale;

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
			if((e1.getLocked() ? 1 : 0) > (e2.getLocked() ? 1 : 0))
				return -1;
			else if((e1.getLocked() ? 1 : 0) < (e2.getLocked() ? 1 : 0))
				return 1;
			
			// Order by name.
			Locale locale = Locale.US; // [TODO] this should be obtained from user profile
			int res = e1.getName().toLowerCase(locale).compareTo(e2.getName().toLowerCase(locale));
			
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
