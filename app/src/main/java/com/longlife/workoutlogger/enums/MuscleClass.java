package com.longlife.workoutlogger.enums;

import java.util.Locale;

public class MuscleClass {
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
    private boolean isExpanded = false; // Only relevant if isHeaderItem = true.

    private static String defaultMuscleName(int idMuscle) {
        switch (idMuscle) {
            case TRAPS:
                return "Traps";
            case RHOMBOIDS:
                return "Rhomboids";
            case LATS:
                return "Lats";
            case LOWER_BACK:
                return "Lower Back";
            case BICEPS:
                return "Biceps";
            case BICEP_BRACHIALIS:
                return "Bicep Brachialis";
            case TRICEPS:
                return "Triceps";
            case FOREARMS:
                return "Forearms";
            case DELTOID_ANTERIOR:
                return "Anterior Deltoid";
            case DELTOID_LATERAL:
                return "Lateral Deltoid";
            case DELTOID_POSTERIOR:
                return "Posterior Deltoid";
            case QUADS:
                return "Quads";
            case HAMSTRINGS:
                return "Hamstrings";
            case GLUTES:
                return "Glutes";
            case CALVES:
                return "Calves";
            case HIP_ADDUCTORS:
                return "Hip Adductors";
            case HIP_ABDUCTORS:
                return "Hip Abductors";
            case ABS:
                return "Abs";
            case OBLIQUES:
                return "Obliques";
            case PEC_MAJOR:
                return "Pecs";
            default:
                return "Muscle not named";
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    // Constructor for a muscle.
    public MuscleClass(int idMuscleGroup, int idMuscle, String name) {
        this.idMuscleGroup = idMuscleGroup;
        this.idMuscle = idMuscle;
        this.name = name;
    }

    // Constructor for muscle group only, i.e. this is the header item for the muscle group, not an actual muscle.
    public MuscleClass(int idMuscleGroup, String name) {
        this.idMuscleGroup = idMuscleGroup;
        this.name = name;
    }

    public static String getName(int idMuscle, Locale locale) {
        if (locale == Locale.US || locale == Locale.ENGLISH) {
            return defaultMuscleName(idMuscle);
        }

        return defaultMuscleName(idMuscle);
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
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

}
