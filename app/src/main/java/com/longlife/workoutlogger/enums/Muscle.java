/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 8:08 PM.
 */

package com.longlife.workoutlogger.enums;

import android.content.Context;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.MuscleEntity;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

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
    static final int SERRATUS = 19;
    // Chest
    static final int UPPER_PEC = 20;
    static final int MIDDLE_PEC = 21;
    static final int LOWER_PEC = 22;

    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;

    public static List<MuscleEntity> getAllMuscleEntities(Context context) {
        List<MuscleEntity> muscles = new ArrayList<>();
        for (Integer idMuscle : Muscle.getAllMuscles(context)) {
            muscles.add(getMuscleEntity(idMuscle));
        }

        return muscles;
    }

    private boolean isSelected = false; // Determines if the muscle was selected to be a part of an exercise. Only used when creating an exercise.

    Muscle(Context context, int idMuscleGroup, int idMuscle) {
        this.name = getMuscleName(context, idMuscle);
        this.idMuscleGroup = idMuscleGroup;
        this.idMuscle = idMuscle;
    }

    static List<Integer> getAllMuscles(Context context) {
        List<Integer> idMuscles = new ArrayList<>();
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_traps));//TRAPS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_rhomboids));//RHOMBOIDS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_lats));//LATS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_lower_back));//LOWER_BACK);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_biceps));//BICEPS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_bicep_brachialis));//BICEP_BRACHIALIS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_triceps));//TRICEPS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_forearms));//FOREARMS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_deltoid_anterior));//DELTOID_ANTERIOR);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_deltoid_lateral));//DELTOID_LATERAL);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_deltoid_posterior));//DELTOID_POSTERIOR);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_quads));//QUADS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_hamstrings));//HAMSTRINGS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_glutes));//GLUTES);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_calves));//CALVES);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_hip_adductors));//HIP_ADDUCTORS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_hip_abductors));//HIP_ABDUCTORS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_abs));//ABS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_obliques));//OBLIQUES);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_serratus));//SERRATUS);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_pec_upper));//UPPER_PEC);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_pec_middle));//MIDDLE_PEC);
        idMuscles.add(GetResource.getIntResource(context, R.integer.MUSCLE_pec_lower));//LOWER_PEC);

        return idMuscles;
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

    public static String getMuscleName(Context context, int idMuscle) {
        switch (idMuscle) {
            // Back
            case TRAPS:
                return GetResource.getStringResource(context, R.string.MUSCLE_traps);
            case RHOMBOIDS:
                return GetResource.getStringResource(context, R.string.MUSCLE_rhomboids);
            case LATS:
                return GetResource.getStringResource(context, R.string.MUSCLE_lats);
            case LOWER_BACK:
                return GetResource.getStringResource(context, R.string.MUSCLE_lower_back);
            // Arms
            case BICEPS:
                return GetResource.getStringResource(context, R.string.MUSCLE_biceps);
            case BICEP_BRACHIALIS:
                return GetResource.getStringResource(context, R.string.MUSCLE_bicep_brachialis);
            case TRICEPS:
                return GetResource.getStringResource(context, R.string.MUSCLE_triceps);
            case FOREARMS:
                return GetResource.getStringResource(context, R.string.MUSCLE_forearms);
            // Shoulders
            case DELTOID_ANTERIOR:
                return GetResource.getStringResource(context, R.string.MUSCLE_deltoid_anterior);
            case DELTOID_LATERAL:
                return GetResource.getStringResource(context, R.string.MUSCLE_deltoid_lateral);
            case DELTOID_POSTERIOR:
                return GetResource.getStringResource(context, R.string.MUSCLE_deltoid_posterior);
            // Legs
            case QUADS:
                return GetResource.getStringResource(context, R.string.MUSCLE_quads);
            case HAMSTRINGS:
                return GetResource.getStringResource(context, R.string.MUSCLE_hamstrings);
            case GLUTES:
                return GetResource.getStringResource(context, R.string.MUSCLE_glutes);
            case CALVES:
                return GetResource.getStringResource(context, R.string.MUSCLE_calves);
            case HIP_ABDUCTORS:
                return GetResource.getStringResource(context, R.string.MUSCLE_hip_abductors);
            case HIP_ADDUCTORS:
                return GetResource.getStringResource(context, R.string.MUSCLE_hip_adductors);
            // Core
            case ABS:
                return GetResource.getStringResource(context, R.string.MUSCLE_abs);
            case OBLIQUES:
                return GetResource.getStringResource(context, R.string.MUSCLE_obliques);
            case SERRATUS:
                return GetResource.getStringResource(context, R.string.MUSCLE_serratus);
            // Chest
            case UPPER_PEC:
                return GetResource.getStringResource(context, R.string.MUSCLE_pec_upper);
            case MIDDLE_PEC:
                return GetResource.getStringResource(context, R.string.MUSCLE_pec_middle);
            case LOWER_PEC:
                return GetResource.getStringResource(context, R.string.MUSCLE_pec_lower);
            default:
                return "Muscle is missing a name."; // Should never appear.
        }
    }

    private static MuscleEntity getMuscleEntity(int idMuscle) {
        return new MuscleEntity(idMuscle, MuscleGroup.getMuscleGroupForMuscle(idMuscle));
    }
}
