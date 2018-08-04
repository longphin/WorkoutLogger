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
	
	public static final int CURRENT = V3_1;
}
// Inner Classes
