package com.longlife.workoutlogger.data;

public class DatabaseVersion
{
	// Static
	// INITIAL mainly works with Exercise DAO
	private static final int INITIAL_A = 1; // base, nothing really new
	private static final int INITIAL_B = 2;
	private static final int INITIAL_C = 3;
	private static final int INITIAL_D = 4;
	private static final int INITIAL_E = 5;
	// V2 will provide interface with Routine DAO
	private static final int V2_0 = 6; // Split up Dao into ExerciseDao and RoutineDao
	private static final int V2_1 = 7; // Added full routine insert (routine, session, session exercise, session exercise set) whenever routine is inserted.
	private static final int V2_2 = 8; // Add exercise IsHidden flag. Instead of deleting the exercise, we set it as hidden.
	private static final int V2_3 = 9; // nothing special
	private static final int V2_4 = 10; // Added hidden flag for routine, so instead of deleting a routine, it will be set as hidden.
	private static final int V3_0 = 11; // Remove unused model fields for now.
	private static final int V3_1 = 12; // Changed id columns from int type to Long.
	private static final int V3_2 = 13; // Added exercise history insert when exercise is inserted.
	private static final int V3_3 = 14; // Made performance status for a routine session an enum, so that it can mark in-progress performances as well.
	private static final int V3_4 = 15; // Removed auto-generate from primary keys, so that keys can be reused if they are deleted.
	private static final int V3_5 = 16; // Split set rest time from only seconds to minutes and seconds.
	private static final int V3_5b = 17; // Gave set rest time a default of 0's.
	private static final int V4_0 = 18; // Added user profile.
	private static final int V4_1 = 19; // Added profile model and profile is obtained on start up.
	
	public static final int CURRENT = V4_1;
}
// Inner Classes
