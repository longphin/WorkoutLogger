package com.longlife.workoutlogger.enums;

public class Muscle {
    // Back
    public static final int TRAPS = 0;
    public static final int RHOMBOIDS = 1;
    public static final int LATS = 2;
    public static final int LOWER_BACK = 3;
    // Arms
    public static final int BICEPS = 4;
    public static final int BICEP_BRACHIALIS = 5;
    public static final int TRICEPS = 6;
    public static final int FOREARMS = 7;
    // Shoulders
    public static final int DELTOID_ANTERIOR = 8;
    public static final int DELTOID_LATERAL = 9;
    public static final int DELTOID_POSTERIOR = 10;
    // Legs
    public static final int QUADS = 11;
    public static final int HAMSTRINGS = 12;
    public static final int GLUTES = 13;
    public static final int CALVES = 14;
    public static final int HIP_ADDUCTORS = 15;
    public static final int HIP_ABDUCTORS = 16;
    // Core
    public static final int ABS = 17; // [TODO] break up abs into lower, middle, and upper?
    public static final int OBLIQUES = 18;
    // Chest
    public static final int PEC_MAJOR = 19; // [TODO] add the other pecs?

    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;

    private boolean isSelected = false; // Determines if the muscle was selected to be a part of an exercise. Only used when creating an exercise.

    // Constructor for a muscle.
    public Muscle(int idMuscleGroup, int idMuscle, String name) {
        this.idMuscleGroup = idMuscleGroup;
        this.idMuscle = idMuscle;
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIdMuscle() {
        return idMuscle;
    }

    public String getName() {
        return name;
    }

    public int getIdMuscleGroup() {
        return idMuscleGroup;
    }

    public void changeSelectedStatus() {
        isSelected = !isSelected;
    }
}
