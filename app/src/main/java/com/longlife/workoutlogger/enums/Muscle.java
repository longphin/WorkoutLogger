package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

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
    public static final int SERRATUS = 19;
    // Chest
    public static final int UPPER_PEC = 20;
    public static final int MIDDLE_PEC = 21;
    public static final int LOWER_PEC = 22;

    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;

    public static List<MuscleEntity> getAllMuscleEntities() {
        List<MuscleEntity> muscles = new ArrayList<>();
        for (Integer idMuscle : Muscle.getAllMuscles()) {
            muscles.add(getMuscleEntity(idMuscle));
        }

        return muscles;
    }
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
            case SERRATUS:
                name = GetResource.getStringResource(context, R.string.MUSCLE_serratus_anterior);
                break;
            // Chest
            case UPPER_PEC:
                name = GetResource.getStringResource(context, R.string.MUSCLE_pec_upper);
                break;
            case MIDDLE_PEC:
                name = GetResource.getStringResource(context, R.string.MUSCLE_pec_middle);
                break;
            case LOWER_PEC:
                name = GetResource.getStringResource(context, R.string.MUSCLE_pec_lower);
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

    public static List<Integer> getAllMuscles() {
        List<Integer> idMuscles = new ArrayList<>();
        idMuscles.add(TRAPS);
        idMuscles.add(RHOMBOIDS);
        idMuscles.add(LATS);
        idMuscles.add(LOWER_BACK);
        idMuscles.add(BICEPS);
        idMuscles.add(BICEP_BRACHIALIS);
        idMuscles.add(TRICEPS);
        idMuscles.add(FOREARMS);
        idMuscles.add(DELTOID_ANTERIOR);
        idMuscles.add(DELTOID_LATERAL);
        idMuscles.add(DELTOID_POSTERIOR);
        idMuscles.add(QUADS);
        idMuscles.add(HAMSTRINGS);
        idMuscles.add(GLUTES);
        idMuscles.add(CALVES);
        idMuscles.add(HIP_ADDUCTORS);
        idMuscles.add(HIP_ABDUCTORS);
        idMuscles.add(ABS);
        idMuscles.add(OBLIQUES);
        idMuscles.add(SERRATUS);
        idMuscles.add(UPPER_PEC);
        idMuscles.add(MIDDLE_PEC);
        idMuscles.add(LOWER_PEC);

        return idMuscles;
    }

    private static MuscleEntity getMuscleEntity(int idMuscle) {
        return new MuscleEntity(idMuscle, MuscleGroup.getMuscleGroupForMuscle(idMuscle));
    }
}
