package com.longlife.workoutlogger.model.comparators;

import com.longlife.workoutlogger.model.Routine;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Longphi on 2/2/2018.
 */
public class RoutineComparators
{
	private static class DefaultComparator
		implements Comparator<Routine>
	{
		// Overrides
		@Override
		public int compare(Routine r1, Routine r2)
		{
			// Order by name.
			Locale locale = Locale.US; // [TODO] this should be obtained from user profile
			int res = r1.getName().toLowerCase(locale).compareTo(r2.getName().toLowerCase(locale));
			
			// Order by id.
			if(res == 0)
				return (r1.getIdRoutine() >= r2.getIdRoutine() ? 1 : -1);
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
