package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.utils.GetResource;

public class Muscle {
    // Back
    static final int TRAPS = 0;
    static final int RHOMBOIDS = 1;
    static final int LATS = 2;
    static final int LOWER_BACK = 3;
    // Arms
    static final int BICEPS = 4;
    static final int BICEP_BRACHIALIS = 5;
    static final int TRICEPS = 6;
    static final int FOREARMS = 7;
    // Shoulders
    static final int DELTOID_ANTERIOR = 8;
    static final int DELTOID_LATERAL = 9;
    static final int DELTOID_POSTERIOR = 10;
    // Legs
    static final int QUADS = 11;
    static final int HAMSTRINGS = 12;
    static final int GLUTES = 13;
    static final int CALVES = 14;
    static final int HIP_ADDUCTORS = 15;
    static final int HIP_ABDUCTORS = 16;
    // Core
    static final int ABS = 17; // [TODO] break up abs into lower, middle, and upper?
    static final int OBLIQUES = 18;
    // Chest
    static final int PEC_MAJOR = 19; // [TODO] add the other pecs?

    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;

    private boolean isSelected = false; // Determines if the muscle was selected to be a part of an exercise. Only used when creating an exercise.

    public Muscle(Context context, int idMuscleGroup, int idMuscle) {
        switch (idMuscle) {
            // Back
            case TRAPS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_traps);
                break;
            case RHOMBOIDS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_rhomboids);
                break;
            case LATS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_lats);
                break;
            case LOWER_BACK:
                name = GetResource.getStringResource(context, R.string.MUSCLE_lower_back);
                break;
            // Arms
            case BICEPS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_biceps);
                break;
            case BICEP_BRACHIALIS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_bicep_brachialis);
                break;
            case TRICEPS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_triceps);
                break;
            case FOREARMS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_forearms);
                break;
            // Shoulders
            case DELTOID_ANTERIOR:
                name = GetResource.getStringResource(context, R.string.MUSCLE_deltoid_anterior);
                break;
            case DELTOID_LATERAL:
                name = GetResource.getStringResource(context, R.string.MUSCLE_deltoid_lateral);
                break;
            case DELTOID_POSTERIOR:
                name = GetResource.getStringResource(context, R.string.MUSCLE_deltoid_posterior);
                break;
            // Legs
            case QUADS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_quads);
                break;
            case HAMSTRINGS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_hamstrings);
                break;
            case GLUTES:
                name = GetResource.getStringResource(context, R.string.MUSCLE_glutes);
                break;
            case CALVES:
                name = GetResource.getStringResource(context, R.string.MUSCLE_calves);
                break;
            case HIP_ABDUCTORS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_hip_abductors);
                break;
            case HIP_ADDUCTORS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_hip_adductors);
                break;
            // Core
            case ABS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_abs);
                break;
            case OBLIQUES:
                name = GetResource.getStringResource(context, R.string.MUSCLE_obliques);
                break;
            // Chest
            case PEC_MAJOR:
                name = GetResource.getStringResource(context, R.string.MUSCLE_pec_major);
                break;
            default:
                name = "Muscle is missing a name."; // Should never appear.
                break;
        }

        this.idMuscleGroup = idMuscleGroup;
        this.idMuscle = idMuscle;
    }

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
