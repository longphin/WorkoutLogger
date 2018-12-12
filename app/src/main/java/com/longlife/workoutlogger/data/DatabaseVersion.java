package com.longlife.workoutlogger.data;

// A list of database versions.
public class DatabaseVersion {

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
    // V3 will provide interface for sets.
    private static final int V3_0 = 11; // Remove unused model fields for now.
    private static final int V3_1 = 12; // Changed id columns from int type to Long.
    private static final int V3_2 = 13; // Added exercise history insert when exercise is inserted.
    private static final int V3_3 = 14; // Made performance status for a routine session an enum, so that it can mark in-progress performances as well.
    private static final int V3_4 = 15; // Removed auto-generate from primary keys, so that keys can be reused if they are deleted.
    private static final int V3_5 = 16; // Split set rest time from only seconds to minutes and seconds.
    private static final int V3_5b = 17; // Gave set rest time a default of 0's.
    // V4 will provide interface for profile and performing exercises.
    private static final int V4_0 = 18; // Added user profile.
    private static final int V4_1 = 19; // Added profile model and profile is obtained on start up.
    private static final int V4_2 = 20; // Changed exercise favorited to locked icon.
    private static final int V4_3 = 21; // Add methods for initially creating an exercise session and obtaining an exercise session.
    private static final int V4_3b = 22; // Fixing some NULL annotations.
    private static final int V4_4 = 23; // Combining exercise history to Exercise entity.
    private static final int V4_4b = 24; // fixes inserting exercise.
    private static final int V4_4c = 25; // Removed routine history.
    private static final int V4_4d = 26; // Added type for set.
    private static final int V4_4e = 27; // Fixed null annotation for sets.
    // V5 will remove history recording from exercises.
    private static final int V5 = 28; // Removing exercise history. Instead, history should be applied to session exercise.
    private static final int V5_1 = 29; // recreating data.
    private static final int V5_2 = 30; // Add weight unit to set object.
    private static final int V5_3 = 31; // Change how weight unit type is defined. No longer using xml, in case the weight was not translated. It will all come from a class object now.
    private static final int V5_4 = 32; // Added was performed status to performing a set, and changed icon.
    // V6 will replace Exercise with ExerciseShort objects where necessary, to reduce the amount of memory used.
    private static final int V6_0 = 33; // Changing exercise lists to use ExerciseShort object instead.
    private static final int V6_1 = 34; // Removed indices from Exercise table.
    // V7 Will add preloaded dummy data. -- [TODO] ON HOLD
    private static final int V7_0 = 35; // test Room callback.
    private static final int V7_0b = 36; // Adding a data.
    private static final int V7_0c = 37; // test
    private static final int V7_0d = 38;
    private static final int V7_0e = 39;
    private static final int V7_1 = 40; // Preloaded data is now written in a json file.
    // V8 Will add related muscles to exercises.
    private static final int V8_0 = 41; // Added Muscles and Muscle Groups, and related muscles to exercises.
    private static final int V8_1 = 42; // Changed ExerciseMuscle to use idMuscle instead. When inserting an exercise, it will also insert ExerciseMuscle records.
    private static final int V8_2 = 43; // Changed Exercise update process to use same fragment as Exercise create. Added muscles when updating the exercise.
    // V9 will add workout program.
    private static final int V9_0 = 44;
    private static final int V9_1 = 45; // Adding Muscle Entity and Muscle Group Entity tables.
    private static final int V9_1b = 46; // Ooops. Muscle Entity was initialized incorrectly.
    private static final int V9_1c = 47; // Recreate database to find debug error related to the first exercise being created.
    private static final int V9_1d = 48; // Recreate database to find debug error related to the first exercise being created.

    static final int CURRENT = V9_1d;
}

