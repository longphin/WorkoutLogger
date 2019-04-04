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
    private int idMuscleGroup;
    private Integer idMuscle;
    private String name;
    private boolean isSelected = false; // Determines if the muscle was selected to be a part of an exercise. Only used when creating an exercise.

    Muscle(Context context, int idMuscleGroup, int idMuscle) {
        this.name = getMuscleName(context, idMuscle);
        this.idMuscleGroup = idMuscleGroup;
        this.idMuscle = idMuscle;
    }

    public static String getMuscleName(Context context, int idMuscle) {
        int[] muscleIdResource = getAllIdResources();
        int[] muscleNameResource = getAllNameResources();

        String name = null;
        for (int i = 0; i < muscleIdResource.length; i++) {
            name = getMuscleNameFromResource(context, muscleIdResource[i], muscleNameResource[i], idMuscle);
            if (name != null) return name;
        }
        return "Muscle is missing a name."; // Should never appear.
    }

    private static int[] getAllIdResources() {
        return new int[]{
                R.integer.MUSCLE_traps,
                R.integer.MUSCLE_rhomboids,
                R.integer.MUSCLE_lats,
                R.integer.MUSCLE_lower_back,
                R.integer.MUSCLE_biceps,
                R.integer.MUSCLE_bicep_brachialis,
                R.integer.MUSCLE_triceps,
                R.integer.MUSCLE_forearms,
                R.integer.MUSCLE_deltoid_anterior,
                R.integer.MUSCLE_deltoid_lateral,
                R.integer.MUSCLE_deltoid_posterior,
                R.integer.MUSCLE_quads,
                R.integer.MUSCLE_hamstrings,
                R.integer.MUSCLE_glutes,
                R.integer.MUSCLE_calves,
                R.integer.MUSCLE_hip_abductors,
                R.integer.MUSCLE_hip_adductors,
                R.integer.MUSCLE_abs,
                R.integer.MUSCLE_obliques,
                R.integer.MUSCLE_serratus,
                R.integer.MUSCLE_pec_lower,
                R.integer.MUSCLE_pec_middle,
                R.integer.MUSCLE_pec_upper,
                R.integer.MUSCLE_cardio,
                R.integer.MUSCLE_stretch
        };
    }

    private static int[] getAllNameResources() {
        return new int[]{R.string.MUSCLE_traps, R.string.MUSCLE_rhomboids, R.string.MUSCLE_lats,
                R.string.MUSCLE_lower_back, R.string.MUSCLE_biceps, R.string.MUSCLE_bicep_brachialis,
                R.string.MUSCLE_triceps, R.string.MUSCLE_forearms, R.string.MUSCLE_deltoid_anterior,
                R.string.MUSCLE_deltoid_lateral, R.string.MUSCLE_deltoid_posterior, R.string.MUSCLE_quads,
                R.string.MUSCLE_hamstrings, R.string.MUSCLE_glutes, R.string.MUSCLE_calves,
                R.string.MUSCLE_hip_abductors, R.string.MUSCLE_hip_adductors, R.string.MUSCLE_abs,
                R.string.MUSCLE_obliques, R.string.MUSCLE_serratus, R.string.MUSCLE_pec_lower,
                R.string.MUSCLE_pec_middle, R.string.MUSCLE_pec_upper, R.string.MUSCLE_cardio,
                R.string.MUSCLE_stretch};
    }

    private static String getMuscleNameFromResource(Context context, int idResource, int nameResource, int idMuscle) {
        if (GetResource.getIntResource(context, idResource) == idMuscle) {
            return GetResource.getStringResource(context, nameResource);
        }
        return null;
    }

    public static List<MuscleEntity> getAllMuscleEntities(Context context) {
        List<MuscleEntity> muscles = new ArrayList<>();
        for (Integer idMuscle : Muscle.getAllMuscles(context)) {
            muscles.add(getMuscleEntity(context, idMuscle));
        }

        return muscles;
    }

    private static List<Integer> getAllMuscles(Context context) {
        int[] idMuscleResources = getAllIdResources();

        List<Integer> idMuscles = new ArrayList<>();
        for (int idMuscleResource : idMuscleResources) {
            idMuscles.add(GetResource.getIntResource(context, idMuscleResource));
        }
        return idMuscles;
    }

    private static MuscleEntity getMuscleEntity(Context context, int idMuscle) {
        return new MuscleEntity(idMuscle, MuscleGroup.getMuscleGroupForMuscle(context, idMuscle));
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
}
