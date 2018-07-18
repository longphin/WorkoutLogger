package com.longlife.workoutlogger.v2.data;

public class DatabaseVersion
{
	// INITIAL mainly works with Exercise DAO
	public static final int INITIAL_A = 1; // base, nothing really new
	public static final int INITIAL_B = 2;
	public static final int INITIAL_C = 3;
	public static final int INITIAL_D = 4;
	public static final int INITIAL_E = 5;
	// V2 will provide interface with Routine DAO
	public static final int V2_0 = 6; // Split up Dao into ExerciseDao and RoutineDao
	public static final int V2_1 = 7; // Added full routine insert (routine, session, session exercise, session exercise set) whenever routine is inserted.
	
	public static final int CURRENT = V2_1;
}
// Inner Classes
