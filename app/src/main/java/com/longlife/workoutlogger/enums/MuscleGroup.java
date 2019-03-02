/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 12/11/18 7:36 PM.
 */

package com.longlife.workoutlogger.enums;

import android.content.Context;
import android.util.Log;

import com.longlife.workoutlogger.R;
import com.longlife.workoutlogger.model.MuscleGroupEntity;
import com.longlife.workoutlogger.utils.GetResource;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroup {
    private static final String TAG = MuscleGroup.class.getSimpleName();

    // Return the MuscleGroup id given an idMuscle. Used to determine what group a muscle is a part of.
    static int getMuscleGroupForMuscle(Context context, int idMuscle) {
        Integer idMuscleGroup = null;
        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_pec_upper, R.integer.MUSCLE_pec_middle, R.integer.MUSCLE_pec_lower},
                idMuscle,
                R.integer.MUSCLEGROUP_chest);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_traps, R.integer.MUSCLE_rhomboids, R.integer.MUSCLE_lats, R.integer.MUSCLE_lower_back},
                idMuscle,
                R.integer.MUSCLEGROUP_back);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_bicep_brachialis, R.integer.MUSCLE_biceps, R.integer.MUSCLE_triceps, R.integer.MUSCLE_forearms},
                idMuscle,
                R.integer.MUSCLEGROUP_arms);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_deltoid_posterior, R.integer.MUSCLE_deltoid_lateral, R.integer.MUSCLE_deltoid_anterior},
                idMuscle,
                R.integer.MUSCLEGROUP_shoulders);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_quads, R.integer.MUSCLE_hamstrings, R.integer.MUSCLE_calves, R.integer.MUSCLE_glutes, R.integer.MUSCLE_hip_abductors, R.integer.MUSCLE_hip_adductors},
                idMuscle,
                R.integer.MUSCLEGROUP_legs);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_abs, R.integer.MUSCLE_obliques, R.integer.MUSCLE_serratus},
                idMuscle,
                R.integer.MUSCLEGROUP_core);
        if (idMuscleGroup != null) return idMuscleGroup;

        idMuscleGroup = getMuscleGroupForMuscleIdFromResource(context,
                new int[]{R.integer.MUSCLE_cardio, R.integer.MUSCLE_stretch},
                idMuscle,
                R.integer.MUSCLEGROUP_misc);
        if (idMuscleGroup != null) return idMuscleGroup;

        Log.d(TAG, "Could not find muscle group by " + String.valueOf(idMuscle));
        return -1;
    }

    private static Integer getMuscleGroupForMuscleIdFromResource(Context context, int[] idMuscleResources, int idMuscle, int idMuscleGroupResource) {
        for (int idMuscleResource : idMuscleResources) {
            if (GetResource.getIntResource(context, idMuscleResource) == idMuscle) {
                return GetResource.getIntResource(context, idMuscleGroupResource);
            }
        }
        return null;
    }

    static String getMuscleGroupName(Context context, int idMuscleGroupResource) {
        int[] muscleGroupIdResource = getAllIdResources();
        int[] muscleGroupNameResource = getAllNameResources();

        String name = null;
        for (int i = 0; i < muscleGroupIdResource.length; i++) {
            name = getMuscleGroupNameFromResource(context, muscleGroupIdResource[i], muscleGroupNameResource[i], idMuscleGroupResource);
            if (name != null) return name;
        }
        return "Muscle group not named";
    }

    private static int[] getAllIdResources() {
        return new int[]{R.integer.MUSCLEGROUP_chest, R.integer.MUSCLEGROUP_back, R.integer.MUSCLEGROUP_arms,
                R.integer.MUSCLEGROUP_shoulders, R.integer.MUSCLEGROUP_legs, R.integer.MUSCLEGROUP_core, R.integer.MUSCLEGROUP_misc};
    }

    private static int[] getAllNameResources() {
        return new int[]{R.string.MUSCLEGROUP_chest, R.string.MUSCLEGROUP_back, R.string.MUSCLEGROUP_arms,
                R.string.MUSCLEGROUP_shoulders, R.string.MUSCLEGROUP_legs, R.string.MUSCLEGROUP_core, R.string.MUSCLEGROUP_misc};
    }

    private static String getMuscleGroupNameFromResource(Context context, int idResource, int nameResource, int idMuscleGroupResource) {
        int idMuscleGroup = GetResource.getIntResource(context, idMuscleGroupResource);
        if (GetResource.getIntResource(context, idResource) == idMuscleGroup) {
            return GetResource.getStringResource(context, nameResource);
        }

        return null;
    }

    public static List<MuscleGroupEntity> getAllMuscleGroupEntities(Context context) {
        List<MuscleGroupEntity> muscleGroups = new ArrayList<>();
        for (Integer idMuscleGroup : MuscleGroup.getAllMuscleGroupsIds(context)) {
            muscleGroups.add(new MuscleGroupEntity(idMuscleGroup));
        }

        return muscleGroups;
    }

    public static List<Integer> getAllMuscleGroupsIds(Context context) {
        int[] idMuscleGroupResources = getAllIdResources();

        List<Integer> idMuscleGroups = new ArrayList<>();
        for (int i = 0; i < idMuscleGroupResources.length; i++) {
            idMuscleGroups.add(GetResource.getIntResource(context, idMuscleGroupResources[i]));
        }
        return idMuscleGroups;
    }
}
