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
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListAdapter;
import com.longlife.workoutlogger.view.Exercises.CreateExercise.MuscleListHelper;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroup {
    static final int CHEST = 0;
    static final int BACK = 1;
    static final int ARMS = 2;
    static final int SHOULDERS = 3;
    static final int LEGS = 4;
    static final int CORE = 5;

    private static final String TAG = MuscleGroup.class.getSimpleName();

    public static List<MuscleListHelper> getAllMuscleGroups(Context context) {
        List<MuscleListHelper> groups = new ArrayList<>();
        for (Integer idMuscleGroup : MuscleGroup.getAllMuscleGroupsIds(context)) {
            groups.add(getMuscleGroup(context, idMuscleGroup));
        }

        return groups;
    }

    public static List<Integer> getAllMuscleGroupsIds(Context context) {
        List<Integer> idMuscleGroups = new ArrayList<>();
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_arms));//ARMS);
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_back));//BACK);
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_chest));//CHEST);
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_core));//CORE);
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_legs));//LEGS);
        idMuscleGroups.add(GetResource.getIntResource(context, R.integer.MUSCLEGROUP_shoulders));//SHOULDERS);

        return idMuscleGroups;
    }

    private static MuscleListHelper getMuscleGroup(Context context, int idMuscleGroup) {
        List<Muscle> muscles = new ArrayList<>();

        for (Integer idMuscle : Muscle.getAllMuscles(context)) {
            if (getMuscleGroupForMuscle(idMuscle) == idMuscleGroup) {
                muscles.add(new Muscle(context, idMuscleGroup, idMuscle));
            }
        }

        return new MuscleListHelper(idMuscleGroup, getMuscleGroupName(context, idMuscleGroup), muscles, MuscleListAdapter.NUMBER_OF_COLUMNS);
    }

    // Return the MuscleGroup id given an idMuscle. Used to determine what group a muscle is a part of.
    static int getMuscleGroupForMuscle(int idMuscle) {
        switch (idMuscle) {
            case Muscle.UPPER_PEC:
            case Muscle.MIDDLE_PEC:
            case Muscle.LOWER_PEC:
                return CHEST;

            case Muscle.TRAPS:
            case Muscle.RHOMBOIDS:
            case Muscle.LATS:
            case Muscle.LOWER_BACK:
                return BACK;

            case Muscle.BICEPS:
            case Muscle.BICEP_BRACHIALIS:
            case Muscle.TRICEPS:
            case Muscle.FOREARMS:
                return ARMS;

            case Muscle.DELTOID_ANTERIOR:
            case Muscle.DELTOID_LATERAL:
            case Muscle.DELTOID_POSTERIOR:
                return SHOULDERS;

            case Muscle.QUADS:
            case Muscle.HAMSTRINGS:
            case Muscle.CALVES:
            case Muscle.GLUTES:
            case Muscle.HIP_ABDUCTORS:
            case Muscle.HIP_ADDUCTORS:
                return LEGS;

            case Muscle.ABS:
            case Muscle.OBLIQUES:
            case Muscle.SERRATUS:
                return CORE;
        }

        Log.d(TAG, "Could not find muscle group by " + String.valueOf(idMuscle));
        return -1;
    }

    static String getMuscleGroupName(Context context, int idMuscleGroup) {
        switch (idMuscleGroup) {
            case CHEST:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_chest);
            case BACK:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_back);
            case ARMS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_arms);
            case SHOULDERS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_shoulders);
            case LEGS:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_legs);
            case CORE:
                return GetResource.getStringResource(context, R.string.MUSCLEGROUP_core);
            default:
                return "Muscle group not named";
        }
    }

    public static List<MuscleGroupEntity> getAllMuscleGroupEntities(Context context) {
        List<MuscleGroupEntity> muscleGroups = new ArrayList<>();
        for (Integer idMuscleGroup : MuscleGroup.getAllMuscleGroupsIds(context)) {
            muscleGroups.add(new MuscleGroupEntity(idMuscleGroup));
        }

        return muscleGroups;
    }
}
