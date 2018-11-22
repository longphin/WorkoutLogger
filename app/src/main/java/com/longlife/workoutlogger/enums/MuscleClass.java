package com.longlife.workoutlogger.enums;

import java.util.Locale;

public class MuscleClass {
    public static final int BICEP = 0;
    public static final int TRICEP = 1;
    public static final int QUADS = 2;
    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;
    private boolean isHeaderItem = false;

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
        this.isHeaderItem = true;
    }

    public static String getName(int idMuscle, Locale locale) {
        if (locale == Locale.US || locale == Locale.ENGLISH) {
            return defaultMuscleName(idMuscle);
        }

        return defaultMuscleName(idMuscle);
    }

    private static String defaultMuscleName(int idMuscle) {
        switch (idMuscle) {
            case BICEP:
                return "Bicep";
            case TRICEP:
                return "Tricep";
            case QUADS:
                return "Quads";
            default:
                return "Muscle not named";
        }
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

    public boolean isHeaderItem() {
        return isHeaderItem;
    }
}
